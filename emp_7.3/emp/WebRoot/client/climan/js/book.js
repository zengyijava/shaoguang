var docUrl = document.URL;
var urls = docUrl.split("/");
docUrl = urls[0] + "//" + urls[2] + "/" + urls[3];
var inheritPath;
var time;
var dep;
var depName;
var depId;
//**********兼容IE6，IE7，IE8，火狐浏览器模态对话框的宽高
var isIE = (document.all) ? true : false;
var isIE6 = isIE && (navigator.appVersion.match(/MSIE 6.0/i)=="MSIE 6.0" ? true : false);
var app=navigator.appName;
var ieWidth = 350;
var ieHeight = 440;

if (isIE6)
{
	ieWidth = 360;
	ieHeight = 470;
}
if (app == "Netscape")
{
	ieWidth = 355;
	ieHeight = 447;
}


function showTree(departId,tmethod)
{
	inheritPath=$('#inheritPath').val();
 	time = new Date();
	dep = window.showModalDialog( inheritPath+"/a_clientDepTree1.jsp?treemethod="+tmethod+"&timer="+time+"&isReturnDepId=false&departId="+departId,"","dialogWidth="+ieWidth+"px;dialogHeight="+ieHeight+"px;help:no;status:no;center:yes");
	if (dep == null)
	{
		dep="&";
		depName = getJsLocaleMessage('client','client_js_book_all'); //"全部";
		depId = "";
	}
    else
    {
    	depName = dep.split("&")[0];
    	depId = dep.split("&")[1];
    }
	
}

$(function(){
$("#all").click(function(e){
	$("#depNam").val(getJsLocaleMessage('client','client_js_book_all'));
	$("#depName").val("");
});
$("#cdep").click(function(e){
	var departId = $("#depId").val() == ""? 0:$("#depId").val();
 var tmethod = $("#tmethod").val();
	showTree(departId,tmethod);
	$("#depNam").val(depName);
	$("#depId").val(depId);
	$("#domdepName").attr("value",depName);
	$("#domString").attr("value",depId);
});
});

var treeWay="";
var deptree=document.getElementsByName("tinytreeInputName");

//复选框多选
function checkAll(e,str)    
{    
	var a = document.getElementsByName(str);    
	var n = a.length;    
	for (var i=0 ; i < n ; i++ ){   
		a[i].checked =e.checked;
	}
}

//按照机构搜索信息
function getDepInfo(nodeId){
	location.href="getInfoByDep.action?depId="+nodeId;
}

//获取选中节点的id集合，返回的id集合格式为 id1,id2,id3
function getNodeId() {
	var id="";
	var result=$('#com_abjust_list')[0].tinytree.getSelecetedNodes();
	$.each(results, function(i, result) {
		ids += result.nodeId+",";
	});
	ids=ids.substring(0,ids.lastIndexOf(','));
	return id;
}

//把链接好的字符串分解为数组
function getNodeIdByString(inString){
	var str = inString.split(",");
	var nodeId = new Array();
	for(var i = 0 ;i < str.length ; i++){
		nodeId[i] = parseInt(str[i]);
	}
	return nodeId;
}

//返回树的结点ID
function returnId(){
	var results=$('#com_abjust_list')[0].tinytree.getSelecetedNodes();
	//alert(results[0].nodeId);
	if(treeWay == "dom"){
		$("#domString").val(results[0].nodeId);
	}else if(treeWay == "priv"){
		$("#privListString").val(results[0].nodeId);
	}
	document.getElementById("com_add_Dom").style.display="none";
	document.getElementById("iframeshow2").style.display="none";
}

//用List的ID拼接字符串
function splitString(list){
	var idString="";
	for(var i = 0;i<list.length;i++)
    {
		idString += list[i] + ",";
    }
	idString=idString.substring(0,idString.lastIndexOf(','));
	return idString;
}


//处理新增修改客户
function doSubClient()
{		
		//如果姓名或电话或者机构任何一个修改了 ，此属性就设为true，后台就要进行重复验证，如果都没有变则不需要验证重复性
	   var bookType=$("#bookType").val();
	   var cName = $.trim($("#cName").val());
	   var cNameTemp = $.trim($("#cNameTemp").val());
	   var mobile = $.trim($("#mobile").val());
	   var mobileTemp = $.trim($("#mobileTemp").val());
	   var depId = $("#depId").val();
	   var depIdTemp = $("#depIdTemp").val();
	   var checkClient = $("#checkClient").val();
	   var clientNo = $.trim($("#clientCode").val());
	   var email = document.getElementById("EMail").value;
	   email=email.replace(/(^\s*)|(\s*$)/g, "");//去除两端空格
	   var lgcorpcode = $("#lgcorpcode").val();
	   //add  edit
	   var actionType = $("#actionType").val();
	   var clientid = "";
	   if(actionType == "edit"){
		   clientid = $("#bookId").val();
	   }
	   if(email != ""){
	        //对电子邮箱的验证
	        var myreg =  /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
	        if(!myreg.test(email))
	        {
	             //alert('邮箱格式不正确');
	        	alert(getJsLocaleMessage('client','client_js_book_emailerror'));
	            $("#EMail").focus();
	            return;
	        }  
	   }
	   if (cName == "")
	   {
		  //alert("姓名不能为空！");
		   alert(getJsLocaleMessage('client','client_js_book_nameisnull'));
		  $("#cName").focus();
		  return;
	   }
	   else  if (outSpecialChar(cName))
	   {
			  //alert("姓名含有非法字符！");
		   alert(getJsLocaleMessage('client','client_js_book_nameformaterror'));
			  $("#cName").focus();
			  return;
	   }
	   else if (depId == "")
	   {
		  // alert("所属机构不能为空！");
		   alert(getJsLocaleMessage('client','client_js_addClient_selectorgisnull'));
		   return;
	   }
	  else if (mobile == "")
	   {
		  // alert("手机不能为空！");
		  alert(getJsLocaleMessage('client','client_js_book_phoneisnull'));
		   $("#mobile").focus();
		   return;
	   }
	   else if (mobile!="" && !checkPhone(mobile))
	   {
		   //alert("手机号码不正确！");
		   alert(getJsLocaleMessage('client','client_js_book_phoneformaterror'));
		   $("#mobile").focus();
		   return;
	   }
	   else
	   {
		   if(mobile.indexOf("*")>-1){
			   $("#mobile").val(mobileTemp);
			   mobile=mobileTemp;
		   }
		   $("#button").attr("disabled","disabled");
		   $.post(checkClient,{mobile : mobile,lgcorpcode:lgcorpcode,actionType:actionType,clientid:clientid,name:cName,depId:depId},function(result){
			   if(result.indexOf("html") > 0){
				   //alert("登录超时，请重新登录！");
				   alert(getJsLocaleMessage('client','client_js_book_logintimeout'));
				   window.location.href = window.location.href;
				   return;
			   }else if(result != null && result == "numfalse")
			   {
				   //alert("手机号码格式不正确！");
				   alert(getJsLocaleMessage('client','client_js_book_phoneformatill'));
				   $("#button").attr("disabled","");
				   return;
			   }else if(result != null && (result == "numtrue" || result == "isexistnum")||result == "updateExist"){
				   var message = "";
				   if(result == "isexistnum"){
					   message = getJsLocaleMessage('client','client_js_book_phoneisexcite'); //"该手机号码已被其它客户使用，是否继续添加？";
				   }
				   if(result == "updateExist"){
					   //alert("该机构下已存在姓名、手机号码相同的客户，不允许修改！");
					   alert(getJsLocaleMessage('client','client_js_book_donotmidify'));
					   $("#button").attr("disabled","");
					   return;
				   }
				   if(message == ""){
					   $("#addForm").attr("action",$("#addForm").attr("action")+"&lguserid="+$("#lguserid").val()+"&lgcorpcode="+lgcorpcode);
					   $("#addForm").submit();
				   }else if(message != ""){
					   if (confirm(message)){
						   $("#addForm").attr("action",$("#addForm").attr("action")+"&lguserid="+$("#lguserid").val()+"&lgcorpcode="+lgcorpcode);
						   $("#addForm").submit();
					   }else{
						   $("#button").attr("disabled","");
						   return;
					   }
				   }
			   }
		   })
	   }
}

function changeinfo(type)
{
    if(type=='1')
    {
        $("#addone").removeClass();
        $("#addall").removeClass();
        $("#addone").addClass("infotd1");
        $("#addall").addClass("infotd2");
        
        $("#addone").addClass("dxzs_addone");
        
        $("#addoneDiv").show();
        $("#addallDiv").hide();
    }
    else
    {
        $("#addone").removeClass();
        $("#addall").removeClass();
        $("#addall").addClass("infotd1");
        $("#addone").addClass("infotd2");
        
        $("#addall").addClass("dxzs_addall");
        
        $("#addallDiv").show();
        $("#addoneDiv").hide();
    }
}

//关闭树的下拉框方法（点击别处时关闭）+关闭时显示下拉框
function closeTreeFunSelBook()
{
	$(".tree").parent().click(function(e){
		e.stopPropagation();
	});
	
	$('html,body').click(function(e){
		var $obj=$(e.target);
        if($obj.attr("class").indexOf("treeInput")==-1){
        	$(".tree").parent().css("display","none");
        	$("select").css("visibility","");
      }
   });
}

//下载2003版客户通讯录的模板
function export2003Excel(langName){
	var lgcorpcode=$('#lgcorpcode').val();
	var path =$('#pathUrl').val();
	var hef=path+"/down.htm?filepath=client/climan/file/temp/"+lgcorpcode+"clientexam_"+langName+".xls";
	window.IE_UNLOAD  = true;
	location.href= hef;
}

//下载2007版客户通讯录的模板
function export2007Excel(langName){
	var lgcorpcode=$('#lgcorpcode').val();
	var path =$('#pathUrl').val();
	var hef=path+"/down.htm?filepath=client/climan/file/temp/"+lgcorpcode+"clientTemplate_"+langName+".xlsx";
	window.IE_UNLOAD  = true;
	location.href= hef;
}
