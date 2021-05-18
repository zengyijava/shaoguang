var redirectUrl=document.URL;
var urls=redirectUrl.split("/");
redirectUrl=urls[0]+"//"+urls[2]+"/"+urls[3]+"/";	
/*测试连接*/
function testConnection(){
	if(checkData())
	{
		var dbType,dbconIp,port,name,dbConType,dbUser,dbPwd;
		dbType=$("#dbType").attr("value");
		dbconIp=$("#dbconIp").attr("value");
		port=$("#port").attr("value");
		dbName=$("#dbName").attr("value");
		dbConnType=$("#dbConnType").attr("value");
		dbUser=$("#dbUser").attr("value");
		dbPwd=$("#dbPwd").attr("value");
	
		$("#testDBConnBtn").attr("disabled","true");
		$("#rightTextConnection").text("");
		$("#errorTextConnection").text("");
		//$("#waitTextConnection").text("正在测试连接，请稍候......");
		$("#waitTextConnection").text(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_zzcelj"));
		$.ajax({
			type:"POST",
			url:"eng_mtProcess.htm",
			data:{
			method:"testConnection",
			dbType:dbType,
			dbconIp:dbconIp,
			port:port,
			dbName:dbName,
			dbConnType:dbConnType,
			dbUser:dbUser,
			dbPwd:dbPwd},
			success:function(data){
				if(data=="true"){
					$("#waitTextConnection").text("");
					document.getElementById("errorTextConnection").innerHTML = "";
					//document.getElementById("rightTextConnection").innerHTML = "连接成功!";
					document.getElementById("rightTextConnection").innerHTML = getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_ljcg");
					$("#testDBConnBtn").attr("disabled",false);
				}else{
					$("#waitTextConnection").text("");
					document.getElementById("rightTextConnection").innerHTML = "";
					//document.getElementById("errorTextConnection").innerHTML = "连接失败!";
					document.getElementById("errorTextConnection").innerHTML = getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_ljsb");
					$("#testDBConnBtn").attr("disabled",false);
				}
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
		//$("#errorTextConnection").html("注：数据源名称不能为空");
		$("#errorTextConnection").html(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_zsjymcbnwk"));
		return false;
	}
	if(dbconName.length >19){
		//$("#errorTextConnection").html("注：数据源名称过长，请输入小于19个字符");
		$("#errorTextConnection").html(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_zsjymcgc"));
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
	//密码过滤的表达式，5~20位非空格
	var pwdPatrn=/^[^\s]{1,20}$/;
	//端口过滤过滤的表达式，0到65535的整
	var portPatrn=/^([0-9]|[1-9]\d|[1-9]\d{2}|[1-9]\d{3}|[1-5]\d{4}|6[0-4]\d{3}|65[0-4]\d{2}|655[0-2]\d|6553[0-5])$/; 
	
	if(port==null || port.length==0){
		$("#rightTextConnection").html("");
		$("#errorTextConnection").html("");
		//$("#errorTextConnection").html("注：端口不能为空");
		$("#errorTextConnection").html(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_zdkbnwk"));
		return false;
	}
	
	if(!portPatrn.exec(port)){
		$("#rightTextConnection").html("");
		$("#errorTextConnection").html("");
		//$("#errorTextConnection").html("注：端口请输入0到65535的整数");
		$("#errorTextConnection").html(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_zdkqsr0d65535dzs"));
		return false;
	}
	
	if(""==dbconIp){
		$("#rightTextConnection").html("");
		$("#errorTextConnection").html("");
		//$("#errorTextConnection").html("注：数据库地址不能为空");
		$("#errorTextConnection").html(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_zsjkdzbnwk"));
		return false;
	}
	if(!ipPatrn.exec(dbconIp)){
		$("#rightTextConnection").html("");
		$("#errorTextConnection").html("");
		$("#errorTextConnection").html("注：数据库地址不合法");
		return false;
	}
	if(name==null || name.length==0){
		$("#rightTextConnection").html("");
		$("#errorTextConnection").html("");
		if($("#dbType").val()=="Oracle")
		{
			//$("#errorTextConnection").html("注：服务名/实例不能为空");
			$("#errorTextConnection").html(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_zfwmslbnwk"));
		}
		else
		{
			//$("#errorTextConnection").html("注：数据库名称不能为空");
			$("#errorTextConnection").html(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_zsjkmcbnwk"));
		}
		return false;
	}
	if(dbUser==null || dbUser.length==0){
		$("#rightTextConnection").html("");
		$("#errorTextConnection").html("");
		//$("#errorTextConnection").html("注：用户名不能为空");
		$("#errorTextConnection").html(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_zyhmbnwk"));
		return false;
	}
	if(!pwdPatrn.exec(dbPwd)){
		$("#rightTextConnection").html("");
		$("#errorTextConnection").html("");
		//$("#errorTextConnection").html("注：密码只能1-20位");
		$("#errorTextConnection").html(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_zmmzn1d20w"));
		return false;
	}else{
		$("#rightTextConnection").html("");
		$("#errorTextConnection").html("");
		return true;
	}
}

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
			$.post("eng_mtProcess.htm",{conName:conName,method:"checkDbName",lgcorpcode:lgcorpcode},
				function(result){
					if(result=="false")
					{
						//if(confirm("确定提交吗 ?")){
						if(confirm(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_qdtjm"))){
							$("#addDBConnBtn").attr("disabled","disabled");
							$("#addDBConnBackBtn").attr("disabled","disabled");
				   		    $("#testDBConnBtn").attr("disabled","disabled");
							 //$("#rightTextConnection").html("正在提交中,请稍候.........");
							 $("#rightTextConnection").html(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_zztjz"));
							addDBConnForm.submit();
						}
					}else if(result=="true")
					{
						$("#dbconName").select();
						$("#rightTextConnection").html("");
						$("#errorTextConnection").html("");
						//$("#errorTextConnection").html("注：数据源名称已存在，请重新输入");
						$("#errorTextConnection").html(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_zsjymcycz"));
					}else 
					{
						$("#rightTextConnection").html("");
						$("#errorTextConnection").html("");
						//$("#errorTextConnection").html("注：验证数据源名称失败，无法提交");
						$("#errorTextConnection").html(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_zyzsjymcsb"));
					}
				}
			);
		}else
		{
			//if(confirm("确定提交吗 ?")){
			if(confirm(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_qdtjm"))){
				$("#addDBConnBtn").attr("disabled","disabled");
				$("#addDBConnBackBtn").attr("disabled","disabled");
	   		    $("#testDBConnBtn").attr("disabled","disabled");
				 //$("#rightTextConnection").html("正在提交中,请稍候.........");
				 $("#rightTextConnection").html(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_zztjz"));
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
    //if(confirm('是否删掉这条记录？')){
    if(confirm(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_sfsdztjl"))){
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



//数据库连接选择
function selDbcon(){
	var selDbType=$("#dbType").attr("value");
	//$("#reportcode").attr('value',selCode);
	if(selDbType=="Oracle"){
		//$("#tdDbName").text("服务名/实例：");
		$("#tdDbName").text(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_fwmsl_mh"));
		$("#dbConType").css('display','inline');
	}else{
		//$("#tdDbName").text("数据库名称：");
		$("#tdDbName").text(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_sjkmc_mh"));
		$("#dbConType").css('display','none');
	}
}

function changeDbType(dbType)
{
	if(dbType=="Oracle")
	{
		//$("#tdDbName span").text("服务名/实例：");
		$("#tdDbName span").text(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_fwmsl_mh"));
		$("#dbConnType").attr("disabled","");
		$("#port").val("1521");
	}else if(dbType=="Sql Server")
	{
		//$("#tdDbName span").text("数据库名称：");
		$("#tdDbName span").text(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_sjkmc_mh"));
		$("#dbConnType").attr("disabled","disabled");
		$("#port").val("1433");
	}else if(dbType=="DB2")
	{
		//$("#tdDbName span").text("数据库名称：");
		$("#tdDbName span").text(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_sjkmc_mh"));
		$("#dbConnType").attr("disabled","disabled");
		$("#port").val("50000");
	}else
	{
		//$("#tdDbName span").text("数据库名称：");
		$("#tdDbName span").text(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_sjkmc_mh"));
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
	//if(confirm("您确定要删除该信息？")){
	if(confirm(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_nqdyscgxx"))){
		$.ajax({
			   type:"post",
			   url:"dat_datasourceConf.htm",
			   data:{dbId:dbId,dbconName:dbconName,method:"delete",lgcorpcode:lgcorpcode},
			   success:function(result) {
				       if(result == "true"){
				    	   //alert("删除成功！");
				    	   alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_sccg"));
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
				    	   //alert("删除失败！");
				    	   alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_scsb"));
					    }
				   },
			   error:function(){
					     //alert("删除出现异常！");
					     alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_sccxyc"));
					   }
			});
	}
}
