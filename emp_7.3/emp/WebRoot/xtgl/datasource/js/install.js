$(document).ready(function(){
	//修改密码
	$('.edit_pwd').click(function(){
		modify_pwd();
	})
	//
	$('#db_cg_event').click(function(){
		db_more_cg();
	})
	$('#web_cg_event').click(function(){
		web_more_cg();
	})
	var dbType=$("#DBType").val();
	if(dbType=="1"){
		$("#tdDbName span").text("实例名/服务名：");
		$("#tdDbName2 span").text("备用实例名/服务名：");
		$("#connType").attr("disabled","");
	}else{
		$("#tdDbName span").text("数据库名称：");
		$("#tdDbName2 span").text("备用数据库名称：");
		$("#connType").attr("disabled","disabled");
	}
	if(show_backup_server=="1"){
			$("#backConn").css("display","block");
			$("#backTest").css("display","block");
		}else{
			$("#backConn").css("display","none");
			$("#backTest").css("display","none");
	}
	//显示，隐藏界面
	changeType(isCluster);
	//如果是集群条件下，才会显示,把后台地址拆分出来显示
	if(isCluster=="1"){
		var cols = $("#initCols");
		if(bakinnerUrl!="null"&&bakinnerUrl!=""){
			var inner=bakinnerUrl.split(",");
			for(var i=0;i<inner.length;i++){
				if(i==0){//因为第一个值不能删掉
					$("#bakinnerUrl1").val(inner[0]);
					continue;
				}
				var colNum=i+1;
				cols.append('<tr id="listinner'+colNum+'">'+
						'<td width="150">备用内网地址'+colNum+'：</td>'+
						'<td><input type="text" name="bakinnerUrl'+colNum+'" id="bakinnerUrl'+colNum+'" value="'+inner[i]+'" class="inpStyle w335" placeholder="·备用内网地址'+colNum+'"></td>'+
						'<td class="tips01"></td></tr>');
			}
		}
		if(bakouterUrl!="null"&&bakouterUrl!=""){
			var outer=bakouterUrl.split(",");
			for(var i=0;i<outer.length;i++){
				if(i==0){//因为第一个值不能删掉
					$("#bakouterUrl1").val(outer[0]);
					continue;
				}
				var colNum=i+1;				
				cols.append('<tr id="listout'+colNum+'">'+
				'<td>备用外网地址'+colNum+'：</td>'+
				'<td><input type="text" name="bakouterUrl'+colNum+'" id="bakouterUrl'+colNum+'" value="'+outer[i]+'" class="inpStyle w335" placeholder="·备用外网地址'+colNum+'"></td>'+
				'<td><a onclick="javascript:delCols('+colNum+')" style="float: left;display: block;line-height: 26px;margin-left: 25px;cursor: pointer;margin-top: 6px;">删除</a></td>'+
				'</tr>');
			}
			
		}

	}
})


function modify_pwd(){/*弹出框----修改密码*/
	art.dialog({
			title:"修改密码",
			fixed: true,
		    content: document.getElementById('modfiy_pwd'),
		    lock: true,
		    background: '#000', 
		    opacity: 0.87,
		    id:'1004',
		    ok: function () {
			var confirmPwd=$("#confirmPwd").attr("value");
			var newPwd=$("#newPwd").attr("value");
			var oldPwd=$("#oldPwd").attr("value");
			$("#errorPWD").html("");
			$('#corrPWD').html("");
			if(""==oldPwd){
				$("#errorPWD").html("旧密码不能为空");
				return false;
			}
			if(""==newPwd){
				$("#errorPWD").html("新密码不能为空");
				return false;
			}
			if(""==confirmPwd){
				$("#errorPWD").html("确认密码不能为空");
				return false;
			}
			if(confirmPwd!=newPwd){
				$("#errorPWD").html("新密码与确认密码不一致");
				return false;
			}
			
			if(passCheck=="false"){
				$('#errorPWD').html("原密码输入错误");
				return false;
			}
			$("#errorPWD").html("");
			$.post("systemManage.htm",
					{method:"modifPwd",
					confirmPwd:confirmPwd,
					newPwd:newPwd,
					oldPwd:oldPwd},
					 function(result){				
					closeDiv('1004',result);
					});
			return false;
		    },
		    cancel: true
		});
}
 
var passCheck;
function checkPass(agoPass)
{
	if(agoPass!="")
	{
		$.post(path+"/systemManage.htm",{method:"checkPass",agoPass:agoPass},function(result)
			{
				passCheck=result;
				if(result=="true")
				{	$('#errorPWD').html("");
					$('#corrPWD').html("原密码输入正确");
				}else if(result=="false")
				{
					$('#corrPWD').html("");
					$('#errorPWD').html("原密码输入错误");
				}
			}
		);
	}else
	{
		$('#zhuPass').html("请输入原密码");
	}
}
//增加地址行
function addCols(){
	var quesType = $("#quesType").val();
	var cols = $("#initCols");
	
	var sontr=cols.find("tr");
	if(sontr.length==10){
		alert("备用地址已达5对，无法再添加！");
		return;
	}
	var colNum = sontr.length/2+1;
	cols.append('<tr id="listinner'+colNum+'">'+
	'<td width="150">备用内网地址'+colNum+'：</td>'+
	'<td><input type="text" name="bakinnerUrl'+colNum+'" id="bakinnerUrl'+colNum+'" value="" class="inpStyle w335" placeholder="·备用内网地址'+colNum+'"></td>'+
	'<td class="tips01"></td>'+
	'</tr><tr id="listout'+colNum+'">'+
	'<td>备用外网地址'+colNum+'：</td>'+
	'<td><input type="text" name="bakouterUrl'+colNum+'" id="bakouterUrl'+colNum+'" value="" class="inpStyle w335" placeholder="·备用外网地址'+colNum+'"></td>'+
	'<td><a onclick="javascript:delCols('+colNum+')" style="float: left;display: block;line-height: 26px;margin-left: 25px;cursor: pointer;margin-top: 6px;">删除</a></td>'+
	'</tr>');
	

}

function delCols(obj){
	$("#listinner"+obj).remove();
	$("#listout"+obj).remove();
}

function db_more_cg(){/*弹出框----数据库更多配置参数*/
	art.dialog({
			title:"数据库更多配置参数",
			fixed: true,
		    content: document.getElementById('db_more_cg'),
		    lock: true,
		    background: '#000', 
		    opacity: 0.87,
		    id:'1000',
		    ok: function () {
		var maxPoolSize =$("#maxPoolSize").attr("value");
		var minPoolSize =$("#minPoolSize").attr("value");
		var InitialPoolSize=$("#InitialPoolSize").attr("value");
		var check=/^\d+$/;// 正则表达示验证数字
		if(maxPoolSize!=""){
			if(!check.test(maxPoolSize)){
				alert("最大连接数为数字！");
				location.reload();
				return;
			}
		}
		if(maxPoolSize==null || maxPoolSize.length==0){
			alert("最大连接数不能为空！");
			location.reload();
			return;
		}
		if(minPoolSize!=""){
			if(!check.test(minPoolSize)){
				alert("最小连接数为数字！");
				location.reload();
				return;
			}
		}
		if(minPoolSize==null || minPoolSize.length==0){
			alert("最小连接数不能为空！");
			location.reload();
			return;
		}
		
		if(parseInt(maxPoolSize)<parseInt(minPoolSize)){
			alert("最大连接数不能小于最小连接数！");
			location.reload();
			return;	
		}
		
		
		if(InitialPoolSize!=""){
			if(!check.test(InitialPoolSize)){
				alert("初始连接数为数字！");
				location.reload();
				return;
			}
		}
		if(InitialPoolSize==null || InitialPoolSize.length==0){
			alert("初始连接数不能为空！");
			return;
		}
		$.post("systemManage.htm",
				{method:"dataSubmit",
			maxPoolSize:maxPoolSize,
			minPoolSize:minPoolSize,
			InitialPoolSize:InitialPoolSize},
				 function(result){				
				closeDiv('1000',result);
				});
		
		        return false;
		    },
		    okVal:"确定",
		    button: [{
		    	name: '恢复默认设置',
		        callback: function () {
		    	defalutDataValue();
		    	location.reload();
		        },
		        focus: true
		    }]
		});
}

function closeDiv(id,result){
	resultShow(result);
	art.dialog({id: id}).close();
}


function defalutDataValue(){
//	window.location.href='systemManage.htm?method=dataDefalut';
	$.post("systemManage.htm",
			{method:"dataDefalut"},
			 function(result){				
			resultShow(result);	
			});
}

function web_more_cg(){/*弹出框---web更多配置参数*/
	art.dialog({
			title:"web信息更多配置参数",
			fixed: true,
		    content: document.getElementById('web_more_cg'),
		    lock: true,
		    background: '#000', 
		    opacity: 0.87,
		    id:'1001',
		    ok: function () {
			var check=/^\d+$/;// 正则表达示验证数字
			var defaultPageSize=$("#defaultPageSize").attr("value");
			var frame=$("#frame").attr("value");
			if(defaultPageSize!=""){
				if(!check.test(defaultPageSize)){
					alert("默认页面大小为数字！");
					location.reload();
					return;
				}
			}
			
			if(defaultPageSize==null || defaultPageSize.length==0){
				alert("默认页面大小不能为空！");
				location.reload();
				return;
			}
			$.post("systemManage.htm",
					{method:"webSubmit",
					defaultPageSize:defaultPageSize,
					frame:frame},
					 function(result){				
					closeDiv('1001',result);
					});
		        return false;
		    },

		    okVal:"确定",
		    button: [{
		    	name: '恢复默认设置',
		        callback: function () {
		    	defalutWebValue();
		    	location.reload();
		        },
		        focus: true
		    }]
		});
}


function resultShow(executResult){
	if(executResult!="null"){
		if(executResult=="modifPwdsuccess"){
			alert("修改密码成功！");
		}
			if(executResult=="differ"){
			alert("旧密码不对！");
		}
			if(executResult=="confsucc"){
			alert("修改配置项成功！");
		}
		if(executResult=="conffail"){
			alert("修改配置项失败！");
		}
		if(executResult=="defalutSucc"){
			alert("恢复默认值成功！");
		}
			if(executResult=="savesucess"){
			$("#errorIP").html("");
			alert("保存成功！");
		}
			if(executResult=="defalutFail"){
			alert("恢复默认值失败！");
		}
		}
	
}

function defalutWebValue(){
//	window.location.href='systemManage.htm?method=webDefalut';
	$.post("systemManage.htm",
			{method:"webDefalut"},
			 function(result){				
			resultShow(result);	
			});
	
}

/*测试连接*/
function testConnection(){
    if("true" == $("#testDBConnBtn").attr("disabled")){
        return;
    }
	if(checkData())
	{
        var dbTypes = ['Oracle','Sql Server','Mysql','DB2'];
		var dbType,dbconIp,port,dbName,dbConnType,dbUser,dbPwd;
		dbType=dbTypes[$("#DBType").val()-1];
		dbconIp=$("#dbconIp").val();
		port=$("#port").val();
		dbName=$("#dbName").val();
		dbConnType=$("#connType").attr("value");
		dbUser=$("#dbUser").val();
		dbPwd=$("#dbPwd").val();
		$.ajax({
			type:"POST",
			url:"dat_datasourceConf.htm",
			data:{
			method:"testConnection",
			dbType:dbType,
			dbconIp:dbconIp,
			port:port,
			dbName:dbName,
			dbConnType:dbConnType,//后台
			dbUser:dbUser,
			dbPwd:dbPwd},
            beforeSend:function(){
                $("#testDBConnBtn").attr("disabled",true);//超链接元素 不支持此属性 设置无效
                $("#waitTextConnection").text("正在测试连接，请稍候......");
            },
            complete:function(){
                $("#waitTextConnection").text("");
                $("#testDBConnBtn").attr("disabled",false);
            },
			success:function(data){
                var isSuc = ("true" == data);
                document.getElementById("errorTextConnection").innerHTML = (isSuc?"":"连接失败!");
                document.getElementById("rightTextConnection").innerHTML = (isSuc?"连接成功!":"");
			}
		})
	}
}

/*测试备用连接*/
function testBackConnection(){
    if("true" == $("#testBackConnBtn").attr("disabled")){
        return;
    }
	if(checkbackData())
	{
        var dbTypes = ['Oracle','Sql Server','Mysql','DB2'];
		var dbType,dbconIp,port,dbName,dbConnType,dbUser,dbPwd;
		dbType=dbTypes[$("#DBType").val()-1];
		dbconIp=$("#dbconIp2").val();
		port=$("#port2").val();
		dbName=$("#dbName2").val();
		dbConnType=$("#connType").attr("value");
		dbUser=$("#dbUser2").val();
		dbPwd=$("#dbPwd2").val();
		$.ajax({
			type:"POST",
			url:"dat_datasourceConf.htm",
			data:{
			method:"testConnection",
			dbType:dbType,
			dbconIp:dbconIp,
			port:port,
			dbName:dbName,
			dbConnType:dbConnType,//后台
			dbUser:dbUser,
			dbPwd:dbPwd},
            beforeSend:function(){
                $("#testBackConnBtn").attr("disabled",true);//超链接元素 不支持此属性 设置无效
                $("#waitTextConnection2").text("正在测试连接，请稍候......");
            },
            complete:function(){
                $("#waitTextConnection2").text("");
                $("#testBackConnBtn").attr("disabled",false);
            },
			success:function(data){
                var isSuc = ("true" == data);
                document.getElementById("errorTextConnection2").innerHTML = (isSuc?"":"连接失败!");
                document.getElementById("rightTextConnection2").innerHTML = (isSuc?"连接成功!":"");
			}
		})
	}
}

	function submit(){
		var dbconIp=$("#dbconIp").attr("value");
		var DBType=$("#DBType").attr("value");
		//连接类型
		var connType=$("#connType").attr("value");
		var dbconIp,port,name,dbConType,dbUser,dbPwd;
		dbconIp=$("#dbconIp").attr("value");
		port=$("#port").attr("value");
		dbName=$("#dbName").attr("value");
		dbUser=$("#dbUser").attr("value");
		dbPwd=$("#dbPwd").attr("value");
		var use_backup_server=$("#use_backup_server").val();
		var dbconIp2=$("#dbconIp2").val();
	    var port2=$("#port2").val();
	    var dbName2=$("#dbName2").val();
	    var dbUser2=$("#dbUser2").val();
	    var dbPwd2=$("#dbPwd2").val();
		
		if(checkData()&&checkbackData())
		{ 

			var innerUrl=$("#innerUrl").attr("value");
			if(innerUrl!=""){
				if(innerUrl.indexOf("http:")==-1){
					$("#errorIP").html("");
					$("#errorIP").html("内网地址格式不对");
					return ;
				}
			}
			
			var outerUrl=$("#outerUrl").attr("value");
			if(outerUrl!=""){
				if(outerUrl.indexOf("http://")==-1){
					$("#errorIP").html("外网地址格式不对");
					return ;
				}
			}
			//备用地址的校验
			var isCluster=$("#isCluster").val();
			//开启的状态下校验
			if(isCluster=="1"){
				
			for (var i=1;i<6;i++){
				var bakinnerUrl=$("#bakinnerUrl"+i).attr("value");
				if(bakinnerUrl==""){
					$("#errorIP").html("备用内网地址"+i+"的地址为空");
					return;
				}else if(bakinnerUrl!=undefined&&bakinnerUrl.indexOf("http://")==-1){
						$("#errorIP").html("备用内网地址"+i+"的地址格式不对");
						return ;
					}
				var bakouterUrl=$("#bakouterUrl"+i).attr("value");
				if(bakouterUrl==""){
					$("#errorIP").html("备用外网地址"+i+"的地址为空");
					return;
				}else if(bakouterUrl!=undefined&&bakouterUrl.indexOf("http://")==-1){
						$("#errorIP").html("备用外网地址"+i+"的地址格式不对");
						return ;
					}
			}
			}
			var EMPaddress=$("#EMPaddress").attr("value");
			if(EMPaddress!=""){
				if(EMPaddress.indexOf("http:")==-1){
					$("#errorIP").html("");
					$("#errorIP").html("EMP访问地址格式不对");
					return ;
				}
			}
			
			var webgate=$("#webgate").attr("value");
			if(webgate!=""){
				if(webgate.indexOf("http://")==-1){
					$("#errorIP").html("网关通讯地址格式不对");
					return ;
				}
			}
			
			var EMPwxaddress=$("#EMPwxaddress").attr("value");
			if(EMPwxaddress!=""){
				if(EMPwxaddress.indexOf("http://")==-1){
					$("#errorIP").html("网讯站点访问地址格式不对");
					return ;
				}
			}
			//EMP日志文件存储地址
			var loggeraddress=$("#loggeraddress").attr("value");
			if(loggeraddress==""){
				$("#errorIP").html("EMP日志文件存储地址不能为空");
				return;
			}
			var isCluster=$("#isCluster").attr("value");
			var serverNumber=$("#serverNumber").attr("value");	
			var bakinnerUrl1=$("#bakinnerUrl1").attr("value");
			var bakinnerUrl2=$("#bakinnerUrl2").attr("value");
			var bakinnerUrl3=$("#bakinnerUrl3").attr("value");
			var bakinnerUrl4=$("#bakinnerUrl4").attr("value");
			var bakinnerUrl5=$("#bakinnerUrl5").attr("value");
			var bakouterUrl1=$("#bakouterUrl1").attr("value");
			var bakouterUrl2=$("#bakouterUrl2").attr("value");
			var bakouterUrl3=$("#bakouterUrl3").attr("value");
			var bakouterUrl4=$("#bakouterUrl4").attr("value");
			var bakouterUrl5=$("#bakouterUrl5").attr("value");
			
			//EMP日志文件默认存储地址
			var defaultloggeraddress=$("#defaultloggeraddress").attr("value");
			var type=$("#type").attr("value");
			$.post("systemManage.htm",
					{method:"update",
				EMPaddress:EMPaddress,
				innerUrl:innerUrl,
				outerUrl:outerUrl,
				webgate:webgate,
				EMPwxaddress:EMPwxaddress,
				loggeraddress:loggeraddress,
				defaultloggeraddress:defaultloggeraddress,
				DBType:DBType,//后台的参数是DBType
				connType:connType,
				dbconIp:dbconIp,
				port:port,
				dbName:dbName,
				type:type,//后台的参数是type
				dbUser:dbUser,
				isCluster:isCluster,
				serverNumber:serverNumber,
				bakinnerUrl1:bakinnerUrl1,
				bakinnerUrl2:bakinnerUrl2,
				bakinnerUrl3:bakinnerUrl3,
				bakinnerUrl4:bakinnerUrl4,
				bakinnerUrl5:bakinnerUrl5,
				bakouterUrl1:bakouterUrl1,
				bakouterUrl2:bakouterUrl2,
				bakouterUrl3:bakouterUrl3,
				bakouterUrl4:bakouterUrl4,
				bakouterUrl5:bakouterUrl5,
				use_backup_server:use_backup_server,
				dbconIp2:dbconIp2,
				port2:port2,
				dbName2:dbName2,
				dbUser2:dbUser2,
				dbPwd2:dbPwd2,
				dbPwd:dbPwd	
					},
					 function(result){				
					resultShow(result);	
					});
			//$("#form").submit();
		}
		
		
	}
	//是否集群
	function changeType(dbType)
	{
		if(dbType=="1")
		{
			
			$("#serverNum").css("display","block");
			$("#initCols").css("display","block");
		}else{
			$("#serverNum").css("display","none");
			//使用默认的
			$("#serverNum").find("option[text='1001']").attr("selected",true); 
			$("#initCols").css("display","none");
		}
	}
	
		//是否开启备用数据库
	function isOPen(dbType)
	{
		if(dbType=="1")
		{
			$("#backConn").css("display","block");
			$("#backTest").css("display","block");
		}else{
			$("#backConn").css("display","none");
			$("#backTest").css("display","none");
		}
	}
	

function checkbackData(){
 	var dbconIp=$("#dbconIp2").val();
    var port=$("#port2").val();
    var name=$("#dbName2").val();
    var dbUser=$("#dbUser2").val();
    var dbPwd=$("#dbPwd2").val();
	var type=$("input[name='dataPooltype']:checked").val();
	//IP过滤的表达式 1.0.0.1  ~  255.255.255.255
	var ipPatrn=/^(([1-9]|([1-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))\.)(([0-9]|([0-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))\.){2}([1-9]|([1-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))$/;
	//域名匹配的表达式
	var urlPatrn=/^(?=^.{3,255}$)[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+$/;
	
	//密码过滤的表达式，5~20位非空格
	var pwdPatrn=/^[^\s]{1,20}$/;
	//端口过滤过滤的表达式，0到65535的整
	var portPatrn=/^([0-9]|[1-9]\d|[1-9]\d{2}|[1-9]\d{3}|[1-5]\d{4}|6[0-4]\d{3}|65[0-4]\d{2}|655[0-2]\d|6553[0-5])$/;
	
	var use_backup_server=$("#use_backup_server").val();
	//如果备用数据库启用才能判断
	if(use_backup_server=="1"){
	    $("#rightTextConnection2").html("");
		if(port==null || port.length==0){
			$("#errorTextConnection2").html("注：备用端口不能为空");
			return false;
		}
		
		if(!portPatrn.exec(port)){
			$("#errorTextConnection2").html("注：备用端口请输入0到65535的整数");
			return false;
		}
		
		if(""==dbconIp){
			$("#errorTextConnection2").html("注：备用数据库地址不能为空");
			return false;
		}
		if(!(urlPatrn.exec(dbconIp) || ipPatrn.exec(dbconIp))){
			$("#errorTextConnection2").html("注：备用数据库地址不合法");
			return false;
		}
		if(name==null || name.length==0){
			if($("#DBType").val() == 1)
			{
				$("#errorTextConnection2").html("注：备用服务名/实例不能为空");
			}
			else
			{
				$("#errorTextConnection2").html("注：备用数据库名称不能为空");
			}
			return false;
		}
		if(dbUser==null || dbUser.length==0){
			$("#errorTextConnection2").html("注：备用用户名不能为空");
			return false;
		}
		if(type==null || type.length==0){
			$("#errorTextConnection2").html("注：数据库连接池不能为空");
			return false;
		}else{
			$("#type").val(type);
		}
		if(!pwdPatrn.exec(dbPwd)){
			$("#errorTextConnection2").html("注：备用密码只能1-20位");
			return false;
		}else{
			$("#errorTextConnection2").html("");
			return true;
		}
	}else{
		return true;
	}
}
	
	
function checkData(){
 	var dbconIp=$("#dbconIp").val();
    var port=$("#port").val();
    var name=$("#dbName").val();
    var dbUser=$("#dbUser").val();
    var dbPwd=$("#dbPwd").val();
	var type=$("input[name='dataPooltype']:checked").val();
	//IP过滤的表达式 1.0.0.1  ~  255.255.255.255
	var ipPatrn=/^(([1-9]|([1-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))\.)(([0-9]|([0-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))\.){2}([1-9]|([1-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))$/;
	//域名匹配的表达式
	var urlPatrn=/^(?=^.{3,255}$)[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+$/;
	
	//密码过滤的表达式，5~20位非空格
	var pwdPatrn=/^[^\s]{1,20}$/;
	//端口过滤过滤的表达式，0到65535的整
	var portPatrn=/^([0-9]|[1-9]\d|[1-9]\d{2}|[1-9]\d{3}|[1-5]\d{4}|6[0-4]\d{3}|65[0-4]\d{2}|655[0-2]\d|6553[0-5])$/;
    $("#rightTextConnection").html("");
	if(port==null || port.length==0){
		$("#errorTextConnection").html("注：端口不能为空");
		return false;
	}
	
	if(!portPatrn.exec(port)){
		$("#errorTextConnection").html("注：端口请输入0到65535的整数");
		return false;
	}
	
	if(""==dbconIp){
		$("#errorTextConnection").html("注：数据库地址不能为空");
		return false;
	}
	if(!(urlPatrn.exec(dbconIp) || ipPatrn.exec(dbconIp))){
		$("#errorTextConnection").html("注：数据库地址不合法");
		return false;
	}
	if(name==null || name.length==0){
		if($("#DBType").val() == 1)
		{
			$("#errorTextConnection").html("注：服务名/实例不能为空");
		}
		else
		{
			$("#errorTextConnection").html("注：数据库名称不能为空");
		}
		return false;
	}
	if(dbUser==null || dbUser.length==0){
		$("#errorTextConnection").html("注：用户名不能为空");
		return false;
	}
	if(type==null || type.length==0){
		$("#errorTextConnection").html("注：数据库连接池不能为空");
		return false;
	}else{
		$("#type").val(type);
	}
	if(!pwdPatrn.exec(dbPwd)){
		$("#errorTextConnection").html("注：密码只能1-20位");
		return false;
	}else{
		$("#errorTextConnection").html("");
		return true;
	}
}