var redirectUrl=document.URL;
var urls=redirectUrl.split("/");
redirectUrl=urls[0]+"//"+urls[2]+"/"+urls[3]+"/";	
/*测试连接*/
function testConnection(){
	if(checkData())
	{
		var dbType,dbconIp,port,dbName,dbConnType,dbUser,dbPwd;
		dbType=$("#dbType").attr("value");
		dbconIp=$("#dbconIp").attr("value");
		port=$("#port").attr("value");
		dbName=$("#dbName").attr("value");
		dbConnType=$("#dbConnType").attr("value");
		dbUser=$("#dbUser").attr("value");
		dbPwd=$("#dbPwd").attr("value");
	

		$.ajax({
			type:"POST",
			url:"dat_datasourceConf.htm",
			data:{
			method:"testConnection",
			dbType:dbType,
			dbconIp:dbconIp,
			port:port,
			dbName:dbName,
			dbConnType:dbConnType,
			dbUser:dbUser,
			dbPwd:dbPwd},
			beforeSend:function(){
                $("#testDBConnBtn").attr("disabled",true);//超链接元素 不支持此属性 设置无效
                $("#waitTextConnection").text(getJsLocaleMessage("xtgl","xtgl_cswh_sjypz_77"));
            },
           	complete:function(){
                $("#waitTextConnection").text("");
                $("#testDBConnBtn").attr("disabled",false);
            },
			success:function(data){
	            var isSuc = ("true" == data);
                document.getElementById("errorTextConnection").innerHTML = (isSuc?"":getJsLocaleMessage("xtgl","xtgl_cswh_sjypz_78"));
                document.getElementById("rightTextConnection").innerHTML = (isSuc?getJsLocaleMessage("xtgl","xtgl_cswh_sjypz_79"):"");
			}
		})
	}
}
//添加与修改数据库连接 过滤
var dbconName,dbconIp,port,name,dbUser,dbPwd;
function checklink(){
 	dbconName=$("#dbconName").attr("value");
	if($.trim(dbconName)=="" || dbconName.length==0){
		$("#rightTextConnection").html("");
		$("#errorTextConnection").html("");
		$("#errorTextConnection").html(getJsLocaleMessage("xtgl","xtgl_cswh_sjypz_80"));
		return false;
	}
	if(dbconName.length >19){
		$("#errorTextConnection").html(getJsLocaleMessage("xtgl","xtgl_cswh_sjypz_81"));
		return false;
	}
	else{
		$("#rightTextConnection").html("");
		$("#errorTextConnection").html("");
		return true;
	}
}
function checkData(){
 	dbconIp=$("#dbconIp").attr("value");
	port=$("#port").attr("value");
	name=$("#dbName").attr("value");
	dbUser=$("#dbUser").attr("value");
	dbPwd=$("#dbPwd").attr("value");
	//IP过滤的表达式 1.0.0.1  ~  255.255.255.255
	var ipPatrn=/^(([1-9]|([1-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))\.)(([0-9]|([0-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))\.){2}([1-9]|([1-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))$/;
	//域名匹配的表达式
	//var urlPatrn=/^(?=^.{3,255}$)[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})*$/;
	var urlPatrn=/^([a-zA-Z]|[0-9])[-a-zA-Z0-9]{0,62}(\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+$/;
	//密码过滤的表达式，5~20位非空格
	var pwdPatrn=/^[^\s]{1,20}$/;
	//端口过滤过滤的表达式，0到65535的整
	var portPatrn=/^([0-9]|[1-9]\d|[1-9]\d{2}|[1-9]\d{3}|[1-5]\d{4}|6[0-4]\d{3}|65[0-4]\d{2}|655[0-2]\d|6553[0-5])$/; 
	
	if(port==null || port.length==0){
		$("#errorTextConnection").html(getJsLocaleMessage("xtgl","xtgl_cswh_sjypz_82"));
		return false;
	}
	
	if(!portPatrn.exec(port)){
		$("#errorTextConnection").html(getJsLocaleMessage("xtgl","xtgl_cswh_sjypz_83"));
		return false;
	}
	
	if(""==dbconIp){
		$("#errorTextConnection").html(getJsLocaleMessage("xtgl","xtgl_cswh_sjypz_84"));
		return false;
	}
	
	//验证长度不超过32位
	if(dbconIp.length>32){
		$("#errorTextConnection").html(getJsLocaleMessage("xtgl","xtgl_cswh_sjypz_99"));
		return false;
	}else{
		if(!(urlPatrn.exec(dbconIp) || ipPatrn.exec(dbconIp))){
			$("#errorTextConnection").html(getJsLocaleMessage("xtgl","xtgl_cswh_sjypz_85"));
			return false;
		}else{
			var dataPatrn=/^\d+(\.\d*)+$/;
	    	//验证纯数字是否合法
	    	if(dbconIp.match(dataPatrn)!=null){
	    		var tempArray=dbconIp.split("\.");
	    		for(var x=0;x<tempArray.length;x++){
	    			if(tempArray.length<4){
	    				$("#errorTextConnection").html(getJsLocaleMessage("xtgl","xtgl_cswh_sjypz_85"));
	    				return false;
	    			}else if(tempArray[x]>250||tempArray[x]<0){
	    				$("#errorTextConnection").html(getJsLocaleMessage("xtgl","xtgl_cswh_sjypz_85"));
	    				return false;
	    			}
	    		}
	    	}
	    	return true;
		}
	}
	
	if(name==null || name.length==0){
		if($("#dbType").val()=="Oracle")
		{
			$("#errorTextConnection").html(getJsLocaleMessage("xtgl","xtgl_cswh_sjypz_86"));
		}
		else
		{
			$("#errorTextConnection").html(getJsLocaleMessage("xtgl","xtgl_cswh_sjypz_87"));
		}
		return false;
	}
	if(dbUser==null || dbUser.length==0){
		$("#errorTextConnection").html(getJsLocaleMessage("xtgl","xtgl_cswh_sjypz_88"));
		return false;
	}
	if(!pwdPatrn.exec(dbPwd)){
		$("#errorTextConnection").html(getJsLocaleMessage("xtgl","xtgl_cswh_sjypz_89"));
		return false;
	}else{
		$("#errorTextConnection").html("");
		return true;
	}
}

/*
//提交信息
function submitInfo(){
	if(checkData()){
		var dbconIp = $("#dbconIp").val(); 
	    $.ajax({
	        url: '${pageContext.request.contextPath}/checkDbConInfo.action',
	        data:{
	    		dbIp:dbconIp
	    		//dbName:dbconIp
	        },
	        type: 'post',
	        dataType: 'json',
	       // timeout: 3000,
	        error: function(){
	        	$("#errorTextConnection").html("查询数据失败");
	        	return false;
	        },
	        success: function(data){
				if (data.success == true) {
					//alert("执行成功！");
					//window.location.reload();
					$("#connection").submit();
					return true;
				}else{
					$("#errorTextConnection").html("");
					$("#errorTextConnection").html("该ip地址已存在，请重新输入！");
					return false;
				}		   
	        }
	    });
		
	}
}
*/

//提交信息
function addDBConn(){
 	var addDBConnForm =$("#addDBConn");//、addDBConnForm.action = "addDbconnect";
 	var lgcorpcode=$(window.parent.document).find("#lgcorpcode").val();
 	addDBConnForm.attr("action",addDBConnForm.attr("action")+"&lgcorpcode="+lgcorpcode);
	if(checklink() && checkData()){
		var conName=$("#dbconName").val();
		var lgcorpcode =$("#lgcorpcode").val();
		if( conName!=$("#curName").val() )
		{
			$.post("dat_datasourceConf.htm",{conName:conName,method:"checkDbName",lgcorpcode:lgcorpcode},
				function(result){
					if(result=="false")
					{
						if(confirm(getJsLocaleMessage("xtgl","xtgl_cswh_sjypz_90"))){
							$("#addDBConnBtn").attr("disabled","disabled");
							$("#addDBConnBackBtn").attr("disabled","disabled");
				   		    $("#testDBConnBtn").attr("disabled","disabled");
							 $("#rightTextConnection").html(getJsLocaleMessage("xtgl","xtgl_cswh_sjypz_91"));
							addDBConnForm.submit();
						}
					}else if(result=="true")
					{
						$("#dbconName").select();
						$("#rightTextConnection").html("");
						$("#errorTextConnection").html("");
						$("#errorTextConnection").html(getJsLocaleMessage("xtgl","xtgl_cswh_sjypz_92"));
					}else 
					{
						$("#rightTextConnection").html("");
						$("#errorTextConnection").html("");
						$("#errorTextConnection").html(getJsLocaleMessage("xtgl","xtgl_cswh_sjypz_93"));
					}
				}
			);
		}else
		{
			if(confirm(getJsLocaleMessage("xtgl","xtgl_cswh_sjypz_90"))){
				$("#addDBConnBtn").attr("disabled","disabled");
				$("#addDBConnBackBtn").attr("disabled","disabled");
	   		    $("#testDBConnBtn").attr("disabled","disabled");
				 $("#rightTextConnection").html(getJsLocaleMessage("xtgl","xtgl_cswh_sjypz_91"));
				addDBConnForm.submit();
			}
		}
					
	}
}

//复选框全选
function checkAll(e,str)    
{    
	var a = document.getElementsByName(str);
	var n = a.length;
	for (var i=0;i<n;i++){
		a[i].checked =e.checked;
	}
}
//删除数据库连接
function del(dbId){                	 	
    if(confirm(getJsLocaleMessage("xtgl","xtgl_cswh_sjypz_94"))){
		location.href="delDbconnect.action?dbId="+dbId;
	}
}

//加载下拉框
function loadselect(dbType){
	for(var i = 0;i<document.getElementById("dbType").length;i++)
    {
		if(document.getElementById("dbType").options[i].value == dbType)
        {
			document.getElementById("dbType").options[i].selected = true;
        }
		break;
    }	
}

$(document).ready(function() {
	getLoginInfo("#hiddenValueDiv");
	noquot("#dbconName");
	noquot("#comments");
	noquot("#port");
	noquot("#dbName");
	noquot("#dbUser");
	noquot("#dbPwd");
	var selDbType=$("#dbType").attr("value");
	if(selDbType=="Oracle"){
		//$("#tdDbName").text("Service Name/SID：");
		$("#dbConType").css('display','inline');
	}
});

//数据库连接选择
function selDbcon(){
	var selDbType=$("#dbType").attr("value");
	//$("#reportcode").attr('value',selCode);
	if(selDbType=="Oracle"){
		$("#tdDbName").text(getJsLocaleMessage("xtgl","xtgl_cswh_sjypz_95"));
		$("#dbConType").css('display','inline');
	}else{
		$("#tdDbName").text(getJsLocaleMessage("xtgl","xtgl_cswh_sjypz_96"));
		$("#dbConType").css('display','none');
	}
	
	
}
function addDbConnection()
{
 	addDBConn();
}

function changeDbType(dbType)
{
	if(dbType=="Oracle")
	{
		$("#tdDbName span").text(getJsLocaleMessage("xtgl","xtgl_cswh_sjypz_95"));
		$("#dbConnType").attr("disabled","");
		$("#port").val("1521");
	}else if(dbType=="Sql Server")
	{
		$("#tdDbName span").text(getJsLocaleMessage("xtgl","xtgl_cswh_sjypz_96"));
		$("#dbConnType").attr("disabled","disabled");
		$("#port").val("1433");
	}else if(dbType=="DB2")
	{
		$("#tdDbName span").text(getJsLocaleMessage("xtgl","xtgl_cswh_sjypz_96"));
		$("#dbConnType").attr("disabled","disabled");
		$("#port").val("50000");
	}else
	{
		$("#tdDbName span").text(getJsLocaleMessage("xtgl","xtgl_cswh_sjypz_96"));
		$("#dbConnType").attr("disabled","disabled");
		$("#port").val("3306");
	}
}

//iframe嵌套的层里面这种方式特殊处理，拿父层的信息
function getLoginInfo1(ids)
{
	var $pa = $(window.parent.document);
	var pahtm = $pa.find("#hiddenValueDiv").html();
	$(ids).html(pahtm);
}

function addFrame()
{
	$("#addDataSource").attr("src","dat_datasourceConf.htm?method=toAdd");
    $('#addFrame').dialog('open');
}
function closeAddFrame()
{
	$("#addFrame").dialog("close");
	$("#addDataSource").attr("src","");
}
function eidtFrame(dbId)
{
	$("#editDataSource").attr("src","dat_datasourceConf.htm?method=toEdit&dbId="+dbId);
    $('#editFrame').dialog('open');
}
function closeEditFrame()
{
	$("#editFrame").dialog("close");
	$("#editDataSource").attr("src","");
}

function deleteSelDBConn(dbId){
 var dbconName = $("#dbconName").val();
 var lgcorpcode = $("#lgcorpcode").val();
	if(confirm(getJsLocaleMessage("xtgl","xtgl_cswh_sjypz_97"))){
		$.ajax({
			   type:"post",
			   url:"dat_datasourceConf.htm",
			   data:{dbId:dbId,dbconName:dbconName,method:"delete",lgcorpcode:lgcorpcode},
			   success:function(result) {
				       if(result == "true"){
				    	   alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_5"));
				    	   	var url = 'dat_datasourceConf.htm';
							var conditionUrl = "";
							if(url.indexOf("?")>-1)
							{
								conditionUrl="&";
							}else
							{
								conditionUrl="?";
							}
							$("#hiddenValueDiv").find(" input").each(function(){
								conditionUrl = conditionUrl + $(this).attr("id")+"="+$(this).val()+"&";
							});
							location.href=url+conditionUrl;		
				    	  // location.reload();
					   }
				       else if(result=="false"){
				    	   alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_6"));
					    }
				   },
			   error:function(){
					     alert(getJsLocaleMessage("xtgl","xtgl_cswh_sjypz_98"));
					   }
			});
	}
}
