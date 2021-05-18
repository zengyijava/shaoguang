function show(result,repeatPath,str){
	var path = $("#pathUrl").val();
	var $but = $(window.parent.document).find("#button1");
	$but.prev().remove();
	if(result=="true"){
		//alert("操作成功！");
		alert(getJsLocaleMessage('client','client_common_optsuc'));
		$("#button3").attr("disabled","");
		window.location.href = "cli_addrBook.htm?method=find&lguserid="+$("#lguserid").val()+"&lgcorpcode="+$("#lgcorpcode").val();
	}else if ( result.indexOf("upload") == 0 || result=="noRecord"){
		$("#button3").attr("disabled","");
		if(repeatPath!=null)
		{
			$.post("common.htm?method=checkFile", {url : repeatPath, upload:0 },function(result) {
				
				if (result.indexOf("true") == 0) {
                src = result.substring(4)||src;
                src = src.replace(/^@/,'');
				window.showModalDialog(src.replace(/\/$/,"") + "/" + repeatPath +"?Rnd="+ Math.random());
				//window.open(src + "/" + url,"_blank");
			} else if (result == "false")
				//alert("文件不存在");
				alert(getJsLocaleMessage('client','client_js_addClient_fileisnull'));
			else
				//alert("出现异常,无法跳转");
				alert(getJsLocaleMessage('client','client_common_jumperror'));
				$("#button3").attr("disabled","");
//				window.location.href = "cli_addrBook.htm?method=find&lguserid="+$("#lguserid").val()+"&lgcorpcode="+$("#lgcorpcode").val();
			});
		}else{
			$("#button3").attr("disabled","");
//			window.location.href = "cli_addrBook.htm?method=find&lguserid="+$("#lguserid").val()+"&lgcorpcode="+$("#lgcorpcode").val();
		}
	
	}else if(result=="false"){
		//alert("操作失败！");
		alert(getJsLocaleMessage('client','client_common_optfalse'));
		$("#button3").attr("disabled","");
	}else if(result=="errorXls"){
		//alert("无法解析的xls格式的文件，请上传正确的xls格式文件！");
		alert(getJsLocaleMessage('client','client_js_addClient_cannotparexls'));
		$("#button3").attr("disabled","");
	}else if(result!=null && result == "emptype"){
		//alert("上传文件中属性列不匹配，请按照模板上传！");
		alert(getJsLocaleMessage('client','client_js_addClient_fileattrerror'));
		$("#button3").attr("disabled","");
	}else if(result!=null && result == "overMax"){
		//alert("每次2007版本的EXCEL上传不能超过20万条！");
		alert(getJsLocaleMessage('client','client_js_addClient_filetoobig'));
		$("#button3").attr("disabled","");
	}else if(result=="isexist"){
		alert(str);
		$("#button3").attr("disabled","");
	}
}
var zTree1;//uploadFrom
var zTree2;//addForm
var setting;
var formName = "addForm";
setting = {
		checkable : true,
	    checkStyle : "checkbox",
    checkType : { "Y": "s", "N": "s" },
	async : true,
	asyncUrl:"",
	isSimpleData: true,
	rootPID : -1,
	treeNodeKey: "id",
	treeNodeParentKey: "pId",
	callback: {
		change: zTreeOnClick,
		asyncSuccess:function(event, treeId, treeNode, msg){
		if(!treeNode){	//判断是 顶级机构就展开,其余的收缩+	
			if(formName == "addForm"){
				 var rootNode2 = zTree2.getNodeByParam("level", 0);
	             zTree2.expandNode(rootNode2, true, false);
			}else if(formName == "uploadForm"){
				 var rootNode1 = zTree1.getNodeByParam("level", 0);
	             zTree1.expandNode(rootNode1, true, false);
			}				  
		}
		},
		beforeAsync:function(treeId,treeNode){
			var lguserid = $('#lguserid').val();
			if(formName == "addForm"){
				zTree2.setting.asyncUrl="cli_addrbookDepTree.htm?method=getSecondDepNoUnknow&lguserid="+lguserid+"&depId="+treeNode.id;
			}else if(formName == "uploadForm"){
				zTree1.setting.asyncUrl="cli_addrbookDepTree.htm?method=getSecondDepNoUnknow&lguserid="+lguserid+"&depId="+treeNode.id;
				}
			}
		}
};

var zNodes =[];

function showMenu(fname) {
	var isIE = (document.all) ? true : false;
	 var isIE6 = isIE && (navigator.appVersion.match(/MSIE 6.0/i)=="MSIE 6.0" ? true : false);
if(isIE6){
 	$("select ").hide();
 	$("#sex ").show();
 }
 	
$("form[name='"+fname+"']").find("#dropMenu").focus();
var sortSel = $("form[name='"+fname+"']").find("#depNam");
var sortOffset = $("form[name='"+fname+"']").find("#depNam").offset();
$("form[name='"+fname+"']").find("#dropMenu").toggle();

setFormName(fname);
var lguserid = $('#lguserid').val();
if(formName == "addForm")
{	
	setting.asyncUrl="cli_addrbookDepTree.htm?method=getSecondDepNoUnknow&lguserid="+lguserid;
	zTree2 = $("form[name='addForm']").find("#dropdownMenu").zTree(setting, zNodes);
	$("form[name='uploadForm']").find("#dropMenu").hide();
}
else if(formName == "uploadForm")
{
	setting.asyncUrl="cli_addrbookDepTree.htm?method=getSecondDepNoUnknow&lguserid="+lguserid;
	zTree1 = $("form[name='uploadForm']").find("#dropdownMenu").zTree(setting, zNodes);
	$("form[name='addForm']").find("#dropMenu").hide();
	}
}

function hideMenu() {
	var isIE = (document.all) ? true : false;
	 var isIE6 = isIE && (navigator.appVersion.match(/MSIE 6.0/i)=="MSIE 6.0" ? true : false);
if(isIE6){
 	$("select").show();
 }
$("form[name='"+formName+"']").find("#dropMenu").hide();
}

function zTreeOnClick(event, treeId, treeNode) {
	if (treeNode.isParent) {
		if(formName == "addForm"){
		zTree2.setting.asyncUrl="cli_addrbookDepTree.htm?method=getClientSecondDepJson&lguserid="+$("#lguserid").val()+"&depId="+treeNode.id;
	}else if(formName == "uploadForm"){
		zTree1.setting.asyncUrl="cli_addrbookDepTree.htm?method=getClientSecondDepJson&lguserid="+$("#lguserid").val()+"&depId="+treeNode.id;
	}
}
if (treeNode) {
	var zTreeNodes;
	if(formName == "addForm"){
		zTreeNodes = zTree2.getChangeCheckedNodes();
	}else if(formName == "uploadForm"){
		zTreeNodes = zTree1.getChangeCheckedNodes();
	}
	var pops="";
	var userids = "";
	for(var i=0; i<zTreeNodes.length; i++){

		pops+=zTreeNodes[i].name+";";
		userids+=zTreeNodes[i].id+",";
	}

	var sortName = $("form[name='"+formName+"']").find("#depNam");
	sortName.attr("value", pops);
	var sortId = $("form[name='"+formName+"']").find("#depId")
	sortId.attr("value",userids);
	if(zTreeNodes.length==0){
		 sortName.attr("value","");
		 sortId.attr("value", "");
		}
		
	}
}
function setFormName(name){
	formName = name;
}
function reloadTree() {
	hideMenu();
	zTree2 = $("form[name='addForm']").find("#dropdownMenu").zTree(setting, zNodes);
}	
$(document).ready(function(){
	cliseTreeClient();
	getLoginInfo("#hiddenValueDiv");
	reloadTree();
	$("#s_ud_s_list li").each(function(index){
		$(this).click(function(){
			$("div.block").removeClass("block");
			$("#tab_menu > div").addClass("hidden");
			$("li.s_ud_tabin").removeClass("s_ud_tabin");
			$("#tab_menu > div").eq(index).addClass("block");
			$("#tab_menu > div").eq(index).removeClass("hidden");
			$(this).addClass("s_ud_tabin");
		});
	});
//show();
});
function doUpload(){
	var uploadFile = $("#uploadFile").attr("value") ;
var depId = $("form[name='"+formName+"']").find("#depId").attr("value");
if (depId==""){
	//alert("所属机构不能为空！");
	alert(getJsLocaleMessage('client','client_js_addClient_selectorgisnull'));
	$("form[name='"+formName+"']").find("#depId").focus();
}else if(uploadFile.length == 0){//非空检查
	//alert("没有选择导入文件，请导入Excel文件！") ;
	alert(getJsLocaleMessage('client','client_js_addClient_importexcelfile'));
	return false ;
}else if(uploadFile.substring(uploadFile.lastIndexOf('.')+1)!="xls"&&uploadFile.substring(uploadFile.lastIndexOf('.')+1)!="xlsx"){
	//检查文件格式是否是.xls格式
	//alert("不支持此种文件类型，请导入后缀名为.xls和.xlsx的文件！") ;
	alert(getJsLocaleMessage('client','client_js_addClient_nosuporttype'));
	return false ;
}else{
	 $("#button1").parent().prepend("<font style='margin-right:25px;'>"+getJsLocaleMessage('client','client_js_addClient_waitupload')+"</font>");
	$("#button1").attr("disabled","disabled");
	$("#uploadForm").attr("action",$("#uploadForm").attr("action")+"&lgcorpcode="+$("#lgcorpcode").val()+"&lguserid="+$("#lguserid").val());
	$("#uploadForm").submit();
	}
}
function zTreeOnClickOK() {
	$("form[name='"+formName+"']").find("#dropMenu").hide();
}
//清空选择
function cleanSelect() {
	var checkNodes ;
	if(formName == "addForm"){
		checkNodes = zTree2.getCheckedNodes();
	}else if(formName == "uploadForm"){
		checkNodes = zTree1.getCheckedNodes();
	}
	for ( var i = 0; i < checkNodes.length; i++) {
		checkNodes[i].checked = false;
	}
	if(formName == "addForm"){
		zTree2.refresh();
	}else if(formName == "uploadForm"){
		zTree1.refresh();
	}
	$("form[name='"+formName+"']").find("#depNam").attr("value",getJsLocaleMessage('client','client_js_addClient_clickselectorg'));//"点击选择机构");
	$("form[name='"+formName+"']").find("#depId").attr("value","");
}

//点击别处关闭树(由于添加客户的页面的两个数的id一致，导致common.js的公共关闭方法只能定位到第一个id树的位置，所以这里写个特殊方法)
function cliseTreeClient()
{
	var treeObjAdd = $("form[name='addForm']").find("#dropMenu");
	var treeObjUpl = $("form[name='uploadForm']").find("#dropMenu");
	
	treeObjAdd.click(function(e){
		e.stopPropagation();
	});
	treeObjUpl.click(function(e){
		e.stopPropagation();
	});
	
	$('html,body').click(function(e){
		var $obj=$(e.target);
        if($obj.attr("class").indexOf("treeInput")==-1)
        {
        	treeObjAdd.css("display","none");
        	treeObjUpl.css("display","none");
        }
   });
}

//弹出导出界面
function exportAll()
{
	$("#exportDiv").css("display","block");
	$('#exportDiv').dialog({
		autoOpen: false,
		height: 160,
		width: 340,
		resizable:false,
		modal:true
	});
	$('#exportDiv').dialog('open');
}