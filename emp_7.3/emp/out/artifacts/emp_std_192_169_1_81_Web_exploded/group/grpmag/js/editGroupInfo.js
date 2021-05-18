$(document).ready(function(){
	$("#left").dblclick(function(){
		moveLeft();
	});
    var temp = $("input#addGpName").val();
    if(temp===""||temp==null){
        $("#gpName_label").show();
    }
	$("#addGpName").focus(function () {
		$("#gpName_label").hide();
	});
    $("#addGpName").blur(function () {
        $("#gpName_label").hide();
    	if($(this).val()===""){
            $("#gpName_label").show();
		}
    });
    var temp1 = $("input#epname").val();
    if(temp1===""||temp1==null){
        $("#enterName").show();
    }
    $("#epname").focus(function () {
        $("#enterName").hide();
    });
    $("#epname").blur(function () {
        $("#enterName").hide();
        if($(this).val()===""){
            $("#enterName").show();
        }
    });
	//window.frames['sonFrame'].a();
    window.parent.$('#load-bg').hide();

    $("#right").find("p").each(function () {
    	if($(this).attr("etype") === "1"){
    		//员工
			$("#ygStr").val($("#ygStr").val() + $(this).attr("pva") + ",");
		}
        if($(this).attr("etype") === "2"){
            //群组
            $("#qzStr").val($("#qzStr").val() + $(this).attr("pva") + ",");
        }
        if($(this).attr("etype") === "4"){
            //共享
            $("#gxStr").val($("#gxStr").val() + $(this).attr("pva") + ",");
        }
    });
});
var ipathh = $("#ipath").val();
function moveLeft() 
{
	if ($("#left option:selected").length > 0) 
	{
		$("#left option:selected").each(function() {
			var tempVal = $(this).val();//当前树的id
			var isExist = false;
			/*$("#right").find("p").each(function(){
				if ($(this).attr("pva") == tempVal)
				{
					isExist = true;
				}
			});*/
			var $rpv = $("#right").find("p[pva='"+tempVal+"']");
			isExist = ($rpv.html()!=null);
			var text = $(this).text();
			if (!isExist) {
				 var types = $("#chooseType").val();
				 if(types != 1)
				 {
                     var myReg = /(.*)(\[.*\])/;
                     text =  text.replace(myReg,"$1");
				 }
				var etypeStr = getTypeStr($(this).attr("etype"),tempVal);
				var phoneStr = getPhoneStr($(this).attr("etype"),$(this).attr("mobile"));
		
				var phtmlstr = "<p title='"+text+"' onclick='cliselp($(this))'  ondblclick='moveme($(this))' etype='"+$(this).attr("etype")+"' pva='"+ $(this).val()+"'";
				if(etypeStr==getJsLocaleMessage('group','group_js_addGroupInfo_selfbuilt'))
				{
					$("#right").find("p[pva2='"+text+"|"+phoneStr+"']").remove();
					phtmlstr = phtmlstr + "pva2 ='"+text+"|"+phoneStr+"'";
				}
				phtmlstr= phtmlstr +"><span class='ld div_bd' >"+ text+"</span>"
						+"<span class='md div_bd' >"+ phoneStr+"</span>"
						+"<span class='rd div_bd' >"+ etypeStr+"</span></p>";
				$("#right").append(phtmlstr);
				//var countt = parseInt($("#numcount").text());

			} else {
			//	alert(text+"已经添加！");
				/*if(!$rpv.hasClass("hoverColor"))
				{
					cliselp($rpv);
				}*/
			}
		});
	$("#numcount").text($("#right > p").length);
	}
}

function getTypeStr(etype,tempVal)
{
	if(etype == 1 || etype=="1")
{
	var ygStr = $("#ygStr").val();
	$("#ygStr").val(ygStr+tempVal+",");
	return getJsLocaleMessage('group','group_js_addGroupInfo_employee') ; //"员工";
}
if(etype == 2 || etype=="2")
{
	var qzStr = $("#qzStr").val();
	$("#qzStr").val(qzStr+tempVal+",");
	return getJsLocaleMessage('group','group_js_addGroupInfo_selfbuilt') ; //"自建";
}
if(etype == 3 || etype=="3")
{
	var zjStr = $("#zjStr").val();
	tempVal = (tempVal||'').replace(/,/g, '');
	$("#zjStr").val(zjStr+tempVal+",");
	return getJsLocaleMessage('group','group_js_addGroupInfo_selfbuilt') ; //"自建";
}
if(etype == 4 || etype=="4")
{
	var gxStr = $("#gxStr").val();
	$("#gxStr").val(gxStr+tempVal+",");
	return getJsLocaleMessage('group','group_js_addGroupInfo_shared') ; //"共享";
}
return "";
}

function getPhoneStr(etype,phone)
{
	var isll = $("#plook").val();
	if(isll == 0)
	{
		if((etype != 2 || etype!="2")
			&& (etype != 3 || etype!="3"))
	{
		if(phone.length == 11)
		{
			return phone.substr(0,3)+"*****"+phone.substr(8,11);
			}else
			{
				return phone;
			}
		}
	}
	return phone;
}
function moveallRight() {
	$("#right p").each(function() {
	moveme($(this));
});
$("#numcount").text("0");
}
function moveme(aa) {
	var etype = aa.attr("etype");
	if(etype === "1") {
        var ygStr = $("#ygStr").val();
        var ygva = aa.attr("pva")+",";
        ygStr = ygStr.replace(ygva,"");
        $("#ygStr").val(ygStr);
	}else if(etype === "2" ) {
        var qzStr = $("#qzStr").val();
        var qzva = aa.attr("pva")+",";
        qzStr = qzStr.replace(qzva,"");
        $("#qzStr").val(qzStr);
	}else if(etype === "3") {
		var zjStr = $("#zjStr").val();
		var zjva = aa.attr("pva")+",";
		zjStr = zjStr.replace(zjva,",");
		$("#zjStr").val(zjStr);
	}else if(etype === "4" ) {
        var gxStr = $("#gxStr").val();
        var gxva = aa.attr("pva")+",";
        gxStr = gxStr.replace(gxva,"");
        $("#gxStr").val(gxStr);
	}
	aa.remove();
	var countt = parseInt($("#numcount").text());
	$("#numcount").text(countt-1);
}
function searchNameInputReset() {
	$("input#epname").val("");
	$("label#enterName").show();
    $("input#epname").removeAttr("disabled");
}
var curI = 1;
function changeChooseType(i)
{
	$("#left").empty();
	$("#prepage").attr("disabled",false);
	$("#nextpage").attr("disabled",false);
	$("#pagecode").empty();
	$("#prepage").css("visibility","hidden");
	$("#nextpage").css("visibility","hidden");
	if( curI == i)
	{
		return;
	}else
	{
		curI = i;
	}
	if(i == 1)
	{
        searchNameInputReset();
		//$("#etree").html("<iframe id='sonFrame' style='width: 240px; height: 175px;' frameborder='1' src='<%=iPath %>/a_addrbookDepTree1.jsp'></iframe>");
		//$("#showUserName").html("当前通讯录：");
		//$("#selectDep").val("选择机构");
		 clickTree();
		$("#etree").show();
		$("#egroup").hide();
		$("#efile").hide();
		$("#div_sel").show();
		$("#left_div").show();
        //显示搜索栏
        $("#epname").show();
        $("#enterName").show();
        $("#btnSearch").show();
	}else if(i == 2)
	{
        searchNameInputReset();
		var egrouphtml = $.trim($("#egroup").html());
		if(egrouphtml == "" || egrouphtml.length == 0){
			$.post(ipathh+"/grp_groupManage.htm",{
				method:"getGroupList" ,
				lguserid:$(window.parent.document).find("#lguserid").val()
	   			},function(GroupList)
	   			{
					$("#egroup").html(GroupList);
				}
			);
			$("#left").html("");
		}else
		{
			grouponChange();
		}
		//$("#showUserName").html("当前群组：");
		//$("#selectDep").val("选择群组");
		$("#etree").hide();
		$("#egroup").show();
		$("#efile").hide();
		$("#div_sel").show();
		$("#left_div").show();
        //显示搜索栏
        $("#epname").show();
        $("#enterName").show();
        $("#btnSearch").show();
	}
	else
	{
        $("input#epname").val("");
        $("input#epname").attr("disabled","disabled");
        $("label#enterName").hide();
		$("#etree").hide();
		$("#egroup").hide();
		$("#efile").show();
		$("#div_sel").hide();
		$("#left_div").hide();
        //隐藏搜索栏
        $("#epname").hide();
        $("#enterName").hide();
        $("#btnSearch").hide();
	}
}
function grouponChange(isSearch)
 {
	    $("#left").empty();
	$("#prepage").attr("disabled",false);
	$("#nextpage").attr("disabled",false);
	$("#pagecode").empty();
	$("#prepage").css("visibility","hidden");
	$("#nextpage").css("visibility","hidden");
	var pageIndex = 1;
    $("#pageIndexAdd").val(pageIndex);

	var tempVal="";
	var name ="";
	var epname = $("#epname").val();
	var epno = $("#epno").val();
	var udgid1 = "";
	var udgid2 = "";
	$("#groupList option:selected").each(function()
		{
		 	tempVal = tempVal+$(this).val()+",";
		 	name = name+$(this).text()+",";
		 	if($(this).attr("qzlx")==1)
		 	{
		 		udgid1 = udgid1+$(this).val()+",";
		 	}else
		 	{
		 		udgid2 = udgid2+$(this).val()+",";
		 	}
		}
	);
	// 如果没有选择群组 //
	if(tempVal==""||tempVal.length==0){
		// alert(getJsLocaleMessage("group","group_editGroupInfo_text"));
		return;
	}
	$.post(ipathh+"/grp_groupManage.htm",{
		method:"getGroupUserByGroupId" ,
		udgId:tempVal ,
		epname : isSearch===1?epname:"",
		lguserid:$(window.parent.document).find("#lguserid").val(),
		epno : epno,
		pageIndex:pageIndex,
		udgid1 : udgid1,
		udgid2 : udgid2
		},function(result){
			if(result != "" ){
						$("#prepage").css("visibility","visible");
						$("#nextpage").css("visibility","visible");
						// 获取分页信息 //
						var arr = result.substring(0,result.indexOf("#")).split(",");
						//总页数 //
						var pageTotal = parseInt(arr[0]);
						//总记录数 //
						var pageRec = arr[1];
						// 添加记录 //
						$("#left").html(result.substring(result.indexOf("#")+1));
						// 显示当前机构 //
						$("#pagecode").html(pageIndex+"/"+pageTotal);
						$("#prepage").attr("disabled",true);
						if(pageTotal == 1){
							$("#prepage").css("visibility","hidden");
							$("#nextpage").css("visibility","hidden");
							return;
						}
					}
		}
	);
	if(name.length>20)
	{
		name = name.substr(0,18)+"...";
	}else if(name.length > 0)
	{
		name = name.substr(0,name.length-1);
	}
	//$("#showUserName").html("当前群组："+name);
 }
function cliselp($pp) {
	$pp.toggleClass("hoverColor");
}
function moveRight()
{
	$("#right .hoverColor").each(function(){moveme($(this));});
}
 var zhihui = 0;
function doAdd(){
	if(zhihui==1) {
		return;
	}else {
		zhihui=1;
	}
	var name = $("#adName").attr("value") ;
	var mobile = $("#adPhone").attr("value") ;
if(name===getJsLocaleMessage('group','group_ydbg_ygtxlgl_text_name')||name.length===0) {
	name = getJsLocaleMessage('group','group_js_addGroupInfo_nothing'); //"No name";
}
if(name.length>0 && outSpecialChar(name)){
	//alert("姓名不能包含特殊符号！") ;
	alert(getJsLocaleMessage('group','group_js_addGroupInfo_illname'));
	$("#adName").focus();
	zhihui = 0;
}else if(mobile.length<7||mobile.length>21){
	//alert("不是有效的手机号码！") ;
	alert(getJsLocaleMessage('group','group_js_editGroupInfo_illphone'));
	$("#adPhone").focus();
	zhihui = 0;
}else{
	zhihui = 1;
	$.post(ipathh+"/grp_groupManage.htm", {
		mobile : mobile,
		method: "checkMoblie"
	}, function(returnMsg) {
			if(returnMsg.indexOf("html") > 0){
				//alert("网络超时，请重新登录！");
				alert(getJsLocaleMessage('group','group_common_nettimeout'));
    			//window.location.href=location;
    		}
			else if (returnMsg == 4) {
				//alert("输入手机号码为非法号码，请确认后重新录入。");
				alert(getJsLocaleMessage('group','group_js_addGroupInfo_reenterphone'));
				$("#adPhone").focus();
			}
			else  if(returnMsg == 3)
			{
				var $rp = $("#right").find("p");
				var isExist = false;
				var tempVal = name+"|"+mobile;
				var $rpv = $("#right").find("p[pva2='"+tempVal+"']");
				isExist = ($rpv.html()!=null);
				if(!isExist)
				{
					getTypeStr(3,tempVal);
					 $("#right").append("<p title='"+name+"' onclick='cliselp($(this))'  ondblclick='moveme($(this))' etype='3' pva='"+tempVal+"'  pva2='"+tempVal+"'>"
								+"<span class='ld div_bd' >"+ name+"</span>"
								+"<span class='md div_bd' >"+ mobile+"</span>"
								+"<span class='rd div_bd' >" + getJsLocaleMessage('group','group_js_addGroupInfo_selfbuilt') + "</span></p>");
					 $("#adName").val("")
					 $("#adPhone").val("")
					 $("#adName").next("label").css("display","inline");
					 $("#adPhone").next("label").css("display","inline");
					 var countt = parseInt($("#numcount").text());
					$("#numcount").text(countt+1);
				}else
				{
					//alert("该信息已添加！");
					alert(getJsLocaleMessage('group','group_js_addGroupInfo_infoadded'));
					/*if(!$rpv.hasClass("hoverColor"))
					{
						cliselp($rpv);
					}*/
				}
			}else{
				//alert("请检查网络/数据库连接是否正确！");
				alert(getJsLocaleMessage('group','group_common_checknet'));
				//window.location.href = location;
				}
				 zhihui=0;
			}
		);
	}
}
function checkText(ep)
{
	//ep.val($.trim(ep.val()));
if(ep.val()=="")
{
	ep.next("label").css("display","inline");
}else
{
	ep.next("label").css("display","none");
	}

}
 function dosearch()
 {
	  var types = $("#chooseType").val();
 var dqcheck;
 if(types == 1)
 {
	$("#sonFrame").contents().find(".curSelectedNode").removeClass("curSelectedNode");
 	$("#depId").val("");
 	$("#prepage").attr("disabled",false);
	$("#nextpage").attr("disabled",false);
	$("#pagecode").empty();
	$("#prepage").css("visibility","hidden");
	$("#nextpage").css("visibility","hidden");
		 clickTree();
	 }
	 else if(types == 2)
	 {
	 	var isSearch = 1;
		 grouponChange(isSearch);
	 }
 }
 function clickTree()
 {
	 $("#left").empty();
 	 var pageIndex = 1;
  $("#pageIndexAdd").val(pageIndex);
	var depId = $("#depId").val();
    var epname = $("#epname").val();
	var epno = $("#epno").val();
    var addTypes = $("#addType").val();
    var lgcorpcode = $(window.parent.document).find("#lgcorpcode").val();
	$.post(ipathh+"/grp_groupManage.htm",
		{
			method:"getDepAndEmpTree1",
			epname : epname,
			lgcorpcode:lgcorpcode,
			pageIndex:pageIndex,
			epno : epno,
			addTypes:addTypes
		}, function(result){
			 $("#pagecode").empty();
						if(result != "" ){
							 $("#prepage").css("visibility","visible");
							 $("#nextpage").css("visibility","visible");
							// 获取分页信息 //
							var arr = result.substring(0,result.indexOf("#")).split(",");
							//总页数 //
							var pageTotal = parseInt(arr[0]);
							//总记录数 //
							var pageRec = arr[1];
							// 添加记录 //
							$("#left").html(result.substring(result.indexOf("#")+1));
							// 如果是只有一页记录的话//
							$("#pagecode").html(pageIndex+"/"+pageTotal);
							if(pageIndex==1){
								$("#prepage").attr("disabled",true);
							}
							if(pageTotal == 1){
								$("#prepage").css("visibility","hidden");
								$("#nextpage").css("visibility","hidden");
									return;
								}
							}
		});
 }
 var dzhihui = 0;
 var fileArray = new Array();
 var fileIndex = -1;
 var fileNum = 0;
 function doupload()
 {
	 if(dzhihui==1)
	{
		return;
	}else
	{
		dzhihui=1;
	}
	 if(fileIndex == 4)
		{
		//alert("已上传5个文件，无法继续上传！");
			alert(getJsLocaleMessage('group','group_js_addGroupInfo_cannotupload'));
		 var filehtml = "<input type='file' name='drfile' id='drfile' onchange='doupload()' />";
		 $("#drfile").remove();
		 $("#fileBtn").before(filehtml);
		 dzhihui=0;
		return;
	}
 var fileName =$("#drfile").val();
 if(fileArray.toString().indexOf(fileName)>-1)
 {
	// alert("该文件已上传过，无需重复上传！");
	 alert(getJsLocaleMessage('group','group_js_addGroupInfo_reupload'));
	 var filehtml = "<input type='file' name='drfile' id='drfile' onchange='doupload()' />";
	 $("#drfile").remove();
	 $("#fileBtn").before(filehtml);
	 dzhihui=0;
	 return;
 }
	var index = fileName.lastIndexOf(".");
	var filetype= fileName.substring(index + 1).toLowerCase();
	if (filetype != "txt" && filetype != "xls"&& filetype != "xlsx") {
		//alert("上传文件格式错误，请选择txt或xls格式的文件。");
		alert(getJsLocaleMessage('group','group_js_addGroupInfo_fileformaterror'));
		dzhihui=0;
		return ;
	}
	//隐藏注释的信息
	$(".remindMsg").hide();
	//$("#upts").text("上传文件处理中，请稍候！");
	$("#upts").text(getJsLocaleMessage('group','group_js_addGroupInfo_waitupload'));
 $.ajaxFileUpload ({
    url: ipathh+'/grp_groupManage.htm?method=upload', //处理上传文件的服务端
    secureuri:false, 								//是否启用安全提交，默认为false
    fileElementId: 'drfile', 							//与页面处理代码中file相对应的ID值
    type:'POST',
    dataType: 'text', 								//返回数据类型:text，xml，json，html,scritp,jsonp五种
    success: function (data) {
	 dzhihui=0;
	 var datavalue=$.trim(data+"");
		if(datavalue=="")
		{
			//alert("上传文件内没有有效的数据！");
			alert(getJsLocaleMessage('group','group_js_addGroupInfo_illfiledata'));
			$("#upts").text("");
			dzhihui = 0;
		} else if(datavalue=="forbid"){
			//alert("文件中存在特殊字符！");
			alert(getJsLocaleMessage('group','group_js_editGroupInfo_illfilechar'));
			$("#upts").text("");
			dzhihui = 0;
		}else
		{
			daarry = data.split("&amp;");
			var dsize = daarry.length;
			if(dsize > 2001)
			{
				//alert("单个文件上传有效记录超过2000条，请重新选择文件上传！");
				alert(getJsLocaleMessage('group','group_js_addGroupInfo_filetoobig'));
				$("#upts").text("");
				dzhihui = 0;
				return;
			}
			fileIndex=fileIndex + 1 ;
			fileNum = fileNum +1;
			fileArray[fileNum] = fileName;
			fileName = fileName.substr(fileName.lastIndexOf("\\")+1,index);
			var fnstr = fileName;
			if(fileName.length>15)
			{
				fnstr = fileName.substr(0,12)+"...";
			}
			var beginsize = 0;
			var percount = 50;
			var exstr = "#";
			$("#right").find("p[pva2]").each(
				function(){
					exstr = exstr+$(this).attr("pva2")+"#";
				}
			);
			doloop(beginsize,percount,dsize,fileNum,exstr);
			$("#efile").append("<div filenum='"+fileNum+"' title='"+fileName+"' class='gp_filelist'><span>"+fnstr
					+"</span><a href='javascript:delFile("+fileNum+")'>" + getJsLocaleMessage('group','group_js_addGroupInfo_del') + "</a></div>");
			$("#numcount").text(countt);
		}
		$("#upts").text("");
 		}
	  });
 }
 var daarry = new Array();
 function doloop(beginsize,percount,dsize,fileNum,exstr)
{
	var countt = 0;
	var looper = percount;
	for(var d=beginsize;d<dsize && looper>0;d=d+1)
	{
		looper = looper - 1;
		var aaa = daarry[d];

		if(aaa!="" && aaa.length>11)
	{
		var isExist = false;
		isExist = (exstr.indexOf("#"+aaa+"#")>-1);
		if(!isExist)
		{
			var aary = aaa.split("|");
			getTypeStr(3,aaa);
			$("#right").append("<p filenum='"+fileNum+"' title='"+aary[0]+"' onclick='cliselp($(this))' "
					+" ondblclick='moveme($(this))' etype='3' pva='"+aaa+"' pva2='"+aaa+"'>"
					+"<span class='ld div_bd' >"+ aary[0]+"</span>"
					+"<span class='md div_bd' >"+ aary[1]+"</span>"
					+"<span class='rd div_bd' >" + getJsLocaleMessage('group','group_js_addGroupInfo_file') + "</span></p>");
			countt = countt +1;
		}/*else
		{
			var $rpv = $("#right").find("p[pva2='"+aaa+"']");
			if(!$rpv.hasClass("hoverColor"))
			{
				cliselp($rpv);
			}
		}*/
	}
}
beginsize=percount+beginsize;
var countta = parseInt($("#numcount").text());
$("#numcount").text(countta+countt);
if(beginsize<dsize)
{
	setTimeout("doloop("+beginsize+","+percount+","+dsize+","+fileNum+",'"+exstr+"')",100);
}else
{
	$("#upts").text("");
		 dzhihui=0;
	}
}
 function delFile(fnum)
 {
	 if(confirm(getJsLocaleMessage('group','group_js_addGroupInfo_delfileinfo')))
 {
	 var countt = parseInt($("#numcount").text());
	 $("#right p[fileNum="+fnum+"]").each(function(){
		 var pva = $(this).attr("pva");
		 var etype = $(this).attr("etype");
		 removeTypeStr(etype,pva);
		 $(this).remove();
		 countt = countt - 1;
	 });
	 $(".gp_filelist[fileNum="+fnum+"]").remove();
	 fileIndex = fileIndex -1;
	 fileArray[fnum] = "";
	 $("#numcount").text(countt);
	 }
 }
 function setcount()
 {
	 $("#numcount").text($("#count").val());
 }
 
 
 // 处理按纽上一页  //
function prePage(){
    var tempVal="";
	var pageIndex =  parseInt($("#pageIndexAdd").val());
	var chooseType = $("#chooseType").val();
	var depId = $("#depId").val();
	var epname = $("#epname").val();
	var lgcorpcode=$(window.parent.document).find("#lgcorpcode").val();
    var lguserid=$(window.parent.document).find("#lguserid").val();
	$("#nextpage").attr("disabled",false);
	// 选择的类型 //
	pageIndex = pageIndex - 1;
	$("#pageIndexAdd").val(pageIndex);
	$("#left").empty();
	// 方法名  //
	var methodName ;
	//用户 查询群组的时候 表识 1 员工群组  2客户群组  //
	var type = 1;
	if(chooseType == "1"){
		methodName = "getDepAndEmpTree1";
	}else if(chooseType == "2"){
		methodName = "getGroupUserByGroupId";
		$("#groupList option:selected").each(function() 
		{
		 	tempVal = tempVal+$(this).val()+",";
		});
	}
	// 处理员工/群组的上一页的操作 //
		//处理机构是否重复       isDep  1表示是员工机构      2表示是客户机构     3表示是群组//
		$.post(ipathh+"/grp_groupManage.htm", {method:methodName,lguserid:lguserid, depId:depId,udgId:tempVal,epname:epname,lgcorpcode:lgcorpcode,pageIndex:pageIndex,type:type}, function(result){
						$("#pagecode").empty();
						if(result != "" ){
							// 获取分页信息 //
							var arr = result.substring(0,result.indexOf("#")).split(",");
							//总页数 //
							var pageTotal = parseInt(arr[0]);
							//总记录数 //
							var pageRec = arr[1];
							// 添加记录 //
							$("#left").html(result.substring(result.indexOf("#")+1));
							// 显示当前机构 //
							$("#pagecode").html(pageIndex+"/"+pageTotal);
							// 当总页数等于当前页时 ，下一页置灰//
							if(1 == pageIndex){
								$("#prepage").attr("disabled",true);
								return;
							}
						}
		});
}
// 处理按纽 下一页 //
function nextPage(){
	var tempVal="";
	var pageIndex =  parseInt($("#pageIndexAdd").val());
	$("#prepage").attr("disabled",false);
	// 选择的类型 //
	//var chooseType = $("input:radio[name='dept']:checked").attr("value");
	var chooseType = $("#chooseType").val();
	var epname = $("#epname").val();
	var lgcorpcode=$(window.parent.document).find("#lgcorpcode").val();
    var lguserid=$(window.parent.document).find("#lguserid").val();
	var depId = $("#depId").val();
	pageIndex = pageIndex + 1;
	$("#pageIndexAdd").val(pageIndex);
	$("#left").empty();
	// 方法名  //
	var methodName ;
	//用户 查询群组的时候 表识 1 员工群组  2客户群组  //
	var type = 1;
	if(chooseType == "1"){
		methodName = "getDepAndEmpTree1";
	}else if(chooseType == "2"){
		methodName = "getGroupUserByGroupId";
		$("#groupList option:selected").each(function() 
		{
		 	tempVal = tempVal+$(this).val()+",";
		});
	}
		//处理机构是否重复       isDep  1表示是员工机构      2表示是客户机构     3表示是群组//
		$.post(ipathh+"/grp_groupManage.htm", {method:methodName,lguserid:lguserid,depId:depId,udgId:tempVal,epname:epname,lgcorpcode:lgcorpcode,pageIndex:pageIndex,type:type}, function(result){
						$("#pagecode").empty();
						if(result != "" ){
							// 获取分页信息 //
							var arr = result.substring(0,result.indexOf("#")).split(",");
							//总页数 //
							var pageTotal = parseInt(arr[0]);
							//总记录数 //
							var pageRec = arr[1];
							// 添加记录 //
							$("#left").html(result.substring(result.indexOf("#")+1));
							// 如果是只有一页记录的话//
							$("#pagecode").html(pageIndex+"/"+pageTotal);
							// 当总页数等于当前页时 ，下一页置灰//
							if(pageTotal == pageIndex){
								$("#nextpage").attr("disabled",true);
									return;
								}
							}
			});
	}
 



//移出主界面的隐藏域信息
function removeTypeStr(etype,tempVal)
{
	if(etype == 1 || etype=="1")
	{
		var ygStr = $("#ygStr").val();
		ygStr = ygStr.replace(","+tempVal+"," , ",");
		$("#ygStr").val(ygStr);
	}
	if(etype == 2 || etype=="2")
	{
		var qzStr = $("#qzStr").val();
		qzStr = gxStr.replace(","+tempVal+"," , ",");
		$("#qzStr").val(qzStr);
	}
	if(etype == 3 || etype=="3")
	{
		var zjStr = $("#zjStr").val();
		zjStr = zjStr.replace(","+tempVal+"," , ",");
		$("#zjStr").val(zjStr);
	}
	if(etype == 4 || etype=="4")
	{
		var gxStr = $("#gxStr").val();
		gxStr = gxStr.replace(","+tempVal+"," , ",");
		$("#gxStr").val(gxStr);
	}
}
 