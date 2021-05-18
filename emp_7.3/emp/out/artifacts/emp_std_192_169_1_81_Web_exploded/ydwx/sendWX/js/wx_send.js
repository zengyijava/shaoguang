var isIE = (document.all) ? true : false;
var isIE6 = isIE && (navigator.appVersion.match(/MSIE 6.0/i)=="MSIE 6.0" ? true : false);
var contentLen = 900;
var baseContentLen = 900;
var path = $("#cpath").val();

$(document).ready(
	function () {
        setFormName('form2');
        floating("downlinks","infomodel");
        $('#moreSelect').hide();
        $('#u_o_c_explain').toggle(function(){
            $("#foldIcon").removeClass("unfold");
            $("#foldIcon").addClass("fold");
            $('#moreSelect').show();
//            $('#u_o_c_explain').addClass("changeFold");
        },function(){
            $("#foldIcon").removeClass("fold");
            $("#foldIcon").addClass("unfold");
            $('#moreSelect').hide();
            $('#u_o_c_explain').removeClass("changeFold");
        });
        $("#picTab td").hover(function(){
            $(this).removeClass("div_bg");
            $(this).addClass("div_hover_bg img_hover");
        },function(){
            $(this).removeClass("div_hover_bg img_hover");
            $(this).addClass("div_bg");
        });
        $("#showeffinfo").toggle(function() {
            $("#effinfo").show();
            $("#arrowhead").removeClass("unfold");
            $("#arrowhead").addClass("fold");
            /*if($("#messages1").is(":hidden")){
                $("#effinfotable").css("top","62px");
            }
            else
            {
               $("#effinfotable").css("top","90px");
            }*/
        }, function() {
            $("#effinfo").hide();
            $("#arrowhead").removeClass("fold");
            $("#arrowhead").addClass("unfold");
        });
        getLoginInfo("#hiddenValueDiv");
        if(findresult==="-1") {
            alert(getJsLocaleMessage("ydwx","ydwx_common_jzymshbqjchwl"));
            return;
        }
        //触发账户change事件
        $("#spUser").trigger("change");
        $("#tmplDiv").dialog({
            autoOpen: false,
            height:595,
            width: 645,
            resizable:false,
            modal: true
        });
        $("#divBox").dialog({
            autoOpen: false,
            height:550,
            width: 300,
            modal: true,
            close:function(){
            }
        });
        $(".ym").change(function(){
            var id=$(this).val();
            for(var i=0;i<listpage.length;i++){
                if(id==listpage[i].id){
                    if(listpage[i].content=="notexists"){
                        $("#nm_preview_common1").attr("src","ydwx/wap/404.jsp");
                    }else{
                        $("#nm_preview_common1").attr("src","file/wx/PAGE/wx_"+listpage[i].id+".jsp");
                    }

                }
            }
        });
        $("#selDh").change(function(){
            var id=$(this).val();
            for(var i=0;i<listpage.length;i++){
                if(id==listpage[i].id){
                    if(listpage[i].content=="notexists"){
                        $("#nm_preview_common").attr("src","ydwx/wap/404.jsp");
                    }else{
                        $("#nm_preview_common").attr("src","file/wx/PAGE/wx_"+listpage[i].id+".jsp");
                    }
                }
            }
        });
        var empLangName = getJsLocaleMessage("ydwx","ydwx_yy");
    });
//详情下载
function uploadbadFiles() {
	var badurl = $("#badurl").val();
	badurl = badurl.replace("_view.txt","_bad.txt");
	$.post("wx_send.htm?method=goToFile", {
		url : badurl
	},
	function(result) {
		if (result === "true") {
			download_href(path + "/doExport.hts?u="+badurl);
		} else if (result === "false")
			alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_116"));
		else
			alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_117"));
	});
}
//新增网讯
function toAdd(){
	var menuCode=$("#wxtempcode").val();
	var lguserid=$("#lguserid").val();
	var lgcorpcode=$("#lgcorpcode").val();
	window.parent.openNewTab(menuCode,path+"/wx_ueditor.htm?method=find&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode+"&type=1");
}


function hideSelect() {
	$("#priority").css("display","none");
	$("#spUser").css("display","none");
	$("#sendType").css("display","none");
	$("#tempSelect").css("display","none");
}

function showSelect()
{
	$("#priority").css("display","inline");
	$("#spUser").css("display","none");
	$("#sendType").css("display","inline");
	$("#tempSelect").css("display","inline");
}
//短信模板处理
function tempSure()
{
	var $fo = $("#contentFrame").contents();
	var $ro = $fo.find("input[type='radio']:checked");
	if($ro.val() == undefined)
	{
		alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_1"));
		return;
	}else
	{
		var message = $ro.next("xmp").text();
		var msgob = $("form[name='" + formName + "']").find("textarea[name='msg']");
		msgob.attr("readonly","readonly");
		msgob.val(message);
		msgob.css("background-color","#E8E8E8");
	}
	$("#ctem").hide();
	$("#qtem").show();
	len(msgob);
	$("#tempDiv").dialog("close");
	$("#depSign").attr("checked",false);
	$("#nameSign").attr("checked",false);
}
function delAddr() {
	if(confirm(getJsLocaleMessage("ydwx","ydwx_jtwxfs_2"))){
		// <%--代表的是选择的ID--%>
		//  $(window.frames['flowFrame'].document).find("#choiceId").val("");
		  //<%--代表的是选择的名称 --%>
		//  $(window.frames['flowFrame'].document).find("#choiceName").val("");
		  // <%--代表的是员工机构IDS --%>
		  $(window.frames['flowFrame'].document).find("#empDepIdsStrs").val("");
		  // <%--代表的是客户机构IDS --%>
		  $(window.frames['flowFrame'].document).find("#cliDepIdsStrs").val("");
		  // <%--代表的是群组IDS --%>wx_send.js
		  $(window.frames['flowFrame'].document).find("#groupIdsStrs").val("");
		  
		  $(window.frames['flowFrame'].document).find("#groupClientStr").val("");
		  //<%--代表的是分页索引第一页 --%>
		  $(window.frames['flowFrame'].document).find("#pageIndex").val("1");
		  //<%--代表的是员工IDS --%>
		  $(window.frames['flowFrame'].document).find("#employeeIds").val("");
		  //<%--代表的是客户IDS--%>
		  $(window.frames['flowFrame'].document).find("#clientIds").val("");
		  //<%--代表的是外部人员IDS --%>
		  $(window.frames['flowFrame'].document).find("#malistIds").val("");
		  $(window.frames['flowFrame'].document).find("#right").empty();
		  $(window.frames['flowFrame'].document).find("#getChooseMan").empty();
		  $(window.frames['flowFrame'].document).find("#rightSelectTempAll").empty();
		  //<%--选择用户对象号码串 --%>
		  $(window.frames['flowFrame'].document).find("#moblieStrs").val("");
		  $(window.frames['flowFrame'].document).find("#manCount").html(0);
		  $("#ygtxl").remove();
		  //<%-- 把主页面的数据也清空--%>
		  $("#empDepIds").val("");
		  $("#cliDepIds").val("");
		  $("#groupIds").val("");
		  $("#groupClient").val("");
		  $("#phoneStr1").val("");
	 }
	
}

//回填
function optionBack(){
	var trs= $("#vss").children().children();           
    $.each(trs,function(i,n){
        var trId=$(n).attr("id");
        var index=trId.indexOf("dep");
        var name=$($(n).children()[0]).text();
        if(index>-1)
        {
            var depId = trId.substring(index+3);
       	    $(window.frames['flowFrame'].document).find("#right").append("<option value='"+depId+"' mobile=''>"+name+"</option>");					
        }
    });
}
//
function showMenu() {
	var sortSel = $("#groupResouce");
	var sortOffset = $("#groupResouce").offset();
	$("#dropMenu").toggle();
}
//显示div层
function doSelectEClose(){
	$("#infoDiv").dialog("close");
}
//文本框数字控制
function numberControl(va)
{
	var pat=/[`~!@#$%^&*()_+<>?:"{},.\/;'[\]]/im;
	if(pat.test(va.val()))
	{
		va.val(va.val().replace(/[^\d]/g,''));
	}
}
//取消
function btcancel1()
{
    showSelect();
	$("#subSend").attr("disabled","");
	$("#qingkong").attr("disabled","");
	$("#detail_Info").dialog("close");
}	
	
function infomodelop(){
	$("#infomodel").dialog({
		modal:true,
		title:getJsLocaleMessage("ydwx","ydwx_jtwxfs_3"), 
		height:300,
		closeOnEscape: false,
		width:500,
		close:function(){
		}
		});
}

//点击选择对象中的  清空按钮
function clearUserInfo(){
    var optionSize = $(window.frames['flowFrame'].document).find("#right option").size();
    if(optionSize === 0){
        clearUser();
        //$("#infoDiv").dialog("close");
        return;
    }
    //该操作将清空所选择对象,是否执行？
    if(confirm("该操作将清空所选择对象,是否执行？")){
    	$("#ygtxl").remove();
    	$("#rightSelectedUserOption").empty();
        //代表的是选择的ID
        //  $(window.frames['flowFrame'].document).find("#choiceId").val("");
        //代表的是选择的名称
        //  $(window.frames['flowFrame'].document).find("#choiceName").val("");
        //代表的是员工机构IDS
        $(window.frames['flowFrame'].document).find("#empDepIdsStrs").val("");
        //代表的是客户机构IDS
        $(window.frames['flowFrame'].document).find("#cliDepIdsStrs").val("");
        //代表的是群组IDS
        $(window.frames['flowFrame'].document).find("#groupIdsStrs").val("");
        //代表的是分页索引第一页
        $(window.frames['flowFrame'].document).find("#pageIndex").val("1");
        //代表的是员工IDS
        $(window.frames['flowFrame'].document).find("#employeeIds").val("");
        //代表的是客户IDS
        $(window.frames['flowFrame'].document).find("#clientIds").val("");
        //代表的是外部人员IDS
        $(window.frames['flowFrame'].document).find("#malistIds").val("");
        $(window.frames['flowFrame'].document).find("#right").empty();
        $(window.frames['flowFrame'].document).find("#getChooseMan").empty();
        //选择用户对象号码串
        $(window.frames['flowFrame'].document).find("#moblieStrs").val("");
        $(window.frames['flowFrame'].document).find("#manCount").html(0);

        clearUser();

        //$("#SelectPerson").dialog("close");
    }
}

//因为机构id字符串是在选择机构按钮事件生成的所以当用户选择机构后点击取消时应该将生成的字符串还原回去
function doNo(){
	$(window.frames['flowFrame'].document).find("#right").empty();
	$(window.frames['flowFrame'].document).find("#getChooseMan").empty();
	if($("#choiceNum").html()==null){
		$(window.frames['flowFrame'].document).find("#manCount").html("0");
	}else{
		$(window.frames['flowFrame'].document).find("#manCount").html($("#choiceNum").html());
	}
	$(window.frames['flowFrame'].document).find("#rightSelectTemp").empty();
	showSelect();
}
// 进程处理
function Process(cutNum){
	$.post("wx_send.htm",{method:"filter",cutNum:cutNum},
	function(data){
		var cur=data.split(',');
		if(cur[0]<cur[1]){
			alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_4"));
			Pro(cur[0],cur[1]);
			return  Process(cur[0]);
		}else if(cur[0]==cur[1]){
			Pro(cur[0],cur[1]);
			isPre="1";//加载完成
			setTimeout(clo,1000);
        	return 1;
    	}
	})
}

//层的遮罩效果
function zhezhao(){
    var width = "100%";
	var height = "100%";
	//setzhezhao("true");
	qdzz();
	var newDiv = document.createElement("div");
	newDiv.id = "loading2";
	newDiv.style.position = "fixed";
	newDiv.style.zIndex = "100";
	newDiv.style.width = width;
	newDiv.style.height = height;
	newDiv.style.top = "0";
	newDiv.style.left = "0";
	newDiv.style.background = "#999";
	newDiv.style.filter = "alpha(opacity=60)";
	newDiv.style.opacity = 0.6;
	newDiv.innerHTML = '<iframe style="border:0;position:absolute;top:0;left:0;width:99%;height:99%;filter:alpha(opacity=0);"></iframe>'
	if(isIE6){
		newDiv.style.position = "absolute";
		newDiv.style.width = Math.max(document.documentElement.scrollWidth, document.documentElement.clientWidth) + "px";
		newDiv.style.height = Math.max(document.documentElement.scrollHeight, document.documentElement.clientHeight) + "px";
	}
	document.body.appendChild(newDiv);
	newDiv.style.display="block";
}

function qdzz()
{
	if (null != document.getElementById("loading2"))
	{
		document.body.removeChild(document.getElementById("loading2"));
	}
}
//定义replaceAll方法
String.prototype.replaceAll = function(s1, s2) {
	return this.replace(new RegExp(s1, "gm"), s2);
}
//定义startWith方法
String.prototype.startWith=function(str){
	if(str==null||str==""||this.length==0||str.length>this.length)
	return false;
	if(this.substr(0,str.length)==str)
	return true;
	else
	return false;
	return true;
}
// 统计短信内容字数
function len(ob) {
	var content = $.trim(ob.val());
	var smsTailContens="";
    if($('#tail-area').is(':visible')){
    	smsTailContens = $.trim($('#smsTailContens').text())||'';
    }
    //加上贴尾之后的总长度
	var totalLen=parseInt(content.length)+parseInt(smsTailContens.length);
	if(ob.is("#contents")){
		if(totalLen>contentLen)
		{	//减去贴尾之后的总长度
			var fontlen=contentLen-parseInt(smsTailContens.length);
			$("#contents").val(content.substring(0,fontlen));
		}
	}
	var huiche = content.length - content.replaceAll("\n", "").length;
	var len = contentLen;
	
	countSMS($("#contents").val().length + huiche+smsTailContens.length, content+smsTailContens);
	if(len != contentLen)
	{
		//由于通过countSMS方法处理了内容长度，所以需要重新取值
		var totalChange=parseInt($("#contents").val().length)+parseInt(smsTailContens.length);
		if(totalChange>contentLen)
			//处理贴尾
		{	var fontlen=contentLen-parseInt(smsTailContens.length);
			$("#contents").val($("#contents").val().substring(0,fontlen));
//			content = $("#contents").val();
//			huiche = content.length - content.replaceAll("\n", "").length;
		}
		$('#maxLen').html("/"+contentLen);
	}
	$('form[name="' + formName + '"]').find(' #strlen').html(($("#contents").val().length +smsTailContens.length));
}

// 手机号码数量校验
function checkMaxNum(maxNum){
	var maxNums=parseInt(maxNum);
	if(maxNums>1000000){
		parent.$("#counts").text();
		parent.$("#effs").text();
		parent.$("#blacks").text();
		parent.$("#legers").text();
		parent.$("#sames").text();						   
		alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_5")+MAX_PHONE_NUM+getJsLocaleMessage("ydwx","ydwx_jtwxfs_6"));
		parent.$("#subSend").attr("disabled",false);
		parent.$("#qingkong").attr("disabled",false);
		parent.$("#detail_Info").dialog("close");
		return false;
	}
	return true;
}

//根据账户获取账户绑定的路由信息拆分规则
function setGtInfo(spUser)
{
	$.post("wx_send.htm",{method : "getSpGateConfig",spUser : spUser},
		function(infoStr){
			if(infoStr !="error" && infoStr.startWith("infos:"))
			{
				var infos = infoStr.replace("infos:","").split("&");
				$("#gt1").val(infos[0]);
				$("#gt2").val(infos[1]);
				$("#gt3").val(infos[2]);
				$("#gt4").val(infos[3]);
				contentLen = infos[4];
				baseContentLen = contentLen;
				$('#maxLen').html("/"+contentLen);
			}
			len($("#contents"));
		}
	);
}
//根据账户获取账户绑定的路由信息拆分规则
function getGtInfo()
{
	var spUser = $('#spUser').val();
	$.post("wx_send.htm",{method : "getSpGateConfig",spUser : spUser},
		function(infoStr){
			if(infoStr !="error" && infoStr.startWith("infos:"))
			{
				var infos = infoStr.replace("infos:","").split("&");
				$("#gt1").val(infos[0]);
				$("#gt2").val(infos[1]);
				$("#gt3").val(infos[2]);
				$("#gt4").val(infos[3]);
				contentLen = infos[4];
				baseContentLen = contentLen;
				$('#maxLen').html("/"+contentLen);
			}
			len($("#contents"));
		}
	);
}

// 验证上传文件格式
function checkFile_old(file) {
	var fileObj = $("#"+file);
	if(fileObj.val() != "") {
		var fileName = fileObj.val();
		var index =fileName.lastIndexOf(".");
		var fileType =fileName.substring(index + 1).toLowerCase();

		var trs= $("#vss").children().children();
		//判断文件重复
		var repeatFlag=false;
		var repeatCount = 0;
		$.each(trs,function(i,n)
		{
            var name=$($(n).children()[1]).html();
            if(name.indexOf(fileName)>-1)
            {
            	repeatCount++;
            }
	    }); 
	    if(repeatCount>1)
	    {
	    	alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_7"));
	    	fileObj.after(fileObj.clone().val(""));   
	    	fileObj.remove(); 
    		return false;
		}
	    
		if (fileType != "txt" && fileType != "zip" && fileType != "xls" && fileType != "xlsx" && fileType != "et") {
			alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_8"));
    		fileObj.after(fileObj.clone().val(""));   
    		fileObj.remove(); 
    		return false;
		} else {
			return true;
		}
	}
	else {
		alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_9"));
		return false;
	}
}



	// 重新加载
	function reloadPage()
	{
   	var lguserid=$('#lguserid').val();
    var lgcorpcode=$('#lgcorpcode').val();
		window.location.href='wx_send.htm?method=find&lguserid='+lguserid+'&lgcorpcode='+lgcorpcode;
	}
	
	//过滤文件名中的非法字符
	function checkForm(name){
	var patrn=/[`~!@#$%^&*()_+<>?:"{},.\/;'[\]]/im;
		if(patrn.test(name)){
  		alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_10"));
    	var file = $(":file");   
    	file.after(file.clone().val(""));   
    	file.remove(); 
   		return false; 
	} 
 	return true;
	} 
// 统计条数-短信拆分规则js版
function countTS(s) {
	
	var len ;
	var maxLen;
	var totalLen;
	var lastLen;
	var signLen;
	var count;
	for(var i=1;i<4;i=i+1)
	{
		count = 0;
		if(s>0)
		{
			var gtinfo = $("#gt"+i).val();
			if(gtinfo != "")
			{
				var gtinfos = gtinfo.split(",");
				
				maxLen = gtinfos[0];
				totalLen = gtinfos[1];
				lastLen = gtinfos[2];
				signLen = gtinfos[3];
				len = s*2;
				if (len <= (totalLen - signLen + 3)*2)
					count = 1;
				else
					count = 1 + Math.floor((len - lastLen * 2 + totalLen * 2 - 1) / (totalLen * 2));
			}
		}
		$("#ft"+i).text(count);
	}
}

// 统计条数-短信拆分规则js版(支持英文短信)
function countSMS(s, content) {
	var len ;
	var enLen;
	var reset;
	var maxLen;
	var totalLen;
	var lastLen;
	var signLen;
	var enmaxLen;
	var entotalLen;
	var enlastLen;
	var ensignLen;
	var gateprivilege;
	var ensinglelen;
	var count;
	var enMsgShortLen;
	var signLocation;
	var longSmsFirstLen;
	//短信内容是否为中文短信,false:英文;true:中文
	var isChinese = false;
	for(var i=1;i<5;i=i+1)
	{
		count = 0;
		if(s>0)
		{
			enLen = 0;
			enMsgShortLen = 0;
			reset = false;
			var gtinfo = $("#gt"+i).val();
			if(gtinfo != "")
			{
				var gtinfos = gtinfo.split(",");
				
				maxLen = gtinfos[0];
				totalLen = gtinfos[1];
				lastLen = gtinfos[2];
				signLen = gtinfos[3];
				enmaxLen = gtinfos[4];
				entotalLen = gtinfos[5];
				enlastLen = gtinfos[6];
				ensignLen = gtinfos[7];
				gateprivilege = gtinfos[8];
				ensinglelen = gtinfos[9];
				signLocation = gtinfos[10];
				//签名前置
				if(signLocation == 1)
				{
					longSmsFirstLen = entotalLen - ensignLen;
				}
				else
				{
					longSmsFirstLen = entotalLen;
				}
				//支持英文短信
				if(!isChinese && gateprivilege == 1){
					//字符ASCII码
					var charAscii;
					//是否中文短信
					for(var j=0;j<content.length;j++)
					{
						enLen += 1;
						enMsgShortLen += 1;
						charAscii = content.charAt(j).charCodeAt();
						if(charAscii > 127)
						{
							isChinese = true;
						}
						for(var k=0; k<special.length; k++)
						{
							if(special[k] == charAscii)
							{
								//长短信边界值
								if(enLen % longSmsFirstLen == 0)
								{
									//条数加2
									enLen += 2;
								}
								else
								{
									enLen += 1;
								}
								enMsgShortLen += 1;
								break;
							}
						}
						
						//英文短信长度超过最大短信长度
						if(enLen > contentLen)
						{
							$("#contents").val(content.substring(0,j));
							content = $("#contents").val();
							s = content.length;
							//重新计算条数
							reset = true;
							break;
						}
						if(isChinese)
						{
							break;
						}
					}
					//重新计算条数,重新遍历
					if(reset)
					{
						i = 0;
						continue;
					}
					//如果为短短信
					if(enMsgShortLen <= (ensinglelen - ensignLen))
					{
						enLen = enMsgShortLen;
					}
				}
				//短信内容为英文并且支持英文短信，使用英文拆分规则
				if(!isChinese && gateprivilege == 1){
					//国内通道英文短信,特殊字符按1个计算
					if(i != 4)
					{
						enLen = s;
					}
					//条数计算
					if (enLen <= (ensinglelen - ensignLen)){
						count = 1;
					}
					else{
						count = 1 + Math.floor((enLen - enlastLen +  parseInt(entotalLen) -1) / entotalLen);
					}
				}
				//中文短信
				else{
					//处理模板导入,短信内容条数计算
					if(isChinese && contentLen == 620)
					{
						contentLen = 270;
						if(s > contentLen)
						{
							$("#contents").val(content.substring(0,270));
							content = $("#contents").val();
							s = content.length;
							//重新计算条数
							i = 0;
							continue;
						}
					}
					len = s*2;
					if (len <= (totalLen - signLen + 3)*2)
						count = 1;
					else
						count = 1 + Math.floor((len - lastLen * 2 + totalLen * 2 - 1) / (totalLen * 2));
				}
			}
		}
		$("#ft"+i).text(count);
	}
	if(isChinese && contentLen == 620)
	{
		contentLen = 270;
	}
	else if(!isChinese && contentLen == 270 && baseContentLen == 620)
	{
		contentLen = 620;
	}
}

// 失去焦点时判断
function eblur(ob) {
	var $parent = ob.parent();
	$parent.find(".add_error").remove();
	if (ob.is("#contents")) {
		ob.val($.trim(ob.val()));
//		var content = $("#contents").val();
		//短信贴尾内容
//		var huiche = content.length - content.replaceAll("\n", "").length;
//		if (content.length + huiche > contentLen) {
//			$("#contents").val(content.substring(0, contentLen - huiche));
//		}
		if($("#tempSelect").val()=="")
		{
			$("#inputContent").val($("#contents").val());
		}
		len(ob);
		if ($.trim(ob.val()) == "") {
			document.forms[formName].isOk.value = 1;
		} else if (ob.val().length > contentLen) {
			document.forms[formName].isOk.value = 1;
		} else {
			document.forms[formName].isOk.value = 2;
		}
	}
}
// 手机号码输入控制
function limit() {
	// 只允许输入数字和';'
	var isIE = false;
	var isFF = false;
	var isSa = false;
	if ((navigator.userAgent.indexOf("MSIE") > 0)
			&& (parseInt(navigator.appVersion) >= 4))
		isIE = true;
	if (navigator.userAgent.indexOf("Firefox") > 0)
		isFF = true;
	if (navigator.userAgent.indexOf("Safari") > 0)
		isSa = true;
	$('#mobileUrl').keypress(
		function(e) {
			var iKeyCode = window.event ? e.keyCode : e.which;
			var vv = !(((iKeyCode >= 48) && (iKeyCode <= 57))
					|| (iKeyCode == 44) || (iKeyCode == 13)
					|| (iKeyCode == 46) || (iKeyCode == 45)
					|| (iKeyCode == 37) || (iKeyCode == 39) || (iKeyCode == 8));
			if (vv) {
				if (isIE) {
					event.returnValue = false;
				} else {
					e.preventDefault();
				}
			}
		}
	);
	// 控制不能由输入法输入其他字符
	$('#mobileUrl').keyup(function() {
		var str = $('#mobileUrl').val();
		// 只能输入0-9或者英文标点","符号、回车换行
		var reg = /[^0-9,\r\n]+/g;
		str = str.replace(reg, "");
		$('#mobileUrl').val(str);
	});
}

// 短信内容键盘事件监听统计字数
function synlen() {
	$("form").find("textarea[name='msg']").keyup(function() {
//		var content = $(this).val();
//		var huiche = content.length - content.replaceAll("\n", "").length;
//		if (content.length + huiche > contentLen) {
//			$(this).val(content.substring(0, contentLen - huiche));
//		}
		len($(this));
	});
}

// 设置当前显示的表单名称
var formName = "";
function setFormName(name) {
	formName = name;
	$('#formName').val(formName);
}

$(document).ready(
function() {
	$("#upNumFile").click(function() {
		$(this).parent().next("#upNum").show();
	});
	var curMsg = "";
	$("a[id='addModel'],a[id='addModel2'],a[id='addModel3']")
			.click(
	function() {
		var dsflag = 0;
		if ($(this).attr("id") == "addModel2") {
			dsflag = $(
					"form[name='" + formName
							+ "']").find(
					"#sendType").val() - 1;
		}
		if ($(this).attr("id") == "addModel3") {
			dsflag = 1;
		}
		var mod = $(this).parent().next(
				"#model");
		var tmpSel = mod.find("#tempSelect");
		var lguserid = $("#lguserid").val();
		if ((tmpSel.find("> option").length == 1 && tmpSel
				.val() == "")
				|| $(this).attr("id") == "addModel2") {
			tmpSel.empty();
			tmpSel.load("tem_smsTemplate.htm", {
				method : "getTmAsOption",
				lguserid:lguserid,
				dsflag : dsflag
			}, function() {
				});
		} else {
			tmpSel.find("> option").eq(0).attr(
					"selected", "selected");
		}
		mod.toggle();
		var msgob = $(
				"form[name='" + formName + "']")
				.find("textarea[name='msg']");
		if (mod.is(":visible")) {
			tmpSel.css("display", "block");
			msgob.val(curMsg);
			curMsg = msgob.val();
			msgob.attr("readonly", "readonly");
			len(msgob);
		} else {
			tmpSel.css("display", "none");
			msgob.val(curMsg);
			curMsg = msgob.val();
			msgob.attr("readonly", "");
			len(msgob);
		}
	});
	$("#delUp").click(function() {
		if (navigator.appName == "Netscape") {
			$("#numFile").attr("value", "");
		} else {
			document.getElementById("numFile").select();
			document.selection.clear();
		}
		$("#upNum").toggle();
	});
	 var spUser = $("#spUser").val();
	getGtInfo();
	$("#spUser").bind("change", function(){getGtInfo();});
	synlen();
	limit();
	setFormName($("#formName").val());
	$("form[name='" + formName + "']").find(
			"input[name='reInput']").click(function() {
		reInputs($(this));
	});
	// 点击创建或存草稿按钮时
	$("input[id='subSend'],input[id='creSend']").click(
			function() {
				$("#phoneStr").val($("#phoneStr1").val()+$("#phoneStr2").val()+$("#inputphone").val()+",");
				//判断时间----------------------------------------
				var sendTime = $('#timerTime').val();
				var serverTime ;
				$.post("common.htm", {
					method : "getServerTime"
				}, function(msg) {
					serverTime = msg;
					var date1 = new Date(Date.parse(sendTime.replaceAll("-","/")));
					var date2 = new Date(Date.parse(serverTime.replaceAll("-","/")));
                    date1.setMinutes(date1.getMinutes() - 1, 0, 0);
					if (date1 <= date2) {
						/*预发送时间小于服务器当前时间！请合理预定发送时间[EXFV011]*/
						alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_11"));
						$("#timerTime").val("");
						return;
					}
					else
					{
						$("#error").val("0");
						if ($(this).attr("id") == "subSend") {
							$("form[name='" + formName + "']").find("input[name='subState']").val(2);
						} else {
							$("form[name='" + formName + "']").find("input[name='subState']").val(1);
						}
						
						var formObj = document.forms[formName];
						if(eval($("#strlen").text())>contentLen){
							/*字数超过  个，请重新输入*/
							alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_12")+contentLen+getJsLocaleMessage("ydwx","ydwx_jtwxfs_13"));
							return;
						}
						var netid = $("#netid").val();
						if(netid==null||netid=="" || netid =="undefined")
						{
							/*请选择网讯！*/
							alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_14"));
							return;
						}
						if (formObj.spUser.value == "") {
							/*没有可供发送的SP账号！*/
							alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_15"));
							return;
						}
						// 判断主题是否填写
						$("form[name='" + formName + "']").find("input[name='taskname']").trigger("blur");
						var $texta = $("form[name='" + formName + "']").find("textarea[name='msg']");
						// 简单发送时
						var sendType = $("form[name='" + formName + "']").find("#sendType").val();
						if (sendType != 3) {
							$texta.trigger("blur");
							$("#dtMsg").val($texta.val());
						}
						if (formObj.isOk.value == 1
								&& sendType != 3) {
							/*请填写正确的短信内容！*/
							alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_16"));
							return;
						}
						$("#busCode1").val($("#busCode").val());
						$("#spUser1").val($("#spUser").val());
						checkWords();
					}
				});
			});

	//查看
	$("input[id='shows']").click(
			function(){
				var formObj = document.forms["form2"];
				if (formObj.spUser.value == "") {
					/*没有可供发送的SP账号！*/
					alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_15"));
					return;
				}
				if(re()){
					$("#form2").attr("action", "wx_send.htm?method=adds");
					$("#form2").submit();
				}
				
			});
	
    $('#toDraft').click(function(){
        //暂存草稿前判断发送内容以及发送号码是否为空
        var msg = $.trim($('#contents').val());
        var len = $('#vss tr:gt(0)').length;
        if(!msg && !len){
        	/*发送内容和发送号码不能全为空！*/
            alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_17"));
            return false;
        }
        /*不作为短信内容发送*/
        if($("#taskname").val()==getJsLocaleMessage("ydwx","ydwx_jtwxfs_18"))
        {
            $("#taskname").val("");
        }
        //提交之前置灰暂存草稿按钮
        $(this).attr('disabled',true);
        $("#phoneStr").val($("#phoneStr1").val()+$("#phoneStr2").val()+$("#inputphone").val()+",");
        $("form[name='form2']").attr('action', "wx_send.htm?method=toDraft&timee="+new Date().getTime());
        $("form[name='form2']").submit();
    });
    
    //页面初始化的时候，需要加载贴尾
    setTailInfo();
    //选择业务类型，SP账号都要查询
    $("#spUser,#busCode").bind("change", function(){
    	setTailInfo();
    	len($('#contents'));
    });
    
	});
function checkTxt() {
	var mobile = $("#mobileUrl").val();
	if (mobile != "") {
		$.post("mms_sendMMS.htm?method=checkMobile", {
			receiver : mobile
		}, function(result) {
			if (result != null && result != "") {
				alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_19") + result);
				return false;
			} else {
				checkWords();
			}
		});
	} else {
		alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_20"));
	}
}
// 验证短信内容是否包含关键字
function checkWords() {
	
	$("#subSend").attr("disabled",true);
	$("#qingkong").attr("disabled",true);
	var msg = $("form[name='" + formName + "']").find("textarea[name='msg']").val();
    msg += $('#smsTailContens').text();
	$.ajax({
		url: "wx_send.htm", 
		type : "POST",
		data: {method : "checkBadWord1",tmMsg : msg,corpCode : $("#lgcorpcode").val()},
		success: function(message) {
			if(message.indexOf("@")==-1){
				/*异步请求时，响应超时，请刷新页面或稍后重试[EXFV005]*/
				alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_21"));
				$("#subSend").attr("disabled","");
				$("#qingkong").attr("disabled","");
				return;
			}
			message=message.substr(message.indexOf("@")+1);
			if (message != "" && message !="error") {
				/*发送内容包含如下违禁词组：请检查后重新输入[EXFV006] */
				alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_22") + message + getJsLocaleMessage("ydwx","ydwx_jtwxfs_23"));
				$("#subSend").attr("disabled","");
				$("#qingkong").attr("disabled","");
			} else if (message == "error") {
				/*预览操作失败[EXFV007]*/
				alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_24"));
				$("#subSend").attr("disabled","");
				$("#qingkong").attr("disabled","");
			} else {
				checkTimer();
			}
		},
		error:function(xrq,textStatus,errorThrown){
			/*网络或服务器无法连接，请稍后重试！[EXFV008]*/
			alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_25"));
			$("#subSend").attr("disabled","");
			$("#qingkong").attr("disabled","");
			window.location.href='wx_send.htm?method=find&lguserid='+$('#lguserid').val()+'&lgcorpcode='+$('#lgcorpcode').val();
		}
	});
}



// 验证是否定时
function checkTimer() {
	var $timer = $("form[name='" + formName + "']").find("input:checked[name='timerStatus']");
	var $timerTime = $("form[name='" + formName + "']").find("input[name='timerTime']");
	if ($timer.val() == 1 && $timerTime.val() == "") {
		/*请填写定时时间！*/
		alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_26"));
		$("#subSend").attr("disabled","");
		$("#qingkong").attr("disabled","");
	} else {
		if($("#vss").find(" tr").length<2)
        {
        	/*请添加手机号码或上传文件！*/
        	alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_27"));
        	$("#subSend").attr("disabled","");
    		$("#qingkong").attr("disabled","");
        }else
        {
	        $("#probar").dialog({
			modal:true,
			/*加载进度..*/
			title:getJsLocaleMessage("ydwx","ydwx_jtwxfs_28"), 
			height:70,
			resizable :false,
			closeOnEscape: false,
			width:300,
			open: function(){
			$("#probar").css("height","50px");	
			$(".ui-dialog-titlebar").hide();
			errorNum=0;
			dd = window.setInterval("fresh()",3000);
	          }
			});
	        var netid = $("#netid").val();
	        var taskId = $("#taskId").val();
	        $("form[name='form2']").attr("action","wx_send.htm?method=upNumber&lguserid="+$("#lguserid").val()+"&netid="+netid+"&taskId="+taskId);
			$("form[name='form2']").submit();
        }
	}
}
// 刷新页面
function fresh()
{
	$.ajax({
		url:"LoadingServlet.htm",
		type:"post",
		dataType:"script",
		success:function(result){
			if( result !="true")
			{
				if(errorNum==0)
				{
					errorNum==1;
					/*网络或服务器无法连接，请稍后重试！[EXFV009]*/
					alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_29"));
					window.clearInterval(dd);
					window.location.href='wx_send.htm?method=find&lguserid='+$('#lguserid').val()+'&lgcorpcode='+$('#lgcorpcode').val();
				}
			}
		},
		error:function(xrq,textStatus,errorThrown){
			
			if(errorNum==0)
			{
				errorNum=1;
				/*网络或服务器无法连接，请稍后重试！[EXFV010]*/
				alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_30"));
			}
			window.clearInterval(dd);
			window.location.href='wx_send.htm?method=find&lguserid='+$('#lguserid').val()+'&lgcorpcode='+$('#lgcorpcode').val();
		}
	});
}

// 获取短信模板内容
function getTempMsg(tmpOb) {
	var msgob = $("form[name='" + formName + "']").find("textarea[name='msg']");
	if (tmpOb.val() != "") {
		$.ajax({
			url:"wx_send.htm",
			data:{method : "getTmMsg1",tmId : tmpOb.val()},
			type:"post",
			success:function(result){
				if(result.indexOf("@")==-1){
					window.location.href='wx_send.htm?method=find&lguserid='+$('#lguserid').val()+'&lgcorpcode='+$('#lgcorpcode').val();
					return;
				}
				result=result.substr(result.indexOf("@")+1);
				if(result=="error")
				{
					alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_31"));
					return;
				}
				msgob.val(result);
				msgob.attr("readonly","readonly");
				msgob.css("background","#E8E8E8");
				msgob.bind("focus",function(){
					this.blur();
				});
				len(msgob);
			},
			error:function(xrq,textStatus,errorThrown){
				alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_30"));
				window.location.href='wx_send.htm?method=find&lguserid='+$('#lguserid').val()+'&lgcorpcode='+$('#lgcorpcode').val();
			}
		});
	} else {
		msgob.val($("#inputContent").val());
		msgob.attr("readonly","");
		msgob.focus();
		msgob.css("background","#FFFFFF");
		msgob.unbind("focus");
		len(msgob);
	}
}

// 获取短信模板内容
function getSql(sqlId) {
	var $sqlContent = $("form[name='" + formName + "']").find(
			"textarea[name='sql']");
	if (sqlId != "") {
		$.post("tem_smsTemplate.htm", {
			method : "getTmMsg",
			tmId : sqlId
		}, function(msg) {
			$sqlContent.val(msg);
		});
	} else {
		$sqlContent.val("");
	}
}

function openTemp(r) {
	var path = $("#cpath").val();
	window.open(path + "/fileUpload/fileDownload/" + r, "_blank");
}
//当前时间与服务器时间对比
function checkServerTime(){
	
	//------------------------------------guodw
	var sendTime = $('#timerTime').val();
	var serverTime ;
	$.post("common.htm", {
		method : "getServerTime"
	}, function(msg) {
		serverTime = msg;
		var date1 = new Date(Date.parse(sendTime.replaceAll("-","/")));
		var date2 = new Date(Date.parse(serverTime.replaceAll("-","/")));
        date1.setMinutes(date1.getMinutes() - 1, 0, 0);
		if (date1 <= date2) {
			alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_11"));
			$("#timerTime").val("");
			return;
		}
	});
	
	//----------------------------------------
}

// 查看号码文件是否存在
function checkFileIsVisible(url, src) {
	$.post(src + "/mms_mmsSendBox.htm?method=checkFiles", {
		url : url
	}, function(result) {
		if (result == "true") {
			window.showModalDialog(src + "/" + url);
		} else if (result == "false")
			alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_32"));
		else
			alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_33"));
	});
}

//提交前先判断是否有号码存在列表中
function re(){
	//提交前先遍历表格，看看是否有手机号码或者文件存在
	//取出表格中的数据
	 var hen=new Array();
     //合法
        　var trs= $("#vss").children().children();           
        $.each(trs,function(i,n){
	       var tds=$(n).children();
	       $.each(tds,function(j,d){
	             if(i!=0&&j==1)
	              {
	                 var ps=$(d).text();
	                 hen[i]=ps;
	              }
	        }); 
	     });
      if(hen.length>0){
    	  return true;
      }else{
    	  alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_34"));
    	  return false;
      }
}

// 重置
function resets(){
	var ie = (navigator.appVersion.indexOf("MSIE")!=-1);//IE         
	if(ie){   
	            $("#numFile").select();   
	             document.execCommand("delete");   
	       }else{   
	              $("#numFile").val("");    
	      }  
	$('form[name="' + formName + '"]').find(' #ft').html((0));
		$("#taskname").attr("value","");
		$("#contents").attr("value","");
		$("#sendNow").attr("checked","checked");
	//1清除列表中的数据
	var trs= $("#vss").children().children();           
        $.each(trs,function(i,n){
	      if(i>0){
	        $(n).remove();
	       }
	 });
     $('form[name="' + formName + '"]').find(' #strlen').html((0));
	 //清队查模板
	 $("#model").css("display","none");
	 $('#contents').attr('readonly',false);
	// $("#contents").attr("disabled","");
	  $("#time2").css("display","none");
}


//预览窗口
function preSend(data)
{
	window.clearInterval(dd);
	$("#error").val("1");
	var SendReq=$("#SendReq").val();
	var cpath = $("#cpath").val();
	$("#probar").dialog("close");
	$(".ui-dialog-titlebar").show();

	if(data.indexOf("empex")===0){
        alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_35")+data.substr(5));
        $("#subSend").attr("disabled",true);
        $("#qingkong").attr("disabled",false);
        return;
	}

	if(data=="error")
	{
		/*预览异常，操作失败。*/
		alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_24"));
		$("#subSend").attr("disabled",true);
		$("#qingkong").attr("disabled",false);
		return;
	}
	else if(data=="overstep")
	{
		/*文件内有效号码大于  万，系统不支持，请重新选择发送文件！[EXFV015]*/
		alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_36")+MAX_PHONE_NUM+getJsLocaleMessage("ydwx","ydwx_jtwxfs_37"));
		$("#subSend").attr("disabled",true);
		$("#qingkong").attr("disabled",false);
		return;
	}
	else if(data=="overSize")
	{
		/*上传文件过大，单次上传TXT文件或EXCEL文件最大支持  兆，请重新选择发送文件！[EXFV018]*/
		alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_38")+MAX_SIZE+getJsLocaleMessage("ydwx","ydwx_jtwxfs_39")+ZIP_SIZE+getJsLocaleMessage("ydwx","ydwx_jtwxfs_40"));
		$("#subSend").attr("disabled",false);
		$("#qingkong").attr("disabled",false);
		return;
	}
	$("#content").empty();
	$("#preStr").val(data);
	$("#content").append("<thead><tr align='center'><th>"+getJsLocaleMessage("ydwx","ydwx_jtwxfs_41")+"</th>" +
			"<th><center><div style='width:89px'>"+getJsLocaleMessage("ydwx","ydwx_jtwxfs_42")+"</div></center></th>" +
			"<th>"+getJsLocaleMessage("ydwx","ydwx_jtwxfs_43")+"</th></tr></thead>" );
	var msgContent=$("form[name='" + formName + "']").find("textarea[name='msg']").val();
	if(data=="noPhone")
	{
		/*文件内没有包含号码！[EXFV016]*/
		alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_44"));
		$("#subSend").attr("disabled",false);
		$("#qingkong").attr("disabled",false);
	}else
	{
		hideSelect();
		var array=data.split('&');
		//var nums=array[0].split(';');
		$("#counts").text(array[1]);
	    $("#effs").text(array[2]);
	    $("#blacks").text(array[5]);
	    $("#legers").text(array[3]);
	    $("#sames").text(array[4]);
	    $("#yct").text(array[7]);
	    $("#ct").text(array[8]);
	    $("#badurl").attr("value",array[9]);
	    var spye = array[11];
	    checkspye(spye);
	    
    	// ----增加SP账号检查---
    	var feeFlag=array[13];
    	//为了先显示短信等，显示SP账号，显示顺序
    	var isshow=true;
    	//如果余额<发送条数 
		if(eval(array[8])<eval(array[7]))
		{
			isshow=false;
	    }
    	//后付费情况
		 if(feeFlag=="2"){
    		$("#shospfee").text("");
    	}
		 //预付费处理
    	else if(feeFlag=="1")
    	{
			$("#shospfee").text("");
			/*SP账号余额：*/
			$("#shospfee").html(getJsLocaleMessage("ydwx","ydwx_jtwxfs_45")+"<span id='spfee'></span>");
			var spAccount=array[12];
			var resultCount=eval(spAccount-array[7]);
		 	checkSpAccount(isshow,resultCount,spAccount);
		 }else if(feeFlag=="-1"&&isshow){
			 $("#shospfee").text("");
			 /*SP账号不能为空*/
			 $("#messages2 font").text(getJsLocaleMessage("ydwx","ydwx_jtwxfs_46"));
			 $("#btsend").attr("disabled","disabled");
		 }else if(feeFlag=="-2"&&isshow){
			 $("#shospfee").text("");
			 /*获取不到SP账号信息*/
			 $("#messages2 font").text(getJsLocaleMessage("ydwx","ydwx_jtwxfs_47"));
			 $("#btsend").attr("disabled","disabled");
		 }else if(feeFlag=="-3"&&isshow){
			 $("#shospfee").text("");
			 /*检查SP账号余额异常！*/
			 $("#messages2 font").text(getJsLocaleMessage("ydwx","ydwx_jtwxfs_48"));
			 $("#btsend").attr("disabled","disabled");
		 }
			 
			  //----增加SP账号检查 end---
			 
	    var wuxiaonum = parseInt(array[4])+parseInt(array[3])+parseInt(array[5]);
	    $("#alleff").text(wuxiaonum);
	    if(wuxiaonum == 0)
	    {
	    	$("#preinfonum").attr("style","display:none");
	    }else{
	    	$("#preinfonum").attr("style","display:block");
	    }
	    $("#isCharg").attr("value",array[10]);
	    if(array[2] == 0)
	    {
	    	/*没有有效的号码*/
	    	$("#content").append("<tbody><tr><td colspan='3'  align='center'>"+getJsLocaleMessage("ydwx","ydwx_jtwxfs_49")+"</td></tr></tbody>");
	    }else
	    {
	    	/*
			for (var x = 0; x < nums.length; x=x+1) 
			{
				$("#content").append("<tr align ='center'><td>"+(x+1)+"</td><td>"+nums[x]+"</td>" +"<td><xmp style='word-break: break-all;white-space:normal; '>"
						+msgContent+"</xmp></td></tr>");
			}
			*/
			
			//防止预览发出多次异步请求
	    	if(SendReq=="0")
	    	{
	    		 $("#SendReq").val("1");
				    var sendType=3;
				    $("#content").load(
				    	"wx_send.htm?method=readSmsContent",
				    	{url:array[0],sendType:sendType},
				    	function(){
				    		$("#detail_Info").css("display","block");
				    		$("#detail_Info").dialog("open");
				    		deleteleftline1();	
				    });
				    $(".ui-dialog-titlebar-close").hide();
	    	}
	    }
	    $("#detail_Info").css("display","block");
	    
	    loadWxTemp();
	    
	    $("#detail_Info").dialog("open");
	    deleteleftline();
	    $(".ui-dialog-titlebar-close").hide();
	}
}
	// 提示SP账号余额相关信息
function checkSpAccount(isshow,resultCount,spAccount)
{
	var spUser=$("#spUser").val();
	var altstr = "";
	if(resultCount < 0&&isshow)
	{
		/*账号余额不足，不允许发送，请联系管理员充值.*/
		altstr = spUser+getJsLocaleMessage("ydwx","ydwx_jtwxfs_50");
		$("#messages2 font").text(altstr);
		$("#btsend").attr("disabled","disabled");	 
	}

	$("#spfee").text(spAccount);

}

// 提示余额相关信息
function checkspye(yeresult)
{
	var altstr = "";
	if(yeresult.indexOf("lessgwfee") == 0)
	{
		/*运营商余额不足，当前余额：*/
		altstr = getJsLocaleMessage("ydwx","ydwx_jtwxfs_51")+yeresult.split("=")[1];
		 $("#btsend").attr("disabled","disabled");
	}else if(yeresult=="feefail" || yeresult=="nogwfee")
	{
		/*获取运营商余额失败，不允许发送*/
		altstr = getJsLocaleMessage("ydwx","ydwx_jtwxfs_52");
		 $("#btsend").attr("disabled","disabled");
	}
	$("#messages2 font").text(altstr);
}

//加载网讯相关信息
function loadWxTemp(){	
	var netId = $('#netid').val();
    $.post('wx_manger.htm?method=showNetById',{netId:netId},function(data){
       data=eval("("+data+")");
       listpage=data;
       $("#selDh").children().remove();
       for(var i=0;i<listpage.length;i++)	{   
           $("<option>").val(listpage[i].id).html(listpage[i].name).appendTo($("#selDh"));
      }
        // 此处为显示错误页面，避免进入登录页面
        if(listpage[0].content=="notexists"){
            $("#nm_preview_common").attr("src","ydwx/wap/404.jsp");
        }else{
            $("#nm_preview_common").attr("src","file/wx/PAGE/wx_"+listpage[0].id+".jsp");
        }
   });
}
//弹出隐藏层
function selectWorkers(){
	$("#yugong").css("display","block");
    $("#yugong").dialog("open");
}

// 检查错误信息
function checkError()
{
	if($("#error").val()=="0")
	{
		window.clearInterval(dd);
		$("#probar").dialog("close");
		/*服务器繁忙！[EXFV017]*/
        alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_53"));
		$("#subSend").attr("disabled",false);
        $("#qingkong").attr("disabled",false);
	}
}

//点击选择短信内容
function chooseTemp(skinType) {
	var pageSize = 10;
	if(skinType === "4.0"){
        pageSize = 5;
	}
	var tpath = $("#cpath").val();
	var frameSrc = $("#tempFrame").attr("src");
	var lguserid = $("#lguserid").val();
	var lgcorpcode = $("#lgcorpcode").val();
	frameSrc = tpath+"/wx_send.htm?method=getLfTemplateBySms&dsflag=0&lguserid="
		+lguserid+"&lgcorpcode="+lgcorpcode+"&timee="+new Date().getTime()+"&pageSize=" + pageSize;
	$("#contentFrame").attr("src",frameSrc);

    $("#tempDiv").dialog({
        autoOpen: false,
        height:skinType === "4.0"?568:460,
        width: "zh_HK"=== empLangName?700:630,
        resizable:false,
        modal: true,
        open:function(){},
        close:function(){
            $("#addSmsTmpDiv").attr("src","");
        }
    });

	$("#tempDiv").dialog("open");
	changeDialogTitleCss('tempDiv');
}

//隐藏层
function closeDialog(){
	$("#tmplDiv").dialog("close");
}

//-- 取消网讯模板--%>
function tempNo()
{
	$("#netid").val("");
	$("#templatepre").empty();
	$("#templatepre").css("display","none");
}
//临时层隐藏
function tempCancel()
{
	$("#tempDiv").dialog("close");
}
//取消短信内容选择
function tempNoShow()
{
	var msgob = $("form[name='" + formName + "']").find("textarea[name='msg']");
	initContents();
	len(msgob);
	$("#depSign").attr("checked",false);
	$("#nameSign").attr("checked",false);
	$("#qtem").hide();
	$("#ctem").show();
	msgob.css("background-color","#ffffff");
}
// 删除导入文件
function ddll(idd){
	idd=idd+"";
	if(idd.indexOf("tr")==0){
		if(confirm( getJsLocaleMessage("ydwx","ydwx_jtwxfs_54")))
		{ 
			$("#"+idd).remove();
			var filename = idd.replace("tr","numFile");
			var trs = $("#allfilename").html();
		    trs = trs.replace($("#"+filename).val()+";","");
			$("#"+filename).remove();
		    $("#allfilename").html(trs);
		}	
	}else{
		if(confirm( getJsLocaleMessage("ydwx","ydwx_jtwxfs_54")))
		{
			
			$("#"+idd+"").remove();
			//由于增加了前缀去掉前缀
			if(idd.indexOf("phone_")>-1){
				idd=idd.replace("phone_","");
				idd="+"+idd;
			}else if(idd.indexOf("phone")>-1){
				idd=idd.replace("phone","");
			}

			
			$("#phoneStr2").val($("#phoneStr2").val().replace(idd+",",""));
			}
	}
}

// 验证上传文件格式
function checkFile(name) {
	var fileObj = $("form[name='" + formName + "']").find("input[name='"+name+"']");
	if(fileObj.val() != "") {
		var fileName = fileObj.val();
		var index =fileName.lastIndexOf(".");
		var fileType =fileName.substring(index + 1).toLowerCase(); 
		var trs = $("#allfilename").html();

        if(trs.indexOf(fileName+';')>-1)
        {
        	alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_7"));
	    	fileObj.after(fileObj.clone().val(""));   
	    	fileObj.remove(); 
    		return false;
        }
	    
		if (fileType != "txt" && fileType != "zip"&& fileType != "rar" && fileType != "xls" && fileType != "xlsx" && fileType != "et") {
			alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_8"));
    		fileObj.after(fileObj.clone().val(""));   
    		fileObj.remove(); 
    		return false;
		} else {
			return true;
		}
	}
	else {
		alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_9"));
		return false;
	}
}

//发送	
function btsend()
{
	subcount = subcount+1;
	if(subcount > 1)
	{
		alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_55s"));
		$("form[name='" + formName + "']").attr("action","");
		return;
	}
	$("#btsend").attr("disabled",true);
	$("#btcancel").attr("disabled",true);
	if($("#taskname").val()==getJsLocaleMessage("ydwx","ydwx_jtwxfs_56"))
	{
		$("#taskname").val("");
	}
		var sendTime = $('#timerTime').val();
	var serverTime ;
	$.post("common.htm", {
		method : "getServerTime"
	}, function(msg) {
		serverTime = msg;
		var date1 = new Date(Date.parse(sendTime.replaceAll("-","/")));
		var date2 = new Date(Date.parse(serverTime.replaceAll("-","/")));
        date1.setMinutes(date1.getMinutes() - 1, 0, 0);
		if (date1 <= date2) {
			alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_11"));
			$("#timerTime").val("");
			showSelect();
			$("#subSend").attr("disabled","");
			$("#qingkong").attr("disabled","");
			$("#detail_Info").dialog("close");
			$("#btsend").attr("disabled","");
			$("#btcancel").attr("disabled","");
			return;
		}
		else
		{
			if($("#effs").text()=="0")
			{
				alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_57"));
				$("#btsend").attr("disabled","");
				$("#btcancel").attr("disabled","");
			}else
			{
				$("#btsend").attr("disabled",true);
				$("#btcancel").attr("disabled",true);
				$("form[name='" + formName + "']").attr("target","_self");
				$("form[name='" + formName + "']").attr("action","wx_send.htm?method=add");
				if($("form[name='" + formName + "']").attr("encoding"))
				{
					$("form[name='" + formName + "']").attr("encoding","application/x-www-form-urlencoded");
				}else
				{
					$("form[name='" + formName + "']").attr("enctype","application/x-www-form-urlencoded");
				}
				$("form[name='" + formName + "']").submit();
			}
		}
	});
}

//********************* send主页面上的脚本*************************
//机构签名
function setDepSign(signObject)
{

	var contents = $("#contents").val();
	if(signObject.checked)
	{
		if($("#nameSign").attr("checked"))
		{
			var str = contents.substring(contents.indexOf("]")+1);
			$("#contents").val("["+depSign+nameSign+"]"+str);
		}else
		{
			$("#contents").val("["+depSign+"]"+contents);
		}
	}
	else
	{
		var str = contents.substring(contents.indexOf("]")+1);
		if($("#nameSign").attr("checked"))
		{
			$("#contents").val("["+nameSign+"]"+str);
		}else
		{
			$("#contents").val(str);
		}
	}
	eblur($("#contents"));
}
//姓名签名
function setNameSign(signObject)
{
	var contents = $("#contents").val();
	if(signObject.checked)
	{
		var str = contents.substring(contents.indexOf("]")+1);
		if($("#depSign").attr("checked"))
		{
			$("#contents").val("["+depSign+nameSign+"]"+str);
		}else
		{
			$("#contents").val("["+nameSign+"]"+str);
		}
	}
	else
	{
		var str = contents.substring(contents.indexOf("]")+1);
		if($("#depSign").attr("checked"))
		{
			$("#contents").val("["+depSign+"]"+str);
		}else
		{
			$("#contents").val(str);
		}
	}
	eblur($("#contents"));
}

//选择人员界面的确定按钮
function doOk() {
	var skinType = $("#skinType").val();
	if("4.0" === skinType){
		// 这里处理是否 右边选择的记录$("#right option:selected").size()
        var optionSize = $(window.frames['flowFrame'].document).find("#right option").size();
        //员工机构id集合
        $("#empDepIds").val($(window.frames['flowFrame'].document).find("#empDepIdsStrs").val());
        //客户机构id集合
        $("#cliDepIds").val($(window.frames['flowFrame'].document).find("#cliDepIdsStrs").val());
        //群组id集合
        $("#groupIds").val($(window.frames['flowFrame'].document).find("#groupIdsStrs").val());
        //员工id集合
        $("#empIds").val($(window.frames['flowFrame'].document).find("#employeeIds").val());
        //客户id集合
        $("#cliIds").val($(window.frames['flowFrame'].document).find("#clientIds").val());
        //自建人员id集合
        $("#malIds").val($(window.frames['flowFrame'].document).find("#malistIds").val());
        //选择用户对象号码串
        $("#phoneStr1").val($(window.frames['flowFrame'].document).find("#moblieStrs").val());
        //右边选择的option 用于数据回显
        $("#rightSelectedUserOption").val($(window.frames['flowFrame'].document).find("#right").html());
        //总人数
        var manCount = $(window.frames['flowFrame'].document).find("#manCount").html()+"";
        //清空内容
        $("#ygtxl").remove();
        if (optionSize > 0) {
            $("#vss").append("<tr  class='div_bd' id='ygtxl' style='background-color:#ffffff'><td style='border-left:0;border-right:0' align='center' name='ConR'  valign='middle'>"+getJsLocaleMessage("ydwx","ydwx_jtwxfs_58")+"</td><td style='border-left:0;border-right:0'  align='center' valign='middle' >"+
                "<a onclick='showInfo();' style='cursor:pointer'><font style='color:#0e5ad1'>"+getJsLocaleMessage("ydwx","ydwx_jtwxfs_59")+"(<label style='color:#0e5ad1' id='choiceNum'>"+manCount+"</label>"+getJsLocaleMessage("ydwx","ydwx_jtwxfs_60")+")</font> </a></td><td  style='border-left:0;border-right:0' align='center' valign='middle' height='20px'>"+
                "<a  onclick='delAddr()'><img border='0' src='"+commonPath+"/common/widget/zTreeStyle/img/delete.gif' style='cursor:pointer'/></a></td></tr>");
        }else{
            //如果未选择对象,则清空一栏
            clearUser();
        }
        $("#infoDiv").dialog("close");
	}else {
        //<%-- 这里处理是否 右边选择的记录$("#right option:selected").size()--%>
        var optionSize = $(window.frames['flowFrame'].document).find("#right option").size();
        //<%--代表的是员工机构IDS --%>
        var empDepIdsStrs = $(window.frames['flowFrame'].document).find("#empDepIdsStrs").val();
        $("#empDepIds").val(empDepIdsStrs);
        //<%--代表的是客户机构IDS --%>
        var cliDepIdsStrs = $(window.frames['flowFrame'].document).find("#cliDepIdsStrs").val();
        $("#cliDepIds").val(cliDepIdsStrs);
        //<%--代表的是群组IDS --%>
        var groupIdsStrs = $(window.frames['flowFrame'].document).find("#groupIdsStrs").val();
        $("#groupIds").val(groupIdsStrs);
        var groupclientStr = $(window.frames['flowFrame'].document).find("#groupClientStr").val();
        $("#groupClient").val(groupclientStr);

        //<%--代表的是员工IDS --%>
        //$("#empIds").val($(window.frames['flowFrame'].document).find("#employeeIds").val());
        //<%--代表的是客户IDS--%>
        //$("#cliIds").val($(window.frames['flowFrame'].document).find("#clientIds").val());
        //<%--代表的是外部人员IDS --%>
        //$("#malIds").val($(window.frames['flowFrame'].document).find("#malistIds").val());
        //<%--选择用户对象号码串 --%>
        //$("#userMoblieStr").val($(window.frames['flowFrame'].document).find("#moblieStrs").val());
        $("#phoneStr1").val("");
        var eIds = "";
        var names = "";
        var mobile="";
        if ($(window.frames['flowFrame'].document).find("#right option").size() < 1) {
            $("#empDepIds").val("");
            $("#cliDepIds").val("");
            $("#groupIds").val("");
            $("#phoneStr1").val("");
            $("#ygtxl").remove();
            $(window.frames['flowFrame'].document).find("#right").empty();
            $(window.frames['flowFrame'].document).find("#getChooseMan").empty();
            $(window.frames['flowFrame'].document).find("#rightSelectTempAll").empty();
            //改变弹出框状态为1
            $("#hidIsDoOk").val("1");
            $("#infoDiv").dialog("close");
            return;
        }
        $(window.frames['flowFrame'].document).find("#right option").each(function() {
            eIds = $(this).val();
            names = $(this).text();
            mobile =$(this).attr("mobile");
            if(mobile!=null && mobile!=""){
                $("#phoneStr1").val($("#phoneStr1").val()+mobile+",");
            }
        });
        $("#ygtxl").remove();

        $("#vss").append("<tr  class='div_bd' id='ygtxl' style='background-color:#ffffff'><td style='border-left:0;border-right:0' align='center' name='ConR'  valign='middle'>"+getJsLocaleMessage("ydwx","ydwx_jtwxfs_58")+"</td><td style='border-left:0;border-right:0'  align='center' valign='middle' >"+
            "<a onclick='javascript:showInfo();' style='cursor:pointer'><font style='color:#0e5ad1'>"+getJsLocaleMessage("ydwx","ydwx_jtwxfs_59")+"(<label style='color:#0e5ad1' id='choiceNum'></label>"+getJsLocaleMessage("ydwx","ydwx_jtwxfs_60")+")</font> </a></td><td  style='border-left:0;border-right:0' align='center' valign='middle' height='20px'>"+
            "<a  onclick='javascript:delAddr()'><img border='0' src='"+commonPath+"/common/widget/zTreeStyle/img/delete.gif' style='cursor:pointer'/></a></td></tr>");
        $(window.frames['flowFrame'].document).find("#rightSelectTempAll").html($(window.frames['flowFrame'].document).find("#right").html());
        //改变弹出框状态为1
        $("#hidIsDoOk").val("1");
        $("#infoDiv").dialog("close");
        $(window.frames['flowFrame'].document).find("#right").empty();
        $(window.frames['flowFrame'].document).find("#getChooseMan").empty();
        $(window.frames['flowFrame'].document).find("#rightSelectTemp").empty();
        $("#choiceNum").html($(window.frames['flowFrame'].document).find("#manCount").html());
        showSelect();
	}
}

//清空所选择对象
function doOk1() {
    var eIds = "";
    var names = "";
    var mobile="";
    if ($(window.frames['flowFrame'].document).find("#right option").size() < 1) {
        alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_61"));
        return;
    }
    var ops = $(window.frames['flowFrame'].document).find("#right option");
    var buffer="";
    var trs= $("#vss").children().children();
    //重新生成表格里面的机构
    $.each(trs,function(i,n){
        var trId=$(n).attr("id");
        var index=trId.indexOf("dep");
        var name=$($(n).children()[0]).text();
        if(index>-1)
        {
            $(n).remove();
        }
    });
    for(var j = 0 ;j<ops.length;j=j+1)
    {

        eIds = ops[j].value;
        names = ops[j].text;
        mobile =ops[j].mobile;
        if(mobile!='' && $("#phoneStr").val().indexOf(","+$.trim(mobile)+",")>-1)
        {
            $(window.frames['flowFrame'].document).find("#right option[mobile='"+mobile+"']").remove();
        }
    }
    $(window.frames['flowFrame'].document).find("#right option").each(function() {
        var trs = $("#vss").children().children();
        var num = trs.length;
        eIds = $(this).val();
        names = $(this).text();
        mobile =$(this).attr("mobile");
        if(mobile!=null && mobile!=""){
            $("#vss").append("<tr id='yt"+eIds+"' ><td align='center' name='ConR'  valign='middle'>"+names
                +"<input  type='hidden' value ='"+mobile+"' id='"+mobile
                +"' name='bts'/></td><td align='center' valign='middle' >"+mobile
                +"</td><td align='center' valign='middle'><a onclick='abc(\"yt"+eIds+"\","+mobile+",\"aa\""+")' >"+getJsLocaleMessage("ydwx","ydwx_common_shanchu")+"</a></td></tr>");
            $("#phoneStr").val($("#phoneStr").val()+mobile+",");
        }else
        {
            $("#vss").append("<tr id='dep"+eIds+"' ><td align='center' name='ConR'  valign='middle'>"+names
                +"<input  type='hidden' value ='"+eIds+"' id='"+eIds
                +"' name='bts'/></td><td align='center' valign='middle' >"+getJsLocaleMessage("ydwx","ydwx_jtwxfs_62")
                +"</td><td align='center' valign='middle'><a onclick='abc(\"dep"+eIds+"\",\""+eIds+"\",\""+names+"\")' >"+getJsLocaleMessage("ydwx","ydwx_common_shanchu")+"</a></td></tr>");
        }
    });
    $(window.frames['flowFrame'].document).find("#right").empty();
    $(window.frames['flowFrame'].document).find("#getChooseMan").empty();
    $("#infoDiv").dialog("close");
    showSelect();
    $(window.frames['flowFrame'].document).find("#rightSelectTemp").empty();
}

// 清空主界面的选择对象（div#sameSendInfo）的数据
function clearUser(){
    // 把主页面的数据清空
    $("#empDepIds").val("");
    $("#cliDepIds").val("");
    $("#groupIds").val("");
    $("#empIds").val("");
    $("#cliIds").val("");
    $("#malIds").val("");
    $("#phoneStr1").val("");
}
//显示人员相关信息
function showInfo() {
    $(".select_clear").hide();
    var skinType = $("#skinType").val();
	var lgcorpcode = $("#lgcorpcode").val();
    var lguserid = $("#lguserid").val();
    //选择类型查询  1.员工通讯录   2.客户通讯录  3.群组(员工+客户) 4.员工群组 5.客户群组 6.客户属性 7.签约用户
    var chooseType = "1,2,3";
	var _height = 560;
	var _width = 540;
	if("4.0" === skinType){
		$(".select_clear").show();
        $(".select_cancel").hide();
		$("#flowFrame").attr("src",commonPath + "/common/selectUserInfo.jsp?lgcorpcode="+ lgcorpcode +"&lguserid=" + lguserid + "&chooseType=" + chooseType);
		_height = 565;
		_width = 690
	}else {
        $(window.frames['flowFrame'].document).find("#right").html($(window.frames['flowFrame'].document).find("#rightSelectTempAll").html());
        $(window.frames['flowFrame'].document).find("#getChooseMan").html($(window.frames['flowFrame'].document).find("#rightSelectTempAll").html().replace(/option/gi, "li").replace(/value/g, "dataval"));

        //<%--代表的是员工机构IDS --%>
        $(window.frames['flowFrame'].document).find("#empDepIdsStrs").val($("#empDepIds").val());
        //<%--代表的是客户机构IDS --%>
        $(window.frames['flowFrame'].document).find("#cliDepIdsStrs").val($("#cliDepIds").val());
        //<%--代表的是群组IDS --%>
        $(window.frames['flowFrame'].document).find("#groupIdsStrs").val($("#groupIds").val());
        $(window.frames['flowFrame'].document).find("#groupClientStr").val($("#groupClient").val());

        //window.frames['flowFrame'].window.frames['sonFrame'].a();
        var type = $(window.frames['flowFrame'].document).find("#chooseType").val();
        if (type == 1) {
            $(window.frames['flowFrame'].document).find("#showUserName").html(getJsLocaleMessage("ydwx", "ydwx_jtwxfs_63"));
        }
        else {
            $(window.frames['flowFrame'].document).find("#showUserName").html(getJsLocaleMessage("ydwx", "ydwx_jtwxfs_64"));
        }
        $(window.frames['flowFrame'].document).find("#left").empty();
        //hideSelect();
        document.getElementById('flowFrame').contentWindow.changeChooseType();
    }
    $("#infoDiv").dialog({
        autoOpen: false,
        height:_height,
        width: _width,
        resizable:false,
        modal: true,
        open:function(){
            hideSelect();
        },
        close:function(){
            if("4.0" !== skinType){
                //0为取消操作；1：为确定操作
                var hidIsDoOk = $("#hidIsDoOk").val();
                if(hidIsDoOk == "0"){
                    doNo();
                }
                $("#hidIsDoOk").val("0");
            }
        }
    });
    changeDialogTitleCss('infoDiv');
    $("#infoDiv").dialog("open");
}

// 加载进度条
function Pro(u,a){
	parent.$("#processbar").progressBar(u*100/a);
}
// 输入手机号后的相关处理
function addph(obj){

	//console.log(phone);
//	if(obj.value!=obj.value.replace(/\D/g,'')){
//	  obj.value=obj.value.replace(/\D/g,'');
//	} 
	phoneInputCtrl($("#tph"));
	var phone=$.trim($("#tph").val());
	//小于等于21位

if(phone.length==11&&phone.substring(0,1)!='+'&&phone.substring(0,2)!='00'){
 		$.post("wx_send.htm",
 	     {method:"filterPh",tmp:phone},
 	     function(data){
      		if(data=="false")
          	{
      		}else
          	{
         		//遍历你窗口已存在的手机号码，并放入一个数组中保存
	     		var hen=new Array();
         		//合法
              　 			var trs= $("#vss").children().children();
       			$.each(trs,function(i,n){
		 	    	var tds=$(n).children();
			        $.each(tds,function(j,d){
			            if(j==1)
			            {
			                var ps=$(d).text();
			                hen[i]=ps;
			            }
        			}); 
    	 		});
     			var flag="0"
			    for(var g=1;g<hen.length;g++)
				{
			    	// 判断新增的手机号码是否已存在
			    	if(phone==hen[g])
				    {
			    		flag="1";
			           	break;
			        }
			    }  
				//看看状态4
				if(flag!="1")
				{//增加一个
       				$("#vss").append("<tr  class='div_bd' id='"+phone+"' style='background-color:#ffffff'><td  style='border-left:0;border-right:0' align='center' name='ConR'  valign='middle'>"+getJsLocaleMessage("ydwx","ydwx_jtwxfs_65")+"<input type='hidden' value ='"+
               				phone+"' id='bt' name='bt'/></td><td  style='border-left:0;border-right:0' align='center' valign='middle' >"+
               				phone+"</td><td  style='border-left:0;border-right:0' align='center' valign='middle' height='20px' style='border-right:0px'><a onclick=ddll('"+phone+
               				"')><img border='0' src='"+commonPath+"/common/widget/zTreeStyle/img/delete.gif' style='cursor:pointer'></img></a></td></tr>"); 
					$("#phoneStr2").val($("#phoneStr2").val()+phone+",");
					$("#tph").val("");
				}else{
       			}  
      		}
  		});
	}
}

// 输入手机号后的相关处理
function addph2(){
	var phone=$("#tph").val();
	if(phone!=""){
		if(phone==getJsLocaleMessage("ydwx","ydwx_jtwxfs_66")){
			alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_67"));
			$("#tph").focus();
		}else if(phone.length<7||phone.length>21){
			alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_86"));
			$("#tph").focus();
			}else{
				$.post("wx_send.htm",
	     		{method:"filterPh",tmp:phone},
	     		function(data){
  				if(data=="false")
      			{
    				alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_68"));
    				$("#tph").focus();
  				}else
      			{
     				//遍历你窗口已存在的手机号码，并放入一个数组中保存
     				var hen=new Array();
     				//合法
          　 					var trs= $("#vss").children().children();
   					$.each(trs,function(i,n){
	 	    			var tds=$(n).children();
		        		$.each(tds,function(j,d){
		            		if(j==1)
		            		{
		                		var ps=$(d).text();
		                		hen[i]=ps;
		            		}
    					}); 
	 				});
 					var flag="0"
		    		for(var g=1;g<hen.length;g++)
					{
		    			// 判断新增的手机号码是否已存在
		    			if(phone==hen[g])
			    		{
		    				flag="1";
		           			 break;
		        		}
		    		}  
					//看看状态
					if(flag!="1")
					{
						var tempPhone=phone;
						var phonetemp="";
						if(tempPhone.indexOf("+")>-1){
							tempPhone=tempPhone.replace("+","");
							 phonetemp="\"phone_"+tempPhone+"\"";
						}else if(tempPhone.indexOf("00")>-1){
							 phonetemp="\"phone"+tempPhone+"\"";
						}
					
   						$("#vss").append("<tr class='div_bd' id="+phonetemp+"><td  style='border-left:0;border-right:0' align='center' name='ConR'  valign='middle'>"+getJsLocaleMessage("ydwx","ydwx_jtwxfs_65")+"<input type='hidden' value ='"+
           						phone+"' id='bt' name='bt'/></td><td  style='border-left:0;border-right:0' align='center' valign='middle' >"+
           						phone+"</td><td  style='border-left:0;border-right:0' align='center' valign='middle' style='border-right:0px'><a onclick='ddll("+phonetemp+
           						")'><img border='0' src='"+commonPath+"/common/widget/zTreeStyle/img/delete.gif' style='cursor:pointer'></img></a></td></tr>"); 
						
   						$("#phoneStr2").val($("#phoneStr2").val()+phone+",");
					}else{
   						alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_69"));
   					}
					$("#tph").val("");  
  				}
				});
			}
		}else{
 		alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_70"));
		}
}

//点击删除链接后的处理
function abc(ag,phone,names){
	if(confirm(getJsLocaleMessage("ydwx","ydwx_jtwxfs_54")))
	{ 
		if(ag.indexOf("yt")>-1)
		{
			$("#"+ag+"").remove();
			$("#phoneStr2").val($("#phoneStr2").val().replace(phone+",",""));
		}else{
			$("#"+ag+"").remove();
			if(names.indexOf(getJsLocaleMessage("ydwx","ydwx_jtwxfs_71"))>-1)
			{
				$("#empDepIds").val($("#empDepIds").val().replace("e"+phone+",",""));
			}else{
				$("#empDepIds").val($("#empDepIds").val().replace(phone+",",""));
			}
		}
		}
	}
	
	
var flag=true;
var isFile=true;
//浏览按纽事件 
function ch(){
		var obj=$("#numFile");
		var pathValue="";
		pathValue = $("#numFile").val();
		var ip=$("#11").attr('id');
	var index = pathValue.lastIndexOf("\\");
	var name = pathValue.substring(index + 1);
		if(checkFile()){
		$("#11").remove();
			$("#vss").append("<tr class='div_bd' style='background-color:#ffffff' id='11'><td  style='border-left:0;border-right:0' align='center' valign='middle' >号码文件</td><td  style='border-left:0;border-right:0' align='center' name='FName' valign='middle'>"+name+
	       			"</td><input type='hidden' value='FileTxt'  /><td  style='border-left:0;border-right:0' align='center' name='Kind' valign='middle'><a onclick='ddll(11)'><img border='0' src='"+commonPath+"/common/widget/zTreeStyle/img/delete.gif' style='cursor:pointer'></img></a></td></tr>"); 
		}
}

//加载机构人员数据
function ok()  
{
 	var  a=$("#queryString").val();
	var hen=new Array();
    //合法
   　        	var trs= $("#vss").children().children();           
    $.each(trs,function(i,n){
        
	            var tds=$(n).children();
       	    $.each(tds,function(j,d){
            if(j==1)
            {
                var ps=$(d).text();
                hen[i]=ps;
            }
   	    
        }); 
    });
		var cc=a.split(',');

			//遍历人员
	  for(var x=0;x<cc.length;x++)
	  {
	      var ab=cc[x].toString();
	      var qp=ab.split(';');
	     //判断是否是机构 qp.length=3是机构,=4是人员
	      if(qp.length==4){
	      var pid=qp[0].split(':');
	      var idp=pid[1].toString().substring(1);
	      var ge=qp[1].split(':');
	      var gs=qp[2].split(':');
	      if(gs[1]!=""){
		      var flag="0"
		      for(var g=1;g<hen.length;g++)
			  {
		         if(gs[1]==hen[g])
			     {
		            flag="1";
		            break;
		         }
		      }
		      if(flag!="1")
			  {
		         $("#vss").append("<tr class='div_bd' id='"+idp+"' ><td align='center' name='ConR'  valign='middle'>"+ge[1]
	                    +"<input  type='hidden' value ='"+gs[1]+"' id='"+gs[1]
	                    +"' name='bts'/></td><td align='center' valign='middle' >"+gs[1]
	                    +"</td><td height='20px' align='center' valign='middle'><a onclick='abc("+idp+","+gs[1]+
	                    ")' ><img border='0' src='"+commonPath+"/common/widget/zTreeStyle/img/delete.gif' style='cursor:pointer'></img></a></td></tr>"); 
		         $("#phoneStr2").val($("#phoneStr2").val()+gs[1]+",");
			  } 
          }
	  }
	}
	$("#infoDiv").dialog("close");
}
var fileCount=1;
// 导入文件
function addFiles(){
		var obj=$("#numFile"+fileCount);
		var pathValue="";
		pathValue = $("#numFile"+fileCount).val();
	var index = pathValue.lastIndexOf("\\");
	var name = pathValue.substring(index + 1);
	if(name.length >12)
	{
	   name = name.substring(0,12)+"....";
	}
		if(checkFile("numFile"+fileCount)){  
		    if ($("#tr"+fileCount).length == 0){ 	  		     	     
  		     $("#vss").append("<tr class='div_bd' style='background-color:#ffffff' id='tr"+fileCount+"'><td  style='border-left:0;border-right:0' align='center' valign='middle' >"+getJsLocaleMessage("ydwx","ydwx_jtwxfs_72")+"</td><td  style='border-left:0;border-right:0' align='center' name='FName' valign='middle'>"+name+
		  		     "</td><td  style='border-left:0;border-right:0' align='center' name='Kind' valign='middle'><a onclick=ddll('tr"+fileCount+"')><img border='0' src='"+commonPath+"/common/widget/zTreeStyle/img/delete.gif' style='cursor:pointer'></img></a></td></tr>");       			 
   			 $("#numFile"+fileCount).css("display","none");
   			 fileCount++; 
   			 var ss = $("#filesdiv").html();      			
   			 var ss1 = "<input type='file' id='numFile"+fileCount+"' name='numFile"+fileCount+"' value='' onchange='addFiles();' class='numfileclass' style='height: 70px;width: 70px;'/>";
   			 $("#filesdiv").prepend(ss1);
   			 $("#allfilename").append(pathValue+";");
			}
		}
}



// 样式调整
function inputTipText(){
	//所有样式名中含有graytext的input
	$("input[class*=graytext]").each(function(){
		var oldVal=$(this).val(); //默认的提示性文本
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

 //直接加载
$(function(){
	inputTipText(); //直接调用就OK了
})
		//点击预览 查看
		function Look(netId){
		    //$("#netid").val(netId);
		    $.post('wx_manger.htm?method=showNetById',{netId:netId},function(data){
		       data=eval("("+data+")");
		       listpage=data;
		       $(".ym").children().remove();
		       for(var i=0;i<listpage.length;i++)	{ 
		           $("<option>").val(listpage[i].id).html(listpage[i].name).appendTo($(".ym"));
		      }
                // 此处为显示错误页面，避免进入登录页面
                if(listpage[0].content=="notexists"){
                    $("#nm_preview_common1").attr("src","ydwx/wap/404.jsp");
                }else{
                    $("#nm_preview_common1").attr("src","file/wx/PAGE/wx_"+listpage[0].id+".jsp");
                }
		      $("#divBox").dialog("open");
		       changeDialogTitleCss('divBox');
	       });
	    }

//选择网讯模板
function chooseNetTpl(skin) {
	var pageSize = 10;
	//如果为4.0皮肤则查询5条记录
	if(skin !== "" && skin === "4.0"){
        pageSize = 5;
	}
	changeDialogTitleCss('tmplDiv');
	$("#ui-dialog-title-tmplDiv").parent().parent().addClass("tmplDivHeight");
	$(".ui-dialog-titlebar-close").show();
	var frameSrc = $("#tempFrame").attr("src");
	var lgcorpcode = $("#lgcorpcode").val();
	if(frameSrc.indexOf("blank.jsp") > 0) {
		var lguserid = $("#lguserid").val();
		frameSrc = path+"/wx_send.htm?method=getNetTemplate&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode+"&pageSize="+pageSize;
		$("#tempFrame").attr("src",frameSrc);
	}
	$("#tmplDiv").dialog("open");
}

/*jquery-ui的dialog的样样式*/
function changeDialogTitleCss(id) {
    var $titleBar = $("#ui-dialog-title-" + id);
    $titleBar.parent().addClass("titleBar");
    $titleBar.addClass("titleBarTxt");
}

//发送成功跳转群发任务查看界面
 function sendRecord(taskId, lguserid, lgcorpcode)
 {
	closemessage();
	window.parent.openNewTab('2700-2100',base_path+"/wx_taskreport.htm?method=find&taskid="+taskId+"&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode+"&pageIndex="+1+"&pageSize="+15);
 }
 function closemessage()
 {
	 $("#message").dialog("close");
 }
 
 //批量输入
 function bulkImport(){
	 var empLangName = $("#empLangName").val();
	 $('#bulkImport_box').dialog({
				autoOpen: false,
				height: 415,
				width: "zh_HK"===empLangName?600:542,
				resizable:false,
				modal:true
			});
	$('#bulkImport_box').dialog('open');
	changeDialogTitleCss('bulkImport_box');
	$('#importArea').blur();
	$('#importArea').html($('#importAreaTemp').html());
	// var num=$('#importAreaTemp').attr('data-num');
	// num=typeof num=='undefined' ? 0 : num;
	// $('#bNum').html(num);
 }
 function bultCancel(){
	 $('#bulkImport_box').dialog('close');
	 $('#importArea').html("");
 }

function formatTelNum(element){
	
	var str=$.trim($('#importArea').val());
	
	var reg=/[\s,、\n，]+/g;
	var result=str.replace(reg," ");
	var arr=result.split(" ");
	if(arr[arr.length-1]==""){
		var len=arr.length-1;
	}else{
		var len=arr.length;
	}
	$(element).html(len);
}
function previewTel(){
	if($('#importArea').val()==''){
		alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_73"));
		return false;
	}
	var phone=$('#bNum').html();
	//批量输入最大支持20000个号码
	if(20000 - phone < 0)
	{
		alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_74"));
		return false;
	}
	$('.bultPhone').remove();
	var str="<tr class='div_bd bultPhone' id='" + phone + "' style='border-left:0;border-right:0'><td  align='center' name='ConR'  valign='middle'>"+getJsLocaleMessage("ydwx","ydwx_jtwxfs_75")+"<input type='hidden' value ='" + phone + "' id='bt' name='bt'/></td>"+
		"<td  style='border-left:0;border-right:0' align='center' valign='middle' >"+
		"<a onclick='javascript:bulkImport();' style='cursor:pointer'><font styl<font style='color:#0e5ad1'>"+getJsLocaleMessage("ydwx","ydwx_jtwxfs_59")+"(<label style='color:#0e5ad1' id='Batchnput'>" + phone + "</label>"+getJsLocaleMessage("ydwx","ydwx_jtwxfs_60")+")</font></a></td>"+
		"<td  style='border-left:0;border-right:0' align='center' valign='middle' style='border-right:0px'><a onclick=delinputphone('" + phone + "')><img border='0' src='"+commonPath+"/common/widget/zTreeStyle/img/delete.gif' style='cursor:pointer'></img></a></td></tr>";
	$("#vss").append(str);

	var inputphone=$('#importArea').val();
	$('#importAreaTemp').html(inputphone).attr('data-num',phone);
	var reg=/[\s,、\n，]+/g;
	
	var result=inputphone.replace(reg,",");
	if(result.substr(0,1)==",")
	{
		result = result.substr(1);
	}
	$("#inputphone").val(result);
	$("#phoneStr").val(result);
	bultCancel();
}

//数组去重
function distinctArray(arr){
	var tempArr=[],obj={};
	for(var i=0;i<arr.length;i++){
		if(!obj[arr[i]] && arr[i]!==''){
			tempArr.push(arr[i]);
			obj[arr[i]]=true;
		}
	}
	return tempArr;
}

function delinputphone(phone) {
	if (confirm(getJsLocaleMessage("ydwx","ydwx_jtwxfs_54"))) {
		$("#" + phone).remove();
		$("#inputphone").val("");
		$("#importArea").val("");
		$('#importAreaTemp').val("").attr('data-num',0);
	}
}

function setDefault()
{
	if(confirm(getJsLocaleMessage("ydwx","ydwx_dtwxfs_82"))) {
		var lguserid = $('#lguserid').val();
		var lgcorpcode = $('#lgcorpcode').val();
		var busCode = $("#busCode").val();
		var spUser = $("#spUser").val();
		$.post("per_sendNot.htm?method=setDefault", {
			lguserid: lguserid,
			lgcorpcode: lgcorpcode,
			busCode: busCode,
			spUser: spUser,
			flag: "7"			
			}, function(result) {
			if (result == "seccuss") {
				/*当前选项设置为默认成功！*/
				alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_83"));
				return;
			} else {
				/*当前选项设置为默认失败！*/
				alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_84"));
				return;
			}
		});
	}
}

//贴尾显示
function getSmsTailcontent(){
	var lgcorpcode = $('#lgcorpcode').val();
	$.ajax({
		url:"wx_send.htm",
		data:{method : "getSmsTailcontent",lgcorpcode : lgcorpcode},
		type:"post",
		success:function(result){
			if(result=="error")
			{
				alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_79"));
				return;
			}
			//用于后台处理
			 $("#smsTail").val(result);
			 //用于显示
			 $("#smsTailContens").val(result);
		},
		error:function(xrq,textStatus,errorThrown){
			alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_30"));
		}
	});
} 	
//选择草稿
function showDraft(skinType){
	var pageSize = 10;
	if(skinType === "4.0"){
        pageSize = 5;
	}
    $("#draftDiv").dialog({
        autoOpen: false,
        height:skinType === "4.0"?618:500,
        width: "zh_HK" === empLangName?1000:900,
        resizable:false,
        modal: true,
        open:function(){},
        close:function(){
            //关闭草稿箱选择 页面内容移除
            $("#draftFrame").attr("src","");
        }
    });
    var draftstype = 4;
    var frameSrc = path+"/wx_send.htm?method=getDrafts&draftstype="+draftstype+"&timee="+new Date().getTime()+"&pageSize="+pageSize;
    $("#draftFrame").attr("src",frameSrc);
    $("#draftDiv").dialog("open");
    changeDialogTitleCss('draftDiv');
}

//选择草稿记录
function draftSure()
{
    var $fo = $("#draftFrame").contents();
    var $ro = $fo.find("input[type='radio']:checked");
    if(!$ro.val())
    {
        alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_80"));
        return;
    }
    //已存在草稿箱 提示覆盖
    if($('#containDraft').val()){
        if(confirm(getJsLocaleMessage("ydwx","ydwx_jtwxfs_81"))){
            $('#containDraft').parents('tr').remove();
            //删除草稿文件后 清空草稿信息
            $('#draftFile').val('');
            $('#draftId').val('');
        }else{
            $("#draftDiv").dialog("close");
            return false;
        }
    }
    
    //****覆盖之前选择的文件，与手动写的号码****
    $("#vss").find("tr").each(function(){
    	if($(this).attr('id')!="first"){
    		$(this).remove();
    	}
       });

    for(var k=1;k<=fileCount;k++){
    	$("#numFile"+k).remove();
    }
    fileCount=1;
    var ss1 = "<input type='file' id='numFile1' name='numFile1' value='' onchange='addFiles();' class='numfileclass' style='height: 70px;width: 70px;'/>";
    $("#filesdiv").append(ss1);
    $("#allfilename").html("");
    $("#phoneStr2").val("");
    $("#phoneStr1").val("");
    $("#inputphone").val("");
    $("#phoneStr").val("");
    $("#inputphone").val("");
    //***************************
    
    //回填草稿箱内容前 如若存在已选择的模板 则需取消掉模板
    tempNoShow();
    
    var $tr = $ro.parents('tr');
    //草稿箱发送文件相对路径
    var filePath = $tr.find('td:eq(0)').attr('path');
    var draftId = $ro.val();
    var taskname = $tr.find('td:eq(2)').find('label').text()||'';
    var msg = $tr.find('td:eq(3)').find('label').attr('msg');
    var trhtml = [];
    trhtml.push("<tr  class='div_bd' style='background-color:#ffffff'>");
    trhtml.push("<td  style='border-left:0;border-right:0' align='center' valign='middle' >"+getJsLocaleMessage("ydwx","ydwx_jtwxfs_87")+"</td>");
    trhtml.push("<td  style='border-left:0;border-right:0' align='center' name='FName' valign='middle'>");
    trhtml.push("<label class='draft-label'>"+draftId+" "+taskname+"</label>");
    trhtml.push("<input type='hidden' id='containDraft' name='containDraft' value='1'>");
    trhtml.push("</td>");
    trhtml.push("<td  style='border-left:0;border-right:0' align='center' name='Kind' valign='middle'><a onclick='delRow(this);'><img border='0' src='" + $('#cpath').val() + "/common/widget/zTreeStyle/img/delete.gif' style='cursor:pointer' /></a></td>");
    trhtml.push("</tr>");
    $("#vss").append(trhtml.join(''));
    $('#draftFile').val(filePath);
    $('#draftId').val(draftId);
    $('#taskname').css('color','').val(taskname);
    $('#contents').val(msg);
    len($('#contents'));
    $("#draftDiv").dialog("close");
}

	/**
	 * 删除草稿箱文件行
	 * @param obj
	 */
	function delRow(obj){
    if(confirm(getJsLocaleMessage("ydwx","ydwx_jtwxfs_54"))){
        $(obj).parents('tr').remove();
        //删除草稿文件后 清空草稿信息
        $('#draftFile').val('');
        $('#draftId').val('');
    	}
	}
	function draftCancel()
	{
	    $("#draftDiv").dialog("close");
	}
	/**
	 * 保存草稿回调
	 */
	function saveDraft(result){
	    result = eval('('+result+')');
	    if(result.ok == 1){
	        $('#draftFile').val(result.draftpath);
	        $('#draftId').val(result.draftid);
	        alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_82"))
	    }else if(result.ok == -1){
	        alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_83"));
	    }else if(result.ok == -2){
	        alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_84"));
	    }else{
	        alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_85"));
	    }
	    //暂存草稿按钮置为可用
	    $('#toDraft').attr('disabled',false);
	}
	
	//设置贴尾
	function setTailInfo(){
	    //业务类型
	    var busCode = $('#busCode').val();
	    //sp账号
	    var spUser = $("#spUser").val();
	    //返回结果
	    var result = true;
	    //先根据其他贴尾类型查找贴尾 再根据全局贴尾 找到就终止
	    $.ajax({
	        url:'common.htm',
	        data:{method:'setTailInfo',busCode:busCode,spUser:spUser,lgcorpcode:GlobalVars.lgcorpcode},
	        type:'post',
	        dataType:'text',
	        async:false,
	        beforeSend:function(){
	        	//显示层
	            $('#tail-area').hide();
	            //显示文本
	            $('#smsTailContens').text('');
	            //传输后台使用
	            $('#smsTail').val('');
	        },
	        success:function(data){
	            data = eval('('+data+')');
	            if(data.status == 1){//找到对应贴尾
	                $('#smsTailContens').text(data.contents);
	                $('#tail-area').show();
	                $('#smsTail').val(data.contents);
	            }
	        },
	        error:function(){
	            result = false;
	        },
	        complete:function(){

	        }
	    })
	    return result;
	}
