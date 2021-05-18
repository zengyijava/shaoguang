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
		depName = "全部";
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
	$("#depNam").val("全部");
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

function doSub()
{
	var bookType=$("#bookType").val();
   var cName = $.trim($("#cName").val());
   var mobile=  $.trim($("#mobile").attr("value"));
	if($("#tempMobile").attr("value")){
		mobile = $("#tempMobile").attr("value");
	}
	if(mobile.indexOf("*") > 0){
		//如果含有星号
		mobile = $("#mobile").attr("value");
	}else{
		$("#mobile").val(mobile);
	}
   var depId = $("#depId").val();
   var checkUrl = $("#checkUrl").val();
   var employeeNo = $.trim($("#employeeNo").val());
   var clientNo = $.trim($("#clientCode").val());
   var email = document.getElementById("EMail").value;
   email=email.replace(/(^\s*)|(\s*$)/g, "");//去除两端空格
   if(email!=""){
            //对电子邮箱的验证
            var myreg =  /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
            if(!myreg.test(email))
            {
                 alert('邮箱格式不正确');
                $("#EMail").focus();
                return;
            }  
   }
   
   if (cName == "")
   {
	  alert("姓名不能为空！");
	  $("#cName").focus();
	  return;
   }
   else  if (outSpecialChar(cName))
   {
		  alert("姓名含有非法字符！");
		  $("#cName").focus();
		  return;
   }
   else  if (bookType == "employee" && employeeNo == "")
   {
		  alert("工号不能为空！");
		  $("#employeeNo").focus();
		  return;
   }
   /*else  if (bookType == "client" && clientNo == "")
   {
		  alert("客户号不能为空！");
		  $("#employeeNo").focus();
		  return;
   }*/
   else if (depId == "")
   {
	   alert("所属机构不能为空！");
	   return;
   }
  else if (mobile == "")
   {
	   alert("手机不能为空！");
	   $("#mobile").focus();
		  return;
   }
   else if (mobile!="" && mobile.length != 11)
   {
	   alert("手机号码不正确！");
	   $("#mobile").focus();
		  return;
   }
   else
   {
	   
	   $("#button").attr("disabled","disabled");
	   $.post(checkUrl,{mobile : mobile,eNo:employeeNo,cNo:clientNo,name:cName,depId:depId,hidOpType:$("#hidOpType").val(),depCode:depId},function(result){
		   if(result.indexOf("html") > 0){
			   alert("登录超时，请重新登录！");
			   window.location.href = window.location.href;
			   return;
		   }else if(result != null && result == "numfalse")
		   {
			   alert("手机号码格式不正确！");
			   $("#button").attr("disabled","");
			   return;
		   }
		   else if(result != null && result != "numfalse" && result != "true" && result != "phoneExist"&&result!="phoneAndNameExist")
		   {
			   $("#button").attr("disabled","");
			   if(result.length > 0){
				   alert(result);
			   }else{
				   alert("请检查网络/数据库连接是否正常！");
			   }
			   return;
		   }else if(result == "true"){
			    $("#addForm").attr("action",$("#addForm").attr("action")+"&lguserid="+$("#lguserid").val()+"&lgcorpcode"+$("#lgcorpcode").val());
				$("#addForm").submit();
		   }
		   else if( result == "phoneExist"||result=="phoneAndNameExist"){
			   var message = "";
			   if(result=="phoneAndNameExist"){
				   alert("该机构下存在姓名和手机号码都相同的员工，不允许添加！");
				   $("#button").attr("disabled","");
				   return;
			   }
			   if(result == "phoneExist")
			   {
				   message="该机构下存在姓名不同，手机号码相同的员工，您是否添加？"
			   }
			   
			   if (confirm(message))
			   {
				   $("#addForm").attr("action",$("#addForm").attr("action")+"&lguserid="+$("#lguserid").val()+"&lgcorpcode"+$("#lgcorpcode").val());
				   $("#addForm").submit();
			   }else{
				   $("#button").attr("disabled","");
				   return;
			   }
		   }
	   })
   }
}

function doSubClient()
{
	//如果姓名或电话或者机构任何一个修改了 ，此属性就设为true，后台就要进行重复验证，如果都没有变则不需要验证重复性
	var changeFlag=false;
	var bookType=$("#bookType").val();
   var cName = $.trim($("#cName").val());
   var cNameTemp = $.trim($("#cNameTemp").val());
   var mobile = $.trim($("#mobile").val());
   var mobileTemp = $.trim($("#mobileTemp").val());
   var depId = $("#depId").val();
   var depIdTemp = $("#depIdTemp").val();
   var checkUrl = $("#checkUrl").val();
   var employeeNo = $.trim($("#employeeNo").val());
   var clientNo = $.trim($("#clientCode").val());
   var email = document.getElementById("EMail").value;
   email=email.replace(/(^\s*)|(\s*$)/g, "");//去除两端空格
   if(email!=""){
            //对电子邮箱的验证
            var myreg =  /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
            if(!myreg.test(email))
            {
                 alert('邮箱格式不正确');
                $("#EMail").focus();
                return;
            }  
   }
   
   if (cName == "")
   {
	  alert("姓名不能为空！");
	  $("#cName").focus();
	  return;
   }
   else  if (outSpecialChar(cName))
   {
		  alert("姓名含有非法字符！");
		  $("#cName").focus();
		  return;
   }
   else  if (bookType == "employee" && employeeNo == "")
   {
		  alert("工号不能为空！");
		  $("#employeeNo").focus();
		  return;
   }
   else if (depId == "")
   {
	   alert("所属机构不能为空！");
	   return;
   }
  else if (mobile == "")
   {
	   alert("手机不能为空！");
	   $("#mobile").focus();
		  return;
   }
   else if (mobile!="" && mobile.length != 11)
   {
	   alert("手机号码不正确！");
	   $("#mobile").focus();
		  return;
   }
   else
   {
	   if(mobile.indexOf("*")>-1)
	   {
		   $("#mobile").val(mobileTemp);
		   mobile=mobileTemp;
	   }
	   //如果姓名和电话都没有修改则没有必要验证重复性
	   if(cNameTemp!=null && mobileTemp!=null && depIdTemp!=null &&(cName!=cNameTemp || mobileTemp!=mobile || depIdTemp!=depId))
	   {
		   changeFlag=true;
	   }
	   if($("#actionType").val()=="add")
	   {
		   changeFlag = true;
	   }
	   $("#button").attr("disabled","disabled");
	   $.post(checkUrl,{mobile : mobile,eNo:employeeNo,changeFlag:changeFlag,name:cName,cNo:clientNo,hidOpType:$("#hidOpType").val(),depCode:depId},function(result){
		   if(result.indexOf("html") > 0){
			   alert("登录超时，请重新登录！");
			   window.location.href = window.location.href;
			   return;
		   }else if(result != null && result == "numfalse")
		   {
			   alert("手机号码格式不正确！");
			   $("#button").attr("disabled","");
			   return;
		   }
		   else if(result != null && result != "numfalse" && result != "phoneNameRepeat"&& result != "noRepeat"&& result != "true" && result != "phoneRepeat" && result != "phoneExist")
		   {
			   $("#button").attr("disabled","");
			   if(result.length > 0){
				   alert(result);
			   }else{
				   alert("请检查网络/数据库连接是否正常！");
			   }
			   return;
		   }else if(result =="phoneNameRepeat"){
			   alert("该机构下已经存在该客户，请勿重复添加！");
			   $("#button").attr("disabled","");
			   return;
		   }
		   else if(result == "true" || result == "phoneRepeat" || result == "noRepeat"){
			   var message = "确定提交吗？";
			   if(result == "phoneRepeat")
			   {
				   message="该机构已经存在该号码，是否继续添加？";
			   }
			   
			   if (confirm(message))
			   {
				   $("#addForm").attr("action",$("#addForm").attr("action")+"&lguserid="+$("#lguserid").val()+"&lgcorpcode="+$("#lgcorpcode").val());
				   $("#addForm").submit();
			   }else{
				   $("#button").attr("disabled","");
				   return;
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
        
        $("#addoneDiv").show();
        $("#addallDiv").hide();
    }
    else
    {
        $("#addone").removeClass();
        $("#addall").removeClass();
        $("#addall").addClass("infotd1");
        $("#addone").addClass("infotd2");
        
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

