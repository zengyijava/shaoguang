//弹出DIV层
function showAddSmsTmp(title)
{
	
	$("#tmTailtype").val("2");
	$("#spTableDiv").show();
   	$("#busTableDiv").hide();
   	$("#tmTailtype").attr("disabled",false);
   	$("#tmTitle").attr("disabled",false);
//如果有内容清空信息
	$("#tmMsg").val("");
	$("#tmTitle").val("");
	$("#strlen").html("0");
	$("#msgtep").css("display","block");
	$("#getChooseMan").empty();
	$("#getChooseManSp").empty();
	//默认是0
	$("#manCount").html(0);
	$("#manCountSp").html(0);
	//是否是修改 的标识符
	$("#updateid").val("");
	//防止查询后，再次点击新建，还保存之前信息
	var epname=$("#epname").val();
	if(epname!=""){
		$("#epname").val("");//设置为空，然后重新查询一遍
	}
	var epnameSp=$("#epnameSp").val();
	if(epnameSp!=""){
		$("#epnameSp").val("");//设置为空，然后重新查询一遍
	}
	fristSearchSp();
	fristSearchBus();
	$("#msgtep").dialog({
		autoOpen: false,
		width: 660,
		title:title,
		resizable:false,
		modal: true,
		open:function(){
			
		},
		close:function(){
			$("#msgtep").css("display","none");
			$("#epname").val("");
			$("#epnameSp").val("");
			$("#right").empty();
			$("#rightSp").empty();
			$("#getChooseMan").empty();
			$("#getChooseManSp").empty();
			$("#rightSelectTemp").empty();
			$("#rightSelectTempSp").empty();
		}
	});
       
	$("#msgtep").dialog("open");
}

	//点击查询
	function fristSearchBus(){
	    var epname = $("#epname").val();
        epname=$.trim(epname);
	    //left
		$.ajax({
			type:"POST",
			url: "tai_msgTailMgr.htm",
			data: {method: "search",epname:epname,theTailtype:"1",isAsync:"yes"},
			success: function(result){
				var data = eval("("+result+")");
				if(data.length==0){
					$("#left").html("");
					return;
				}
				$("#left").html("");
				for(var i= 0;i<data.length;i++){
					var bus_code = data[i].bus_code;
					var bus_name = data[i].bus_name;
					var bus_id = data[i].bus_id;
					var tablelink= data[i].tablelink;
					var tail_nameBus=data[i].tail_nameBus;
					if(tablelink!=""){
						$("#left").append("<option value=\'"+bus_id+"\' style='color:#CDCDCD;height: 22px;' title='"+getJsLocaleMessage("xtgl","xtgl_cswh_twgl_73")+"("+tail_nameBus+")"+getJsLocaleMessage("xtgl","xtgl_cswh_twgl_74")+"' ismove='not'>"+bus_name+"("+bus_code+")</option>");
					}else{
						$("#left").append("<option value=\'"+bus_id+"\' style='height: 22px;' title='"+bus_name+"("+bus_code+")' >"+bus_name+"("+bus_code+")</option>");
					}
				}
			},
			error: function(){alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_76"));}
		})
	}		
	function fristSearchSp(){
		var epnameSp = $("#epnameSp").val();
        epnameSp=$.trim(epnameSp);
	    //left
		$.ajax({
			type:"POST",
			url: "tai_msgTailMgr.htm",
			data: {method: "search",epnameSp:epnameSp,theTailtype:"2",isAsync:"yes"},
			success: function(result){
				var data = eval("("+result+")");
				if(data.length==0){
					$("#leftSp").html("");
					return;
				}
				$("#leftSp").html("");
				for(var i= 0;i<data.length;i++){
					var userid = data[i].userid;
					var staffname = data[i].staffname;
					var tablelink= data[i].tablelink;
					var tail_nameSp=data[i].tail_nameSp;
					if(tablelink!=""){
						$("#leftSp").append("<option value=\'"+userid+"\' style='color:#CDCDCD;height: 22px;' title='"+getJsLocaleMessage("xtgl","xtgl_cswh_twgl_73")+"("+tail_nameSp+")"+getJsLocaleMessage("xtgl","xtgl_cswh_twgl_74")+"' ismove='not'>"+staffname+"("+userid+")</option>");
					}else{
						$("#leftSp").append("<option value=\'"+userid+"\' style='height: 22px;' title='"+staffname+"("+userid+")' >"+staffname+"("+userid+")</option>");
					}
				}
			},
			error: function(){alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_76"));}
		})
	}



//显示贴尾内容
function modify(t)
{
	//如果其他的几个弹出了，先关闭其他几个
	$('#tailName').dialog('close');
	$('#buslist').dialog('close');
	$('#modify').dialog({
		autoOpen: false,
		width:265,
	    height:200
	});
	$("#msgshow").empty();
	$("#msgshow").text($(t).children("label").children("xmp").text());
	$('#modify').dialog('open');
}
//适用范围 用于业务和SP账号
function showBus(t)
{
	$('#tailName').dialog('close');
	$('#modify').dialog('close');
	$('#buslist').dialog({
		autoOpen: false,
		width:265,
	    height:200
	});
	$("#msgshow2").empty();
	$("#msgshow2").text($(t).children("label").children("xmp").text());
	$('#buslist').dialog('open');
}





//贴尾名称
function showName(t)
{
	$('#buslist').dialog('close');
	$('#modify').dialog('close');
	$('#tailName').dialog({
		autoOpen: false,
		width:250,
	    height:200
	});
	$("#msgshow3").empty();
	$("#msgshow3").text($(t).children("label").children("xmp").text());
	$('#tailName').dialog('open');
}


//全选
function checkAlls(e,str)    
{  
	var selectid=",";
	var a = document.getElementsByName(str);    
	var n = a.length;  


		for (var i=0; i<n; i=i+1)   
		{
			a[i].checked =e.checked; 
			selectid=selectid+a[i].value+",";
		}
		if(!e.checked){
			selectid=",";
		}
	$("#selectid").val(selectid);
}

function selectIt(e){
	if(e.checked){
		var id=$("#selectid").val()+e.value+",";
		$("#selectid").val(id);
	}else{
		$("#checkall").attr("checked",false);
		var old=$("#selectid").val();
		var newval=old.replace(","+e.value+",",",");
		$("#selectid").val(newval);
	}
}



	//关闭弹出层
	function closeDiv(){
		modif=false;
		$("#msgtep").dialog("close");
		$("#epname").val("");
		$("#right").empty();
		$("#rightSp").empty();
		$("#getChooseMan").empty();
		$("#getChooseManSp").empty();
		$("#rightSelectTemp").empty();
		$("#rightSelectTempSp").empty();
		
	}
	//保存内容
	function save(){
		var tmMsg=$("#tmMsg").val();
		var lguserid=$("#lguserid").val();
		var lgcorpcode=$("#lgcorpcode").val();
		var tmTitle=$("#tmTitle").val();
		var modfid= $("#updateid").val();
		var theTailtype = $('#tmTailtype').val();
		//去空处理
		tmTitle=$.trim(tmTitle);
		tmMsg=$.trim(tmMsg);
		
		if(tmTitle==""){
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_77"));
			return;
		}
		var pattern=/[`~@#\^\*_\+\\\/'\[\]]/im; 
		if(pattern.test(tmTitle)){
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_78"));
			return;
		}
		
		if(tmMsg==""){
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_79"));
			return;
		}
		
		if(pattern.test(tmMsg)){
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_80"));
			return;
		}
		if($('#tmMsg').val().length>128){
			  alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_81"));
			  $('#tmMsg').select();
			  return;
		} 
		
		if(theTailtype == 1){
			if ($("#getChooseMan li").size() < 1) {
				alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_82"));
				return;
			}
		}else{
			if ($("#getChooseManSp li").size() < 1) {
				alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_83"));
				return;
			}
		}
		
		
		
		var bussids="";
		$("#getChooseMan li").each(function() {
			var dataval = $(this).attr("dataval");
			if (dataval != null && dataval != "") {
				bussids=bussids + dataval + ",";
			}
		});
		//获得busscodes  (因emp6.1标准版新需求中，数据库tailbind表中与业务关联的字段为buscode，所以这里新增)
		var busscodes="";
		$("#getChooseMan li").each(function() {
			var nameandcode = $(this).text();
			if(nameandcode != null && nameandcode != ""){
				var code1 = nameandcode.split("(")[1];
				var code2 = code1.substring(0,code1.length-1);
				if (code2 != null && code2 != "") {
					busscodes=busscodes + code2 + ",";
				}
			}
			
		});
		
		var spsids="";
		$("#getChooseManSp li").each(function() {
			var dataval = $(this).attr("dataval");
			if (dataval != null && dataval != "") {
				spsids=spsids + dataval + ",";
			}
		});
		
		
		//如果是业务
		if(theTailtype == 1){
			//如果判断是修改状态
			if(modfid!=""){
				$.ajax({
					type:"POST",
					url: "tai_msgTailMgr.htm",
					data: {method: "update",tmMsg:tmMsg,tmTitle:tmTitle,lguserid:lguserid,lgcorpcode:lgcorpcode,spsids:spsids,bussids:bussids,busscodes:busscodes,theTailtype:theTailtype,modfid:modfid,isAsync:"yes"},
					success: function(result){
						if(result != null &&result=='success'){
							alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_84"));
							window.location.href="tai_msgTailMgr.htm?method=find&refresh=true&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;
							
						}else if(result != null &&result=='fail'){
							alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_85"));
						}else if(result != null &&result != ""){
							//如果是关键字 去掉前缀10位
							if(result.length>10){
								alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_86")+result.substring(10));
							}else if(result.length==10){
								alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_87"));
							}
							return;
						}
					},
					error: function(){alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_76"));}
				})
			}else{
				$.ajax({
					type:"POST",
					url: "tai_msgTailMgr.htm",
					data: {method: "save",tmMsg:tmMsg,tmTitle:tmTitle,lguserid:lguserid,lgcorpcode:lgcorpcode,spsids:spsids,bussids:bussids,busscodes:busscodes,theTailtype:theTailtype,isAsync:"yes"},
					success: function(result){
						if(result != null &&result=='success'){
							alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_88"));
							window.location.href="tai_msgTailMgr.htm?method=find&refresh=true&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;
							
						}else if(result != null &&result=='fail'){
							alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_89"));
						}else if(result != null &&result != ""){
							//如果是关键字 去掉前缀10位
							if(result.length>10){
								alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_86")+result.substring(10));
							}else if(result.length==10){
								alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_87"));
							}
							return;
						}
					},
					error: function(){alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_76"));}
				})
			}
		//如果是SP帐号
		}else{
			//如果判断是修改状态
			if(modfid!=""){
				$.ajax({
					type:"POST",
					url: "tai_msgTailMgr.htm",
					data: {method: "update",tmMsg:tmMsg,tmTitle:tmTitle,lguserid:lguserid,lgcorpcode:lgcorpcode,spsids:spsids,bussids:bussids,busscodes:busscodes,theTailtype:theTailtype,modfid:modfid,isAsync:"yes"},
					success: function(result){
						if(result != null &&result=='success'){
							alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_90"));
							window.location.href="tai_msgTailMgr.htm?method=find&refresh=true&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;
							
						}else if(result != null &&result=='fail'){
							alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_91"));
						}else if(result != null &&result != ""){
							//如果是关键字 去掉前缀10位
							if(result.length>10){
								alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_86")+result.substring(10));
							}else if(result.length==10){
								alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_87"));
							}
							return;
						}
					},
					error: function(){alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_76"));}
				})
			}else{
				$.ajax({
					type:"POST",
					url: "tai_msgTailMgr.htm",
					data: {method: "save",tmMsg:tmMsg,tmTitle:tmTitle,lguserid:lguserid,lgcorpcode:lgcorpcode,spsids:spsids,bussids:bussids,busscodes:busscodes,theTailtype:theTailtype,isAsync:"yes"},
					success: function(result){
						if(result != null &&result=='success'){
							alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_92"));
							window.location.href="tai_msgTailMgr.htm?method=find&refresh=true&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;
							
						}else if(result != null &&result=='fail'){
							alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_93"));
						}else if(result != null &&result != ""){
							//如果是关键字 去掉前缀10位
							if(result.length>10){
								alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_86")+result.substring(10));
							}else if(result.length==10){
								alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_87"));
							}
							return;
						}
					},
					error: function(){alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_76"));}
				})
			}
		}
	}
	//开始时间
	function rtime(){
		var max = "2099-12-31 23:59:59";
		var v = $("#startSubmitTime").attr("value");
		if(v.length != 0)
		{
		    var year = v.substring(0,4);
			var mon = v.substring(5,7);
			var day = 31;
			if (mon != "12")
			{
			    mon = String(parseInt(mon,10)+1);
			    if (mon.length == 1)
			    {
			        mon = "0"+mon;
			    }
			    switch(mon){
				    case "01":day = 31;break;
				    case "02":day = 28;break;
				    case "03":day = 31;break;
				    case "04":day = 30;break;
				    case "05":day = 31;break;
				    case "06":day = 30;break;
				    case "07":day = 31;break;
				    case "08":day = 31;break;
				    case "09":day = 30;break;
				    case "10":day = 31;break;
				    case "11":day = 30;break;
			    }
				}
				else
				{
				    year = String((parseInt(year,10)+1));
				    mon = "01";
				}
				max = year+"-"+mon+"-"+day+" 23:59:59"
			}
			WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:v,maxDate:max});
		
		};
		//时间控件处理
		function stime(){
			    var max = "2099-12-31 23:59:59";
		    var v = $("#endSubmitTime").attr("value");
		    var min = "1900-01-01 00:00:00";
			if(v.length != 0)
			{
			    max = v;
			    var year = v.substring(0,4);
				var mon = v.substring(5,7);
				if (mon != "01")
				{
				    mon = String(parseInt(mon,10)-1);
				    if (mon.length == 1)
				    {
				        mon = "0"+mon;
				    }
				}
				else
				{
				    year = String((parseInt(year,10)-1));
				    mon = "12";
				}
				min = year+"-"+mon+"-01 00:00:00"
			}
			WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:max});
		
	};


//是否修改标识符
	var modif=false;
	function modifSmsTmp(tailid,title,tailtype){
		var manCount=0;
		var manCountSp=0;;
		modif=true;
		
		$("#left").empty();
		$("#leftSp").empty();
		
		//如果是业务
		if(tailtype == '1'){
			$("#right").empty();
			$("#getChooseMan").empty();
			$("#rightSelectTemp").empty();
			
			
			$("#tmTitle").empty();
			$("#tmMsg").empty();
			$("#updateid").empty();
			$("#strlen").empty();
			
			$("#spTableDiv").hide();
		   	$("#busTableDiv").show();
			
			$.ajax({
				type:"POST",
				url: "tai_msgTailMgr.htm",
				data: {method: "getTailByID",tailid:tailid,theTailtype:tailtype,isAsync:"yes"},
				success: function(result){
					var data = eval("("+result+")");
					$("#tmTailtype").val(data.tail_type);
					$("#tmTailtype").attr("disabled","disabled");
					$("#tmTitle").val(data.tail_name);
					$("#tmTitle").attr("disabled","disabled");
					$("#tmMsg").val(data.content);
					$("#updateid").val(data.tailid);
					$("#strlen").text(data.content.length);
					var jsonarray=data.updateSelectBus;
					for(var i=0;i<jsonarray.length;i++){
						var forbidden ='';
						var bus_id = jsonarray[i].bus_id;
						var bus_name = jsonarray[i].bus_name;
						var bus_code = jsonarray[i].bus_code;
						var state = jsonarray[i].state;
						if(state=='1'){
							forbidden=" <font color='red'>"+getJsLocaleMessage("xtgl","xtgl_cswh_twgl_70")+"</font>";
						}
						manCount=manCount+1;
						$("#right").append("<option value=\'"+bus_id+"\' >"+bus_name+"("+bus_code+")</option>");
						$("#getChooseMan").append("<li dataval='"+bus_id+"'>"+bus_name+"("+bus_code+")"+forbidden+"</li>");
						$("#rightSelectTemp").append("<option value=\'"+bus_id+"\'>"+bus_name+"("+bus_code+")</option>");
					}
					$("#manCount").html(manCount);
				},
				error: function(){alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_76"));}
			})
			
			//防止查询后，再次点击新建，还保存之前信息
			var epname=$("#epname").val();
			if(epname!=""){
				$("#epname").val("");//设置为空，然后重新查询一遍
			}
			searchName();
			
			
		//如果是SP账号
		}
		if(tailtype == '2'){
			$("#rightSp").empty();
			$("#getChooseManSp").empty();
			$("#rightSelectTempSp").empty();
			
			
			$("#tmTitle").empty();
			$("#tmMsg").empty();
			$("#updateid").empty();
			$("#strlen").empty();
			
			$("#spTableDiv").show();
		   	$("#busTableDiv").hide();
			
			$.ajax({
				type:"POST",
				url: "tai_msgTailMgr.htm",
				data: {method: "getTailByID",tailid:tailid,theTailtype:tailtype,isAsync:"yes"},
				success: function(result){
					var data = eval("("+result+")");
					$("#tmTailtype").val(data.tail_type);
					$("#tmTailtype").attr("disabled","disabled");
					$("#tmTitle").val(data.tail_name);
					$("#tmTitle").attr("disabled","disabled");
					$("#tmMsg").val(data.content);
					$("#updateid").val(data.tailid);
					$("#strlen").text(data.content.length);
					var jsonarray=data.updateSelectSp;
					for(var i=0;i<jsonarray.length;i++){
						var forbidden ='';
						var userid = jsonarray[i].userid;
						var staffname = jsonarray[i].staffname;
						manCountSp=manCountSp+1;
						$("#rightSp").append("<option value=\'"+userid+"\' >"+staffname+"("+userid+")</option>");
						$("#getChooseManSp").append("<li dataval='"+userid+"'>"+staffname+"("+userid+")</li>");
						$("#rightSelectTempSp").append("<option value=\'"+userid+"\'>"+staffname+"("+userid+")</option>");
					}
					$("#manCountSp").html(manCountSp);
				},
				error: function(){alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_76"));}
			})
			
			//防止查询后，再次点击新建，还保存之前信息
			var epnameSp=$("#epnameSp").val();
			if(epnameSp!=""){
				$("#epnameSp").val("");//设置为空，然后重新查询一遍
			}
			
			searchNameSp();
		}
		
		
		
		
		
		
		
		
		
		$("#msgtep").css("display","block");
		$("#msgtep").dialog({
			autoOpen: false,
			width: 660,
			title:title,
			resizable:false,
			modal: true,
			open:function(){
				
			},
			close:function(){
				$("#msgtep").css("display","none");
				modif=false;
			}
		});
	       
		$("#msgtep").dialog("open");
	}

	
	//删除单条记录
	function deleteMsg(id){
		var lguserid=$("#lguserid").val();
		var lgcorpcode=$("#lgcorpcode").val();
		if(confirm(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_94"))){
		$.ajax({
			type:"POST",
			url: "tai_msgTailMgr.htm",
			data: {method: "deleteByID",id:id,isAsync:"yes"},
			success: function(result){
				if(result=='success'){
					alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_5"));
					window.location.href="tai_msgTailMgr.htm?method=find&refresh=true&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;
				}else{
					alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_6"));
				}
			},
			error: function(){alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_76"));}
		})
	}
	}
	

	
	//删除选中记录
	function deleteSelect(){
		var lguserid=$("#lguserid").val();
		var lgcorpcode=$("#lgcorpcode").val();
		var id=$("#selectid").val();
		if(id==","||id==""){
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_95"));
			return;
		}
		
	if(confirm(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_96"))){
		$.ajax({
			type:"POST",
			url: "tai_msgTailMgr.htm",
			data: {method: "deleteSelect",id:id,isAsync:"yes"},
			success: function(result){
				if(result=='success'){
					alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_5"));
					window.location.href="tai_msgTailMgr.htm?method=find&refresh=true&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;
				}else{
					alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_6"));
				}
			},
			error: function(){alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_76"));}
		})
	 }
	}
	//点击选择  用于业务
	function router()
	{

		if($("#left").val()!=null && $("#left").val()!="")
		{
			moveLeft();
		}else{
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_97"));
			return;
		}
	}	
	
	//点击选择  用于SP账号
	function routerSp()
	{

		if($("#leftSp").val()!=null && $("#leftSp").val()!="")
		{
			moveLeftSp();
		}else{
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_98"));
			return;
		}
	}	
	
	//点击查询   用于业务
	function searchName(){
		//var theTailtype = $('#tmTailtype').val();
        var epname = $("#epname").val();
        epname=$.trim(epname);
        $("#epname").val(epname);
    	var pattern=/[`~!@#\$%\^\&\*\(\)_\+<>\?:"\{\},\.\\\/;'\[\]]/im;  
    	if(pattern.test(epname)){
    		alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_99"));
    		return;
    	}
        
        //left
		$.ajax({
			type:"POST",
			url: "tai_msgTailMgr.htm",
			data: {method: "search",epname:epname,theTailtype:"1",isAsync:"yes"},
			success: function(result){
				var data = eval("("+result+")");
				if(data.length==0){
					$("#left").html("");
					alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_100"));
					return;
				}
				$("#left").html("");
				for(var i= 0;i<data.length;i++){
					var bus_code = data[i].bus_code;
					var bus_name = data[i].bus_name;
					var bus_id = data[i].bus_id;
					var tablelink= data[i].tablelink;
					var tail_nameBus=data[i].tail_nameBus;
					if(tablelink!=""){
						$("#left").append("<option value=\'"+bus_id+"\' style='color:#CDCDCD;height: 22px;' title='"+getJsLocaleMessage("xtgl","xtgl_cswh_twgl_73")+"("+tail_nameBus+")"+getJsLocaleMessage("xtgl","xtgl_cswh_twgl_74")+"' ismove='not'>"+bus_name+"("+bus_code+")</option>");
					}else{
						$("#left").append("<option value=\'"+bus_id+"\' style='height: 22px;' title='"+bus_name+"("+bus_code+")' >"+bus_name+"("+bus_code+")</option>");
					}
				}
			},
			error: function(){alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_76"));}
		})
	}
	
	//点击查询  用于SP账号
	function searchNameSp(){
		//var theTailtype = $('#tmTailtype').val();
        var epnameSp = $("#epnameSp").val();
        epnameSp=$.trim(epnameSp);
        $("#epnameSp").val(epnameSp);
    	var pattern=/[`~!@#\$%\^\&\*\(\)_\+<>\?:"\{\},\.\\\/;'\[\]]/im;  
    	if(pattern.test(epnameSp)){
    		alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_99"));
    		return;
    	}
        
        //left
		$.ajax({
			type:"POST",
			url: "tai_msgTailMgr.htm",
			data: {method: "search",epnameSp:epnameSp,theTailtype:"2",isAsync:"yes"},
			success: function(result){
				var data = eval("("+result+")");
				if(data.length==0){
					$("#leftSp").html("");
					alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_100"));
					return;
				}
				$("#leftSp").html("");
				for(var i= 0;i<data.length;i++){
					var userid = data[i].userid;
					var staffname = data[i].staffname;
					var tablelink= data[i].tablelink;
					var tail_nameSp=data[i].tail_nameSp;
					if(tablelink!=""){
						$("#leftSp").append("<option value=\'"+userid+"\' style='color:#CDCDCD;height: 22px;' title='"+getJsLocaleMessage("xtgl","xtgl_cswh_twgl_73")+"("+tail_nameSp+")"+getJsLocaleMessage("xtgl","xtgl_cswh_twgl_74")+"' ismove='not'>"+staffname+"("+userid+")</option>");
					}else{
						$("#leftSp").append("<option value=\'"+userid+"\' style='height: 22px;' title='"+staffname+"("+userid+")' >"+staffname+"("+userid+")</option>");
					}
				}
			},
			error: function(){alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_76"));}
		})
	}
	
	
	//***************获取机构代码******	
	setting3 = {									
			async : true,				
			asyncUrl : "tai_msgTailMgr.htm?method=createDeptTree", //获取节点数据的URL地址
		    isSimpleData : true,
			rootPID : 0,
			treeNodeKey : "id",
			treeNodeParentKey : "pId",
			asyncParam: ["depId"],	
			
			callback: {
					
				click: zTreeOnClick3,					
				asyncSuccess:function(event, treeId, treeNode, msg){
					if(!treeNode){
					   var rootNode = zTree3.getNodeByParam("level", 0);
					   zTree3.expandNode(rootNode, true, false);
					}
				}
			}
	};
	
	//选中的机构显示文本框
	function zTreeOnClick3(event, treeId, treeNode) {
		if (treeNode) {				
			var pops="";
			var depts ="";
			$("#depNam").attr("value", treeNode.name);	
			$("#deptid").attr("value",treeNode.id);
		}
	}	
	
	
	//隐藏树形控件
	function showMenu() {
		var sortSel = $("#depNam");
		var sortOffset = $("#depNam").offset();
		$("#dropMenu").toggle();
	}
	//选中的机构显示文本框
	function zTreeOnClickOK3() {
		hideMenu();
	}	
	//隐藏树形控件
	function hideMenu() {
		$("#dropMenu").hide();
	}
	
	function cleanSelect_dep3()
	{
		var checkNodes = zTree3.getCheckedNodes();
	    for(var i=0;i<checkNodes.length;i++){
	     checkNodes[i].checked=false;
	    }
	    zTree3.refresh();
	    $("#depNam").attr("value", getJsLocaleMessage("xtgl","xtgl_cswh_twgl_101"));	
		$("#deptid").attr("value","");	
	}
	function reloadTree(zNodes3) {
		hideMenu();
		setting3.expandSpeed = ($.browser.msie && parseInt($.browser.version)<=7)?"":"fast";
		zTree3 = $("#dropdownMenu").zTree(setting3, zNodes3);
		zTree3.expandAll(true);		
	}
	
	