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
function hasChecked(roleId){
	for(var i = 0 ; i < roleId.length ; i=i+1 ){
		if( roleId[i].checked == true ){
			return true;
		}
	}
	return false;
}
//提交数据合法性检查
function checkData(way){
	
	var userNo=$("#userNo").attr("value");
	var userCode=$("#userCode").attr("value");
	var password=$("#password").attr("value");
	var isChange = $("#rePwd").attr("value");
	var newPwd = $("#newPwd").attr("value");
	var newPwd2 = $("#newPwd2").attr("value");
	var name=$("#name").attr("value");
	var oph=$("#oph").attr("value");
	var mobile= $("#tempMobile").attr("value");//$("#mobile").attr("value");
	/*if($("#tempMobile").attr("value")){
		mobile = $("#tempMobile").attr("value");
	}*/
	var phone_temp = $("#mobile").val();
	var ishidephone = $("#ishidephone").val();
	if((mobile == "" || mobile.length == 0) && phone_temp.length>0){
		 $("#tempMobile").val(ishidephone);
		 mobile = ishidephone;
	}
	if(mobile.indexOf("*") > 0){
		//如果含有星号
		mobile = $("#mobile").attr("value");
	}else{
		$("#mobile").val(mobile);
	}
	var qq=$("#qq").attr("value");
	var EMail=$("#EMail").attr("value");
	var password2=$("#password2").attr("value");
	var depId=$("#depId").val();
	var sex=$("#sex").attr("value");
	var userState=$("#userState").attr("value");
	var permissionType=$("#userPerType").attr("value");
	var employPerType=$("#employPerType").attr("value");
	var clientPerType=$("#clientPerType").attr("value");
	var domDepId=$("#domDepId").val();
	var domDepId_employ = $("#domDepId_employ").val();
	var domDepId_client = $("#domDepId_client").val();
	var privListString=document.getElementsByName("privListString");
	var roleId=document.getElementsByName("roleId");
	var cheRoles = "";
	var domDepId = $("#domDepId").val();
    var depId = $("#depId").val();
	/*//这里是判断是否需要绑定操作员固定尾号
	var isneedSubno = "2";		
	if(document.getElementsByName('isNeedSubno')[0].checked==true){
		isneedSubno = "1";
	}*/
	var haveSubno=$("#haveSubno").val();
	var isGiveSubno=$("#isGiveSubno").val();
	if(isGiveSubno == "1"){
		if(haveSubno == "1"){
			var updateSubno=$("#updateSubno").val();
			var userdSubno=$("#subno").val();
			if(updateSubno == "" || updateSubno.length == 0){
				alert(getJsLocaleMessage("user","user_xtgl_czygl_text_94"));
				$("#updateSubno").val(userdSubno);
				return;
			}
		}
	}else if(isGiveSubno == "2"){
		if(haveSubno == "1"){
			var updateSubno=$("#addSubno").val();
			var subno2=$("#subno2").val();
			if(updateSubno == "" || updateSubno.length == 0){
				alert(getJsLocaleMessage("user","user_xtgl_czygl_text_94"));
				$("#addSubno").val(subno2);
				return;
			}
		}
	}
	var isEmploy = $("input:radio[name='isEmploy']:checked").attr("value");
	var employeeNo = $("#employeeNo").val();
	var employDepId = $("#employDepId").val();
	
	var patrnName=/^[a-zA-Z0-9]{1,20}$|^[\u4e00-\u9fa5]{1,8}$/;
	var patrnUserNo=/^(\w){1,15}$/; //字母数字下划线
	var patrnPass=/^[^\s]{5,20}$/;  //不能有空格
	var patrnOph=/^((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$/; //可以“+”开头，除数字外，可含有“-”
	var patrnMobile=/^[0-9]{11}$/;///^(13[0-9]|15[0|3|6|7|8|9]|18[8|9])\d{8,16}$/; //必须以数字开头，除数字外，可含有“-”
	var patrnQQ=/^[0-9]*[1-9][0-9]*$/;//正整数 
	var patrnEMail=/^[a-zA-Z0-9_\-]{1,}@[a-zA-Z0-9_\-]{1,}\.[a-zA-Z0-9_\-.]{1,}$/;
	var patrnUserCode = /^[0-9a-zA-Z]{1,20}$/;
	
 	var depNode = zTree_user.getNodesByParam("id", depId)[0];
 	var domNodes = zTree_user.getNodesByParam("id", domDepId,depNode);
 	var corpstr="";
 	if($('#type').val()=="add"){
 		corpstr=$("#corptype").val();
 	}
	$("#zhu").html("");
	if(userNo=="")
	{
		$("#zhu").html(getJsLocaleMessage("user","user_xtgl_czygl_text_95"));
	}else
	if(!patrnUserNo.exec(userNo)){
		$("#zhu").html(getJsLocaleMessage("user","user_xtgl_czygl_text_96"));
	}else
	if(userCode=="")
	{
		$("#zhu").html(getJsLocaleMessage("user","user_xtgl_czygl_text_97"));
	}else if(!patrnUserCode.exec(corpstr+""+userCode)){
		$("#zhu").html(getJsLocaleMessage("user","user_xtgl_czygl_text_98"));
	}else if(outSpecialChar(name)){
		$("#zhu").html(getJsLocaleMessage("user","user_xtgl_czygl_text_99"));
	}else if(name.length<1 || name.length>60){
		$("#zhu").html(getJsLocaleMessage("user","user_xtgl_czygl_text_100"));
	}else if(way==2 && (depId==null || depId=="0")){
		$("#zhu").html(getJsLocaleMessage("user","user_xtgl_czygl_text_101"));
	}else if(mobile.length == 0){
			$("#zhu").html(getJsLocaleMessage("user","user_xtgl_czygl_text_102"));
	}else if(!checkPhone(mobile)){
		$("#zhu").html(getJsLocaleMessage("user","user_xtgl_czygl_text_103"));
	}else if(roleId.length==0 && way==2){
		if(confirm(getJsLocaleMessage("user","user_xtgl_czygl_text_104"))){
			showfirstrev();
		}
		$("#zhu").html("");
		$("#zhu").html(getJsLocaleMessage("user","user_xtgl_czygl_text_105"));
	}else if(way==2&&!hasChecked(roleId)){
		$("#zhu").html("");
		$("#zhu").html(getJsLocaleMessage("user","user_xtgl_czygl_text_105"));
		showRole();
	}else if(userState==null){
		$("#zhu").html(getJsLocaleMessage("user","user_xtgl_czygl_text_106"));
	}else if(permissionType == 2 && way==2 && !domDepId ){
		$("#zhu").html(getJsLocaleMessage("user","user_xtgl_czygl_text_107"));
	}else if(permissionType == 2 && depId !=  domDepId &&(domNodes.length != 1 || !depNode)){
 		alert(getJsLocaleMessage("user","user_xtgl_czygl_text_108"));
	}else if(employPerType == 2 && way==2 && !domDepId_employ){
		$("#zhu").html(getJsLocaleMessage("user","user_xtgl_czygl_text_109"));
	}else if(clientPerType == 2 && way==2 && !domDepId_client ){
		$("#zhu").html(getJsLocaleMessage("user","user_xtgl_czygl_text_110"));
	}else if(!patrnOph.exec(oph)&&oph.length != 0){
		$("#zhu").html(getJsLocaleMessage("user","user_xtgl_czygl_text_111"));
	}else if($("#emailRemind").attr("checked")==true&&(EMail.length==0||EMail=="")){
		$("#zhu").html(getJsLocaleMessage("user","user_xtgl_czygl_text_112"));
	}else if(EMail.length!=0 && !patrnEMail.exec(EMail)){
		$("#zhu").html(getJsLocaleMessage("user","user_xtgl_czygl_text_113"));
	}else if(qq.length!=0 && !patrnQQ.exec(qq)){
		$("#zhu").html(getJsLocaleMessage("user","user_xtgl_czygl_text_114"));
	}else if(isEmploy == "1" && !employDepId){
		$("#zhu").html(getJsLocaleMessage("user","user_xtgl_czygl_text_115"));
	}else{
		$.post("opt_sysuser.htm",{
			method:"checkPhone" ,
			mobile : mobile 
				},function(resultMsg){
					if(resultMsg.indexOf("html")>0){
						 window.location.href = window.location.href;
					}else if(resultMsg == "false"){
						$("#zhu").html(getJsLocaleMessage("user","user_xtgl_czygl_text_103"));
					}else if(resultMsg == "true"){
									var mid=0;
									if(way==2){
										for(var i = 0 ; i < roleId.length ; i=i+1 ){
											if( roleId[i].checked == true ){
												mid=1;
												break;
											}
											if( i == (roleId.length-1) ){
												$("#zhu").html("");
												$("#zhu").html(getJsLocaleMessage("user","user_xtgl_czygl_text_105"));
												showRole();
												return;
											}
										}
									}
									if(mid==1){
										for(var i = 0 ; i < roleId.length ; i=i+1 )
										{
											if( roleId[i].checked == true ){
											    cheRoles = cheRoles + roleId[i].value+",";
											}
										}
										cheRoles=cheRoles.substring(0,cheRoles.length-1);
										$("#cheRoles").attr("value",cheRoles);
										var addUpdateflag = false;
										if($('#type').val()=="add"){
											addUpdateflag = true;
										}else if($('#oldDepId').val() != depId){
											addUpdateflag = true;
										}
										if(addUpdateflag){
											$.post("opt_sysuser.htm",{
												method:"checkDepCount" ,
												depId : depId 
												,lgcorpcode:$("#lgcorpcode").val()
													},function(returnMsg){
														if(returnMsg.indexOf("html")>0){
															 window.location.href = window.location.href;
														}else if(returnMsg == "false"){
															$("#zhu").html(getJsLocaleMessage("user","user_xtgl_czygl_text_116"));
														}else if(returnMsg == "true"){
															if($('#type').val()=="add")
															{
																$("#button").attr("disabled","disabled");
																checkUser(way,1);
															}else
															{
																$("#button").attr("disabled","disabled");
																//$("#Sysuser").submit();
																checkUser(way,1);
															}
														}else if("" == returnMsg){
															alert(getJsLocaleMessage("user","user_xtgl_czygl_text_117"));
															window.location.href = window.location.href;
														}
											});
										}else{
											$("#button").attr("disabled","disabled");
											//$("#Sysuser").submit();
											checkUser(way,1);
										}
								}
					}
			}
		);
	}
	
}




//checkadmin
function checkadmin(userId){
    $.ajax({
        url:"checkadmin.action",
		type:"post",
		data:{userId:userId}
    	,success:function(data){
			if(data=="true")
			{
				if(confirm(getJsLocaleMessage("user","user_xtgl_czygl_text_118")))
				{
					document.forms['operatorManagerForm'].action="sysuserLoadEdit.action?userId="+userId;
					//document.forms['operatorManagerForm'].submit();
				}
				
			}else
			{
				alert(getJsLocaleMessage("user","user_xtgl_czygl_text_119"));
			}
		}
    });
}


/*检查要添加的操作员是否已经存在*/
function checkUser(boo,count){  
		var orname=$("input[name='username']:hidden").val()||'';
		var userNo=$.trim($("#userNo").attr("value"));
		if(userNo=="admin" || userNo=="sysadmin")
    	{
    		alert(getJsLocaleMessage("user","user_xtgl_czygl_text_120")+userNo+getJsLocaleMessage("user","user_xtgl_czygl_text_121"));
    		$("#userNo").val("");
    		$("#button").attr("disabled","");
    		return
    	}
	    $.ajax({
	        url: 'opt_sysuser.htm',
	        data:{
	    		username:userNo,
	    		method:"checkUserName",
	    		orname:orname
	    		//method:"checkName"
	    		,lgcorpcode:$("#lgcorpcode").val()	
	        },
	        type: 'post',
	       // timeout: 3000,
	        success: function(data){
	        	if(userNo=="")
	        	{
	        		$("#zhu").html("");
					$("#zhu").html(getJsLocaleMessage("user","user_xtgl_czygl_text_122"));
					$("#button").attr("disabled","");
	        	}else
				if (data == "false") {
					$("#zhu").html("");
					 var userCode=$.trim($("#userCode").attr("value"));
					 var orCode=$("#oremployeeNo").val()||'';
					    $.ajax({
					        url: 'opt_sysuser.htm',
					        data:{
					    	userCode:userCode,
				    		method:"checkUserCode",
				    		orCode:orCode,
				    		optype:$("#optype").val(),
			    			isEmploy:$("input[name='isEmploy']:checked").val(),
							employeeNo:$("#employeeNo").val()
					        },
					        type: 'post',
					       // timeout: 3000,
					        success: function(checkUserCodeResult){
					        	if(userCode=="")
					        	{
					        		$("#zhu").html("");
									$("#zhu").html(getJsLocaleMessage("user","user_xtgl_czygl_text_97"));
									$("#button").attr("disabled","");
					        	}else
								if (checkUserCodeResult == "false") {
									$("#zhu").html("");
									if(boo==2){
										$("#button").attr("disabled","disabled");
										$("#Sysuser").submit();
									}
								}else if(checkUserCodeResult == "true"){
									$("#zhu").html("");
									$("#button").attr("disabled","");
									$("#zhu").html(getJsLocaleMessage("user","user_xtgl_czygl_text_143"));
								}else if(checkUserCodeResult == "existEmploy"){
									$("#zhu").html("");
									$("#button").attr("disabled","");
									$("#zhu").html(getJsLocaleMessage("user","user_xtgl_czygl_text_122"));
								}else{
									$("#zhu").html("");
									$("#zhu").html(getJsLocaleMessage("user","user_xtgl_czygl_text_123"));
									$("#button").attr("disabled","");
								}
					        }
					    });
				}else if(data == "dotrue")
				{
					$("#zhu").html("");
					$("#zhu").html(getJsLocaleMessage("user","user_xtgl_czygl_text_124"));
					$("#button").attr("disabled","");
				}
				else if(data.indexOf("true") == 0){
					$("#zhu").html("");
					$("#zhu").html(getJsLocaleMessage("user","user_xtgl_czygl_text_124"));
					$("#button").attr("disabled","");
				}else{
					$("#zhu").html("");
					$("#zhu").html(getJsLocaleMessage("user","user_xtgl_czygl_text_125"));
					$("#button").attr("disabled","");
				}
	        }
	    });
}


//复选框多选
function checkAll(e,str)    
{    
	var a = document.getElementsByName(str);    
	var n = a.length;    
	for (var i=0 ; i < n ; i++ ){   
		a[i].checked =e.checked;
	}
}

//添加角色
function showRole(){
	$('#roDiv').dialog('open');
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
		$("#domDepId").val(results[0].nodeId);
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


//更改状态
function checkdel(val,userId,name,us,keyId){
	var curId=$("#curId").attr("value");
	if(val==2)
	{
		deleteUser(userId, keyId);
	}
	else if(val==3)
	{
		useUser(userId, keyId);
	}
	else
	{
		if(curId!=userId && userId!=1)
		{
			    $.ajax({
			        url:"opt_sysuser.htm",
					type:"post",
					data:{userId:userId,method:"changeState",name:name,us:us,lgcorpcode:$("#lgcorpcode").val(),keyId:keyId}
			    	,success:function(state){
						if(state!="error")
						{
							if (state == "no")
							{
								alert(getJsLocaleMessage("user","user_xtgl_czygl_text_126"));
								return;
							}
							else
							{
								alert(getJsLocaleMessage("user","user_xtgl_czygl_text_127"));
							}
						}else
						{
							alert(getJsLocaleMessage("user","user_xtgl_czygl_text_26"));
						}
						$("#pageForm").attr("action","opt_sysuser.htm?method=find&pageIndex="+pageIndex+"&pageSize="+pageSize);
						submitForm();
					}
			    });
			//}
		}else
		{
			alert(getJsLocaleMessage("user","user_xtgl_czygl_text_128"));
			$("#pageForm").attr("action","opt_sysuser.htm?method=find&pageIndex="+pageIndex+"&pageSize="+pageSize+"&lguserid="+$("#lguserid").val()+"&lgcorpcode="+$("#lgcorpcode").val());
			submitForm();
		}
	}
}

function swapEle(e1,e2)
{
	if(navigator.userAgent.indexOf('MSIE')>0)
   	{
   		e1.swapNode(e2);
   	}
    else
    {
    	var parent = e1.parentNode;//父节点
		var t1 = e1.nextSibling;//两节点的相对位置
		var t2 = e2.nextSibling;
		if(t1) parent.insertBefore(e2,t1);
		else parent.appendChild(e2);
		if(t2) parent.insertBefore(e1,t2);
		else parent.appendChild(e1);

    }
}

function resetPwd(v)
{
	if(getJsLocaleMessage("user","user_xtgl_czygl_text_129") == v)
	{
		$("#rePwd").attr("value",getJsLocaleMessage("user","user_xtgl_czygl_text_130"));
		$('#newPwd').css("background","#FFFFFF");
		$("#newPwd").attr("disabled",false);
		$('#newPwd').focus();
		$('#newPwd2').css("background","#FFFFFF");
		$("#newPwd2").attr("disabled",false);
	}else
	{
		$("#rePwd").attr("value",getJsLocaleMessage("user","user_xtgl_czygl_text_129"));
		$('#newPwd').css("background","#C0C0C0");
		$("#newPwd").attr("disabled",true);
		$('#newPwd').attr("value","");
		$('#newPwd2').css("background","#C0C0C0");
		$("#newPwd2").attr("disabled",true);
		$('#newPwd2').attr("value","");
		if (getJsLocaleMessage("user","user_xtgl_czygl_text_131") == $("#zhu").html())
		{
			$("#zhu").html("");
		}
	}
}


function doreturn()
{
	var pathUrl = $("#pathUrl").val();
	var isEmp = $("#isEmp").val();
    if(isEmp == "true")
    {
	    location.href=pathUrl+"/epl_employeeBook.htm?method=find&lguserid="+$("#lguserid").val()+
							"&lgcorpcode="+$("#lgcorpcode").val()+"&lgguid="+$("#lgguid").val();
    }
    else
    {
	    location.href=pathUrl+"/opt_sysuser.htm?method=find&lguserid="+$("#lguserid").val()+
						"&lgcorpcode="+$("#lgcorpcode").val()+"&returnBySubPage=true";
    }
}

//复选框多选
function checkAlls(e,str)    
{    
	var a = document.getElementsByName(str);    
	var n = a.length;    
	for (var i=0 ; i < n ; i++ ){   
		a[i].checked =e.checked;
	}
}
/*
function toEmployee()
{
	var pathUrl = $("#pathUrl").val();
	var count = $("input[name='people']:checked").length;   
	if(count != "1"){alert("请选择一个操作员进行操作！");return;}
	else
	{
		var isopr = $("input[name='people']:checked").attr("id");
		if (isopr == "2")
		{
			alert("该操作员已经是员工！");
			return;
		}
		var id = $("input[name='people']:checked").val();
	    if (id != null || id != "")
	    {
	    	location.href=pathUrl+"/opt_sysuser.htm?method=toEmployee&id="+id+"&lguserid="+$("#lguserid").val()+
	    	"&lgcorpcode="+$("#lgcorpcode").val();
	    }
	}
     
}
*/
function fillRoleName(){
	var roleNameStr = "";
	$('input[name="roleId"]:checked').each(function(){    
   		roleNameStr +=$(this).next().html()+",";
 	}); 
 	if(roleNameStr != ""){
 	    roleNameStr = roleNameStr.substring(0,roleNameStr.lastIndexOf(","));
 	    $("#roleName").attr("allRoleName",roleNameStr);
 	    $("#allRoleName").html(roleNameStr);
 	    var arr = roleNameStr.split(",");
 	    if(arr.length > 1){
 	    	roleNameStr = arr[0]+"...";
 	    }
 		$("#roleName").val(roleNameStr.replaceAll("&lt;","<").replaceAll("&gt;",">"));
 		$("#roleName").removeClass("fontColor");
 	}else{
 		$("#roleName").val(getJsLocaleMessage("user","user_xtgl_czygl_text_144"));
 		$("#roleName").addClass("fontColor");
 		$("#roleName").attr("allRoleName","");
 	}
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

//关闭树的下拉框方法（点击别处时关闭）+关闭时显示下拉框
function closeTreeFunSel(ids)
{
	$("#selectDepBtn").click(function(e){e.stopPropagation()});
	for(var i=0;i<ids.length;i++)
	{
		$("#"+ids[i]).click(function(e){
			e.stopPropagation();
		});
	}
	
	$('html,body').click(function(e){
		var $obj=$(e.target);
		//请选择的显示判断
		if($("#userPerType").val!=1)
		{
			$("#selectDepBtn").css("visibility","visible");
		}
        if($obj.attr("class").indexOf("treeInput")==-1&&$obj.attr("id").indexOf("userPerType")==-1){
        	for(var i=0;i<ids.length;i++)
        	{
        		$("#"+ids[i]).css("display","none");
        	}
        	$("select").css("visibility","");
      }
   });
}


//关闭树的下拉框方法（点击别处时关闭）这个是要特殊处理的方法
function closeTreeFunOnSpc(ids)
{
	$("#employPerType").click(function(e){
		e.stopPropagation();
	});

	$("#clientPerType").click(function(e){
		e.stopPropagation();
	});
	
	$("#userPerType").click(function(e){
		e.stopPropagation();
	});
	
	$("#selectDepBtn_employ").click(function(e){
		e.stopPropagation();
	});
	
	$("#selectDepBtn_user").click(function(e){
		e.stopPropagation();
	});

	$("#treeInput").click(function(e){
		e.stopPropagation();
	});
	
	$("#selectDepBtn_client").click(function(e){
		e.stopPropagation();
	});
	for(var i=0;i<ids.length;i++)
	{
		$("#"+ids[i]).click(function(e){
			e.stopPropagation();
		});
	}

	$('html,body').click(function(e){
		var $obj=$(e.target);
        if($obj.attr("class").indexOf("treeInput")==-1){
        	for(var i=0;i<ids.length;i++)
        	{
        		$("#"+ids[i]).css("display","none");
        		$("select").css("visibility","");
        	}
      }
   });
}
$(function(){
	$("#employeeNo").bind('keyup blur',function(){
		var reg=/[<'">]/g;
		var val=$(this).val();
		if(reg.test(val)){
			$(this).val($(this).val().replace(reg,''));
		}
	});
});

$(document).ready(function(){
	$('#employDepNam').wrapSel();
});

  $(function(){
        $("#emailRemind").click(function(){
            if($("#emailRemind").attr("checked")){
                $("#addEmailCheck").css('display','');
                $("#editEmailCheck").css('display','');
            }else{
                $("#addEmailCheck").css('display','none');
                $("#editEmailCheck").css('display','none');
            }
        });
    });