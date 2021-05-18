

//
function showAddAccountTmp()
{
		var pathUrl = $("#pathUrl").val();
        window.location.href=pathUrl+"/app_acctmanager.htm?method=doAdd&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;	
}
//
function showEditAcctTmp(tmId)
{
	var pathUrl = $("#pathUrl").val();
	window.location.href=pathUrl+"/app_acctmanager.htm?method=doEdit&tmId="+tmId+"&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;

}

//返回首页
function showMain()
{
	var pathUrl = $("#pathUrl").val();
	window.location.href=pathUrl+"/app_acctmanager.htm?method=find&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;	

}
//判断ip地址的合法性
function checkIP(value){
    var exp=/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
    var reg = value.match(exp);
    if(reg==null)
    {
    	//alert("通讯IP不正确！");
		alert(getJsLocaleMessage('appmage','appmage_js_accountList_iperror'));
    	return "false";
    }
    return "true";
}

function back()
{
	var pathUrl = $("#pathUrl").val();
  	location.href=pathUrl+"/app_acctmanager.htm?method=find&lguserid="+$("#lguserid").val()+"&lgcorpcode="+$("#lgcorpcode").val();
}

function saveData(){
	var fakeid = $("#fakeid").val();
	var name=$("#name").val();
	var appAccount=$("#code").val();
	var ip=$("#ip").val();
	var port=$("#port").val();
	var password=$("#password").val();
	var fileSvrUrl=$("#fileSvrUrl").val();
	if (name=="") {
		//alert("请输入企业名称必填项！");
		alert(getJsLocaleMessage('appmage','appmage_js_accountList_enterbusname'));
		$("#name").focus();
		$("#save").attr("disabled",false);
		return;
	}else if(fakeid == ""){
		//alert("请输入企业编号必填项！");
		alert(getJsLocaleMessage('appmage','appmage_js_accountList_enterbuscode'));
		$("#code").focus();
		$("#save").attr("disabled",false);
		return;
	}else if(appAccount == ""){
		//alert("请输入APP账号必填项！");
		alert(getJsLocaleMessage('appmage','appmage_js_accountList_enterappaccount'));
		$("#appAccount").focus();
		$("#save").attr("disabled",false);
		return;
	}else if(password == ""){
		//alert("请输入账号密码必填项！");
		alert(getJsLocaleMessage('appmage','appmage_js_accountList_enteraccandpwd'));
		$("#password").focus();
		$("#save").attr("disabled",false);
		return;
	}else if(fileSvrUrl == ""){
		//alert("请输入文件服务器地址必填项！");
		alert(getJsLocaleMessage('appmage','appmage_js_accountList_enterserverpath'));
		$("#fileSvrUrl").focus();
		$("#save").attr("disabled",false);
		return;
	}else if(ip == ""){
		//alert("请输入IP必填项！");
		alert(getJsLocaleMessage('appmage','appmage_js_accountList_enterip'));
		$("#ip").focus();
		$("#save").attr("disabled",false);
		return;
	}
	var array = new Array();
	array.push(fakeid);
	array.push(appAccount);
	array.push(name);
	// 校验参数包含特殊字符，则不通过
	if(checkParams(array) == false) {
	    return;
    }
	
	if(checkIP(ip)=="false"){
		return;
	}
	if(port == ""){
		//alert("请输入端口必填项！");
		alert(getJsLocaleMessage('appmage','appmage_js_accountList_enterport'));
		$("#port").focus();
		$("#save").attr("disabled",false);
		return;
	}
	 if(port.length!=4){
			//alert("端口不正确！");
			alert(getJsLocaleMessage('appmage','appmage_js_accountList_porterror'));
			$("#ip").focus();
			$("#save").attr("disabled",false);
			return;
		}
	 $("#waitTextConnection").text("");
	 $("#errorTextConnection").text("");
	 $("#rightTextConnection").text("");
	// $("#waitTextConnection").text("APP网关正在登录到梦网APP平台，请稍候......");
	 $("#waitTextConnection").text(getJsLocaleMessage('appmage','appmage_js_accountList_waitlogin'));
	 $("#save").attr("disabled",true);
	document.forms[0].submit();

}

function checkParams(arrParams) {
    var patternStr = '<,>,",\',;';
    var arr = patternStr.split(',');
    for(var i = 0; i < arrParams.length; i++) {
        for(var j = 0; j < arr.length; j++) {
            if(arrParams[i] != null && arrParams[i] != undefined ) {
                if(arrParams[i].indexOf(arr[j]) != -1) {
                    alert(getJsLocaleMessage('appmage','appmage_smstask_text_7'));
                    return false;
                }
            }
        }
    }
}


function testConn(){
	var urlip= $("#ip").val();
	var port= $("#port").val();
	var fileSvrUrl=$("#fileSvrUrl").val();
	if(fileSvrUrl == ""){
		//alert("请输入文件服务器地址必填项！");
		alert(getJsLocaleMessage('appmage','appmage_js_accountList_enterserverpath'));
		$("#fileSvrUrl").focus();
		$("#save").attr("disabled",false);
		return;
	}
	if(urlip == ""){
		//alert("请输入IP必填项！");
		alert(getJsLocaleMessage('appmage','appmage_js_accountList_enterip'));
		$("#ip").focus();
		$("#save").attr("disabled",false);
		return;
	}
	if(port.length==0){
		//alert("请输入端口必填项！");
		alert(getJsLocaleMessage('appmage','appmage_js_accountList_enterport'));
		$("#ip").focus();
		$("#save").attr("disabled",false);
		return;
	}
	if(port.length!=4){
		//alert("端口不正确！");
		alert(getJsLocaleMessage('appmage','appmage_js_accountList_porterror'));
		$("#ip").focus();
		$("#save").attr("disabled",false);
		return;
	}
	if(checkIP(urlip)=="false"){
		return;
	}

	
	document.getElementById("errorTextConnection").innerHTML = "";
	document.getElementById("rightTextConnection").innerHTML = "";
	//$("#waitTextConnection").text("正在测试连接，请稍候......");
	$("#waitTextConnection").text(getJsLocaleMessage('appmage','appmage_js_accountList_waitconn'));
	var pathUrl = $("#pathUrl").val();
	 			$.ajax({
 		        url: pathUrl+'/app_acctmanager.htm',
 		        data:{
 		            method:'testConn',
		    	 	urlip:urlip,
		    	 	fileSvrUrl:fileSvrUrl,
					port:port
 		        },
 		        type: 'post',
 		        error: function(){
					$("#waitTextConnection").text("");
					document.getElementById("rightTextConnection").innerHTML = "";
					document.getElementById("errorTextConnection").innerHTML = getJsLocaleMessage('appmage','appmage_js_accountList_connecterror'); //"连接异常，请检查网络连接!";	
 		        },
 		        success: function(r){
                    if((r!=null&&r=="ipError")||r=="")
                    {
    					$("#waitTextConnection").text("");
    					document.getElementById("rightTextConnection").innerHTML = "";
    					document.getElementById("errorTextConnection").innerHTML = getJsLocaleMessage('appmage','appmage_js_accountList_checkipandport'); //"连接失败，请检查通讯IP与端口是否正确!";

                    }else if(r!=null&&r=="fileError"){
    					$("#waitTextConnection").text("");
    					document.getElementById("rightTextConnection").innerHTML = "";
    					document.getElementById("errorTextConnection").innerHTML = getJsLocaleMessage('appmage','appmage_js_accountList_checkserverpath'); //"连接失败，请检查文件服务器地址是否正确!";
                    }else{
    					$("#waitTextConnection").text("");
    					document.getElementById("errorTextConnection").innerHTML = "";
    					document.getElementById("rightTextConnection").innerHTML = getJsLocaleMessage('appmage','appmage_js_accountList_connsuc'); //"连接成功!";
                    }
 		        }
 		    });
	
}

function eblur(ob) 
{
       		if (ob.is("#contents")) 
           	{
       			ob.val($.trim(ob.val()));
       			var content = $("#contents").val();
       			var huiche = content.length - content.replaceAll("\n", "").length;
       			if (content.length + huiche > 990) {
       				$("#contents").val(content.substring(0, 990 - huiche));
       			}
       		}
}
function upload(id){
var imgtem=$("#"+id).val();
	if(imgtem.length<1){
	return ;	
}else{
	var c = ".jpg|.jpeg|.gif|.bmp|.png|";
    var b = imgtem.substring(imgtem.lastIndexOf("."),imgtem.length).toLowerCase();
    if(c.indexOf(b)<0){
        //alert("请上传.jpg、.jpeg、.gif、.bmp、.png类型的图片！");
		alert(getJsLocaleMessage('appmage','appmage_js_accountList_uploadformat'));
        resetfileinput('#'+id);
        return;
    }
}
var size=1024;
var index=0;
   $.ajaxFileUpload({
       url: "app_acctmanager.htm?method=uploadImg", //需要链接到服务器地址
       secureuri: false,
       fileElementId: id,
       data:{'size':size},
       success: function (data, textStatus) {               //相当于java中try语句块的用法
           if('success'==textStatus){
        	   var url=$(data).text();
              if(url.indexOf("overSize")!=-1){
                  //alert("文件尺寸超过50像素*50像素！");
				  alert(getJsLocaleMessage('appmage','appmage_js_accountList_filesize'));
              }else{
                  var $upload=$("#"+id);
                  var $pre=$upload.parents("form").find(".img");
                  $upload.siblings().first().val(url);
                  $upload.next().val(url);
                  $pre.eq(index).children("img").removeAttr('src').removeAttr('width').removeAttr('height');
                  $pre.eq(index).children("img").attr('src',url).show().siblings().hide();
                  $("#company_logo").val(url);
              	}
           	}
       },
       error: function (data, status, e) {           //相当于java中catch语句块的用法
       	//alert("上传图片失败！");
		alert(getJsLocaleMessage('appmage','appmage_js_home_uploadimagefalse'));
       },
       complete: function(){
    	   resetfileinput('#'+id);
       }
   });
   $("#"+id).val("");
}

function resetfileinput(obj){
	 $(obj).wrap("<form></form>");
    var $form = $(obj).parent();
    $form[0].reset();
    $(obj).unwrap();
}

function modify(t)
{
	$('#modify').dialog({
	autoOpen: false,
	width:250,
    height:200
});
$("#msg").empty();
$("#msg").text($(t).children("label").children("xmp").text());
$('#modify').dialog('open');
}