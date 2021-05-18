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
		/*实例名/服务名：*/
		$("#tdDbName span").text(getJsLocaleMessage("txgl","txgl_js_install_1"));
		/*备用实例名/服务名：*/
		$("#tdDbName2 span").text(getJsLocaleMessage("txgl","txgl_js_install_2"));
		$("#connType").attr("disabled","");
	}else{
		/*数据库名称：*/
		$("#tdDbName span").text(getJsLocaleMessage("txgl","txgl_js_install_3"));
		/*备用数据库名称：*/
		$("#tdDbName2 span").text(getJsLocaleMessage("txgl","txgl_js_install_4"));
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
						/*备用内网地址  备用内网地址*/
						'<td width="150">'+getJsLocaleMessage("txgl","txgl_js_install_5")+colNum+'：</td>'+
						'<td><input type="text" name="bakinnerUrl'+colNum+'" id="bakinnerUrl'+colNum+'" value="'+inner[i]+'" class="inpStyle w335" placeholder="·'+getJsLocaleMessage("txgl","txgl_js_install_5")+colNum+'"></td>'+
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
				/*备用外网地址  备用外网地址   删除*/
				cols.append('<tr id="listout'+colNum+'">'+
				'<td>'+getJsLocaleMessage("txgl","txgl_js_install_6")+colNum+'：</td>'+
				'<td><input type="text" name="bakouterUrl'+colNum+'" id="bakouterUrl'+colNum+'" value="'+outer[i]+'" class="inpStyle w335" placeholder="·'+getJsLocaleMessage("txgl","txgl_js_install_6")+colNum+'"></td>'+
				'<td><a onclick="javascript:delCols('+colNum+')" style="float: left;display: block;line-height: 26px;margin-left: 25px;cursor: pointer;margin-top: 6px;">'+getJsLocaleMessage("common","common_delete")+'</a></td>'+
				'</tr>');
			}
			
		}

	}
})


function modify_pwd(){/*弹出框----修改密码*/
	art.dialog({
			/*修改密码*/
			title:getJsLocaleMessage("common","common_passwdChange"),
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
				/*旧密码不能为空*/
				$("#errorPWD").html(getJsLocaleMessage("txgl","txgl_js_install_7"));
				return false;
			}
			if(""==newPwd){
				/*新密码不能为空*/
				$("#errorPWD").html(getJsLocaleMessage("common","common_frame2_main_45"));
				return false;
			}
			if(""==confirmPwd){
				/*确认密码不能为空*/
				$("#errorPWD").html(getJsLocaleMessage("txgl","txgl_js_install_8"));
				return false;
			}
			if(confirmPwd!=newPwd){
				/*新密码与确认密码不一致*/
				$("#errorPWD").html(getJsLocaleMessage("txgl","txgl_js_install_9"));
				return false;
			}
			
			if(passCheck=="false"){
				/*原密码输入错误*/
				$('#errorPWD').html(getJsLocaleMessage("common","common_frame2_main_39"));
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
 
function switchLang(){/*弹出框----切换语言*/
	//$("#switchLang").css("display","block");

	var lang = $("#switchLang").val();
	if (lang == 'zh_TW' || lang == 'zh_HK' || lang == 'zh_CN') {
		var pathUrl = $("#pathUrl").val();
		$.post(pathUrl + "/emp_tz.htm", {
			method : "setLangeuage",
			lang : lang,
			//userid: null,
			isAsync: "yes"
		}, function(result) {
			if (result == "true") {
				//var menu=$("#menu").html();
				 //$("#editWyGateFrame",window.parent.document).attr("src",pathUrl + "/systemManage.htm?method=toSkip");
//				 if(menu!=null&&menu!=''){
//					 //打开模块
//					
//					// refreshLanguage();
//					 //refreshPersonSet();
//					 //refreshBalance();
//					 //refreshSkin();
//					 //showImg();
//					 $(window.parent.document).contents().find("#mainFrame")[0].contentWindow.doOpen1(menu); 
//				 }else{
//					$("#themeForm").attr("action", pathUrl + "/systemManage.htm?method=toSkip");
//					$("#themeForm").submit();
//				 }
//				$("#editWyGateFrame").attr("action", pathUrl + "/systemManage.htm?method=toSkip");
			window.parent.location.href=pathUrl + "/systemManage.htm?method=toSkip";
			}
		});
	}
	return false;

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
					/*原密码输入正确*/
					$('#corrPWD').html(getJsLocaleMessage("common","common_frame2_main_38"));
				}else if(result=="false")
				{
					$('#corrPWD').html("");
					/*原密码输入错误*/
					$('#errorPWD').html(getJsLocaleMessage("common","common_frame2_main_39"));
				}
			}
		);
	}else
	{
		/*请输入原密码*/
		$('#zhuPass').html(getJsLocaleMessage("common","common_frame2_main_40"));
	}
}
//增加地址行
function addCols(){
	var quesType = $("#quesType").val();
	var cols = $("#initCols");
	
	var sontr=cols.find("tr");
	if(sontr.length==10){
		/*备用地址已达5对，无法再添加！*/
		alert(getJsLocaleMessage("txgl","txgl_js_install_10"));
		return;
	}
	var colNum = sontr.length/2+1;
	cols.append('<tr id="listinner'+colNum+'">'+
	/*备用内网地址  备用内网地址*/
	'<td width="150">'+getJsLocaleMessage("txgl","txgl_js_install_5")+' '+colNum+'：</td>'+
	'<td><input type="text" name="bakinnerUrl'+colNum+'" id="bakinnerUrl'+colNum+'" value="" class="inpStyle w335" placeholder="·'+getJsLocaleMessage("txgl","txgl_js_install_5")+' '+colNum+'"></td>'+
	'<td class="tips01"></td>'+
	'</tr><tr id="listout'+colNum+'">'+
	/*备用外网地址  备用外网地址   删除*/
	'<td>'+getJsLocaleMessage("txgl","txgl_js_install_6")+' '+colNum+'：</td>'+
	'<td><input type="text" name="bakouterUrl'+colNum+'" id="bakouterUrl'+colNum+'" value="" class="inpStyle w335" placeholder="·'+getJsLocaleMessage("txgl","txgl_js_install_6")+' '+colNum+'"></td>'+
	'<td><a onclick="javascript:delCols('+colNum+')" style="float: left;display: block;line-height: 26px;margin-left: 25px;cursor: pointer;margin-top: 6px;">'+getJsLocaleMessage("common","common_delete")+'</a></td>'+
	'</tr>');
	

}

function delCols(obj){
	$("#listinner"+obj).remove();
	$("#listout"+obj).remove();
}

function db_more_cg(){/*弹出框----数据库更多配置参数*/
	art.dialog({
			/*数据库更多配置参数*/
			title:getJsLocaleMessage("txgl","txgl_js_install_11"),
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
				/*最大连接数为数字！*/
				alert(getJsLocaleMessage("txgl","txgl_js_install_12"));
				location.reload();
				return;
			}
		}
		if(maxPoolSize==null || maxPoolSize.length==0){
			/*最大连接数不能为空！*/
			alert(getJsLocaleMessage("txgl","txgl_js_install_13"));
			location.reload();
			return;
		}
		if(minPoolSize!=""){
			if(!check.test(minPoolSize)){
				/*最小连接数为数字！*/
				alert(getJsLocaleMessage("txgl","txgl_js_install_14"));
				location.reload();
				return;
			}
		}
		if(minPoolSize==null || minPoolSize.length==0){
			/*最小连接数不能为空！*/
			alert(getJsLocaleMessage("txgl","txgl_js_install_15"));
			location.reload();
			return;
		}
		
		if(parseInt(maxPoolSize)<parseInt(minPoolSize)){
			/*最大连接数不能小于最小连接数！*/
			alert(getJsLocaleMessage("txgl","txgl_js_install_16"));
			location.reload();
			return;	
		}
		
		
		if(InitialPoolSize!=""){
			if(!check.test(InitialPoolSize)){
				/*初始连接数为数字！*/
				alert(getJsLocaleMessage("txgl","txgl_js_install_17"));
				location.reload();
				return;
			}
		}
		if(InitialPoolSize==null || InitialPoolSize.length==0){
			/*初始连接数不能为空！*/
			alert(getJsLocaleMessage("txgl","txgl_js_install_18"));
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
			/*确定*/
		    okVal:getJsLocaleMessage("common","common_confirm"),
		    button: [{
                /*恢复默认设置*/
		    	name: getJsLocaleMessage("txgl","txgl_js_install_19"),
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
			/*web信息更多配置参数*/
			title:getJsLocaleMessage("txgl","txgl_js_install_20"),
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
					/*默认页面大小为数字！*/
					alert(getJsLocaleMessage("txgl","txgl_js_install_21"));
					location.reload();
					return;
				}
			}
			var $yn = $(":radio[name=langSelect]:checked");
			var $langName = $(":checkbox[name=langName]:checked");
			var defaultLanguage = $("input#defaultLanguage").val();
			var langEnable = $yn.val();
			var langs = "";
                $langName.each(function () {
                    langs += $(this).val()+",";
                });
			if($yn.val()==="Yes" && $langName.val()==null){
                /*您没有选择任何语言，系统将会使用当前默认语言：！*/
                alert(getJsLocaleMessage("txgl","txgl_js_langSelect")+getJsLocaleMessage("txgl","txgl_js_common_"+defaultLanguage));
                location.reload();
                return;
			}
			if(defaultPageSize==null || defaultPageSize.length==0){
				/*默认页面大小不能为空！*/
				alert(getJsLocaleMessage("txgl","txgl_js_langSelect"));
				location.reload();
				return;
			}
			$.post("systemManage.htm",
					{
					method:"webSubmit",
					defaultPageSize:defaultPageSize,
					frame:frame,
					langs:langs.substr(0,langs.lastIndexOf(",")),
					defaultLanguage:defaultLanguage,
					langEnable:langEnable
					},
					 function(result){				
					closeDiv('1001',result);
					});
		        return false;
		    },

			/*确定*/
			okVal:getJsLocaleMessage("common","common_confirm"),
		    button: [{
                /*恢复默认设置*/
                name: getJsLocaleMessage("txgl","txgl_js_install_19"),
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
			/*修改密码成功！*/
			alert(getJsLocaleMessage("txgl","txgl_js_install_23"));
		}
			if(executResult=="differ"){
			/*旧密码不对！*/
			alert(getJsLocaleMessage("txgl","txgl_js_install_24"));
		}
			if(executResult=="confsucc"){
			/*修改配置项成功！*/
			alert(getJsLocaleMessage("txgl","txgl_js_install_25"));
		}
		if(executResult=="conffail"){
			/*修改配置项失败！*/
			alert(getJsLocaleMessage("txgl","txgl_js_install_26"));
		}
		if(executResult=="defalutSucc"){
            /*恢复默认值成功！*/
            alert(getJsLocaleMessage("txgl","txgl_js_install_27"));
		}
			if(executResult=="savesucess"){
			$("#errorIP").html("")
			/*保存成功！*/
			alert(getJsLocaleMessage("txgl","txgl_js_install_28"));
		}
			if(executResult=="defalutFail"){
			/*恢复默认值失败！*/
			alert(getJsLocaleMessage("txgl","txgl_js_install_29"));
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
				/*正在测试连接，请稍候......*/
                $("span#waitTextConnection").text(getJsLocaleMessage("txgl","txgl_js_install_30"));
            },
            complete:function(){
                $("span#waitTextConnection").text("");
                $("#testDBConnBtn").attr("disabled",false);
            },
			success:function(data){
                if("true" === data){
                    /*连接成功!*/
                    $("span#rightTextConnection").text(getJsLocaleMessage("txgl","txgl_js_install_32"));
				}else{
                    /*连接失败!*/
                    $("span#errorTextConnection").text(getJsLocaleMessage("txgl","txgl_js_install_31"));
				}
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
                /*正在测试连接，请稍候......*/
                $("#waitTextConnection2").text(getJsLocaleMessage("txgl","txgl_js_install_30"));
            },
            complete:function(){
                $("#waitTextConnection2").text("");
                $("#testBackConnBtn").attr("disabled",false);
            },
			success:function(data){
                var isSuc = ("true" == data);
                /*连接失败!*/
                document.getElementById("errorTextConnection2").innerHTML = (isSuc?"":getJsLocaleMessage("txgl","txgl_js_install_31"));
                /*连接成功!*/
                document.getElementById("rightTextConnection2").innerHTML = (isSuc?getJsLocaleMessage("txgl","txgl_js_install_32"):"");
			}
		})
	}
}

	function submit(){
        var $yn = $(":radio[name=langSelect]:checked");
        var $langName = $(":checkbox[name=langName]:checked");
        var defaultLanguage = $("input#defaultLanguage").val();
        var langEnable = $yn.val();
        var langs = "";
        $langName.each(function () {
            langs += $(this).val()+",";
        });
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
					/*内网地址格式不对*/
					$("#errorIP").html(getJsLocaleMessage("txgl","txgl_js_install_33"));
					return ;
				}
			}
			
			var outerUrl=$("#outerUrl").attr("value");
			if(outerUrl!=""){
				if(outerUrl.indexOf("http://")==-1){
					/*外网地址格式不对*/
					$("#errorIP").html(getJsLocaleMessage("txgl","txgl_js_install_34"));
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
					/*备用内网地址  的地址为空*/
					$("#errorIP").html(getJsLocaleMessage("txgl","txgl_js_install_5")+" "+i+getJsLocaleMessage("txgl","txgl_js_install_35"));
					return;
				}else if(bakinnerUrl!=undefined&&bakinnerUrl.indexOf("http://")==-1){
                    	/*备用内网地址 的地址格式不对*/
						$("#errorIP").html(getJsLocaleMessage("txgl","txgl_js_install_5")+" "+i+getJsLocaleMessage("txgl","txgl_js_install_36"));
						return ;
					}
				var bakouterUrl=$("#bakouterUrl"+i).attr("value");
				if(bakouterUrl==""){
					/*备用外网地址  的地址为空*/
					$("#errorIP").html(getJsLocaleMessage("txgl","txgl_js_install_6")+" "+i+getJsLocaleMessage("txgl","txgl_js_install_35"));
					return;
				}else if(bakouterUrl!=undefined&&bakouterUrl.indexOf("http://")==-1){
                    	/*备用外网地址  的地址格式不对*/
						$("#errorIP").html(getJsLocaleMessage("txgl","txgl_js_install_6")+" "+i+getJsLocaleMessage("txgl","txgl_js_install_36"));
						return ;
					}
			}
			}
			var EMPaddress=$("#EMPaddress").attr("value");
			if(EMPaddress!=""){
				if(EMPaddress.indexOf("http:")==-1){
					$("#errorIP").html("");
					/*EMP访问地址格式不对*/
					$("#errorIP").html(getJsLocaleMessage("txgl","txgl_js_install_37"));
					return ;
				}
			}
			var EMPOuterAddress=$("#EMPOuterAddress").attr("value");
			if(EMPOuterAddress!=""){
				if(EMPOuterAddress.indexOf("http:")==-1){
					$("#errorIP").html("");
					/*EMP访问地址格式不对*/
					$("#errorIP").html(getJsLocaleMessage("txgl","txgl_js_install_37"));
					return ;
				}
			}
			
			var webgate=$("#webgate").attr("value");
			if(webgate!=""){
				if(webgate.indexOf("http://")==-1){
					/*网关通讯地址格式不对*/
					$("#errorIP").html(getJsLocaleMessage("txgl","txgl_js_install_38"));
					return ;
				}
			}
			
			var EMPwxaddress=$("#EMPwxaddress").attr("value");
			if(EMPwxaddress!=""){
				if(EMPwxaddress.indexOf("http://")==-1){
					/*网讯站点访问地址格式不对*/
					$("#errorIP").html(getJsLocaleMessage("txgl","txgl_js_install_39"));
					return ;
				}
			}
			//EMP日志文件存储地址
			var loggeraddress=$("#loggeraddress").attr("value");
			if(loggeraddress==""){
				/*EMP日志文件存储地址不能为空*/
				$("#errorIP").html(getJsLocaleMessage("txgl","txgl_js_install_40"));
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
			var smsSplit = $(":radio[name=smsSplit]:checked").val();
			
			//EMP日志文件默认存储地址
			var defaultloggeraddress=$("#defaultloggeraddress").attr("value");
			var type=$("#type").attr("value");
			$.post("systemManage.htm",
				{
				method:"update",
				EMPaddress:EMPaddress,
				EMPOuterAddress:EMPOuterAddress,
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
				dbPwd:dbPwd,
				langs:langs.substr(0,langs.lastIndexOf(",")),
				defaultLanguage:defaultLanguage,
				langEnable:langEnable,
				smsSplit:smsSplit
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
			/*注：备用端口不能为空*/
			$("#errorTextConnection2").html(getJsLocaleMessage("txgl","txgl_js_install_41"));
			return false;
		}
		
		if(!portPatrn.exec(port)){
			/*注：备用端口请输入0到65535的整数*/
			$("#errorTextConnection2").html(getJsLocaleMessage("txgl","txgl_js_install_42"));
			return false;
		}
		
		if(""==dbconIp){
			/*注：备用数据库地址不能为空*/
			$("#errorTextConnection2").html(getJsLocaleMessage("txgl","txgl_js_install_43"));
			return false;
		}
		if(!(urlPatrn.exec(dbconIp) || ipPatrn.exec(dbconIp))){
			/*注：备用数据库地址不合法*/
			$("#errorTextConnection2").html(getJsLocaleMessage("txgl","txgl_js_install_44"));
			return false;
		}
		if(name==null || name.length==0){
			if($("#DBType").val() == 1)
			{
				/*注：备用服务名/实例不能为空*/
				$("#errorTextConnection2").html(getJsLocaleMessage("txgl","txgl_js_install_45"));
			}
			else
			{
				/*注：备用数据库名称不能为空*/
				$("#errorTextConnection2").html(getJsLocaleMessage("txgl","txgl_js_install_46"));
			}
			return false;
		}
		if(dbUser==null || dbUser.length==0){
			/*注：备用用户名不能为空*/
			$("#errorTextConnection2").html(getJsLocaleMessage("txgl","txgl_js_install_47"));
			return false;
		}
		if(type==null || type.length==0){
			/*注：数据库连接池不能为空*/
			$("#errorTextConnection2").html(getJsLocaleMessage("txgl","txgl_js_install_48"));
			return false;
		}else{
			$("#type").val(type);
		}
		if(!pwdPatrn.exec(dbPwd)){
			/*注：备用密码只能1-20位*/
			$("#errorTextConnection2").html(getJsLocaleMessage("txgl","txgl_js_install_49"));
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
		/*注：端口不能为空*/
		$("#errorTextConnection").html(getJsLocaleMessage("txgl","txgl_js_install_50"));
		return false;
	}
	
	if(!portPatrn.exec(port)){
		/*注：端口请输入0到65535的整数*/
		$("#errorTextConnection").html(getJsLocaleMessage("txgl","txgl_js_install_51"));
		return false;
	}
	
	if(""==dbconIp){
		/*注：数据库地址不能为空*/
		$("#errorTextConnection").html(getJsLocaleMessage("txgl","txgl_js_install_52"));
		return false;
	}
	if(!(urlPatrn.exec(dbconIp) || ipPatrn.exec(dbconIp))){
		/*注：数据库地址不合法*/
		$("#errorTextConnection").html(getJsLocaleMessage("txgl","txgl_js_install_53"));
		return false;
	}
	if(name==null || name.length==0){
		if($("#DBType").val() == 1)
		{
			/*注：服务名/实例不能为空*/
			$("#errorTextConnection").html(getJsLocaleMessage("txgl","txgl_js_install_54"));
		}
		else
		{
			/*注：数据库名称不能为空*/
			$("#errorTextConnection").html(getJsLocaleMessage("txgl","txgl_js_install_55"));
		}
		return false;
	}
	if(dbUser==null || dbUser.length==0){
		/*注：用户名不能为空*/
		$("#errorTextConnection").html(getJsLocaleMessage("txgl","txgl_js_install_56"));
		return false;
	}
	if(type==null || type.length==0){
		/*注：数据库连接池不能为空*/
		$("#errorTextConnection").html(getJsLocaleMessage("txgl","txgl_js_install_48"));
		return false;
	}else{
		$("#type").val(type);
	}
	if(!pwdPatrn.exec(dbPwd)){
		/*注：密码只能1-20位*/
		$("#errorTextConnection").html(getJsLocaleMessage("txgl","txgl_js_install_57"));
		return false;
	}else{
		$("#errorTextConnection").html("");
		return true;
	}
}