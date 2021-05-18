//弹出DIV层
function showAddSmsTmp(title)
{
//如果有内容清空信息
	$("#tmMsg").val("");
	$("#tmTitle").val("");
	$("#strlen").html("0");
	$("#msgtep").css("display","block");
	$("#getChooseMan").empty();
	//默认是0
	$("#manCount").html(0);
	//是否是修改 的标识符
	$("#updateid").val("");
	//防止查询后，再次点击新建，还保存之前信息
	var epname=$("#epname").val();
	if(epname!=""){
		$("#epname").val("");//设置为空，然后重新查询一遍
	}
	fristSearch();
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
			$("#right").empty();
			$("#getChooseMan").empty();
			$("#rightSelectTemp").empty();
		}
	});
       
	$("#msgtep").dialog("open");
}

	//点击查询
	function fristSearch(){
	    var epname = $("#epname").val();
        epname=$.trim(epname);
	    //left
		$.ajax({
			type:"POST",
			url: "ydyw_postTailMgr.htm",
			data: {method: "search",epname:epname,isAsync:"yes"},
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
					var bustail_name=data[i].bustail_name;
					if(tablelink!=""){
						$("#left").append("<option value=\'"+bus_id+"\' style='color:#CDCDCD;height: 22px;' title='"+getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_12")+"("+bustail_name+")"+getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_13")+"。' ismove='not'>"+bus_name+"("+bus_code+")</option>");
					}else{
						$("#left").append("<option value=\'"+bus_id+"\' style='height: 22px;' title='"+bus_name+"("+bus_code+")' >"+bus_name+"("+bus_code+")</option>");
					}
				}
			},
			error: function(){alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtcgl_text_51"));}
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
//适用业务
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
		$("#getChooseMan").empty();
		$("#rightSelectTemp").empty();
		
	}
	//保存内容
	function save(){
	var tmMsg=$("#tmMsg").val();
	var lguserid=$("#lguserid").val();
	var lgcorpcode=$("#lgcorpcode").val();
	var tmTitle=$("#tmTitle").val();
	var modfid= $("#updateid").val();
	//去空处理
	tmTitle=$.trim(tmTitle);
	tmMsg=$.trim(tmMsg);
	
	if(tmTitle==""){
		alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_15"));
		return;
	}
	var pattern=/[`~@#\^\*_\+\\\/'\[\]]/im; 
	if(pattern.test(tmTitle)){
		alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_16"));
		return;
	}
	
	if(tmMsg==""){
		alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_17"));
		return;
	}
	
	if(pattern.test(tmMsg)){
		alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_18"));
		return;
	}
	
	
	if ($("#getChooseMan li").size() < 1) {
		alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywbgl_text_14"));
		return;
	}
	var bussids="";
	$("#getChooseMan li").each(function() {
		var dataval = $(this).attr("dataval");
		if (dataval != null && dataval != "") {
			bussids=bussids + dataval + ",";
		}
	});
	//如果判断是修改状态
	if(modfid!=""){
		$.ajax({
			type:"POST",
			url: "ydyw_postTailMgr.htm",
			data: {method: "update",tmMsg:tmMsg,tmTitle:tmTitle,lguserid:lguserid,lgcorpcode:lgcorpcode,bussids:bussids,modfid:modfid,isAsync:"yes"},
			success: function(result){
				if(result != null &&result=='success'){
					alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_19"));
					window.location.href="ydyw_postTailMgr.htm?method=find&refresh=true&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;
					
				}else if(result != null &&result=='fail'){
					alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_20"));
				}else if(result != null &&result != ""){
					alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_21")+result);
					return;
				}
			},
			error: function(){alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtcgl_text_51"));}
		})
	}else{
		$.ajax({
			type:"POST",
			url: "ydyw_postTailMgr.htm",
			data: {method: "save",tmMsg:tmMsg,tmTitle:tmTitle,lguserid:lguserid,lgcorpcode:lgcorpcode,bussids:bussids,isAsync:"yes"},
			success: function(result){
				if(result != null &&result=='success'){
					alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_22"));
					window.location.href="ydyw_postTailMgr.htm?method=find&refresh=true&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;
					
				}else if(result != null &&result=='fail'){
					alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_23"));
				}else if(result != null &&result != ""){
					alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_21")+result);
					return;
				}
			},
			error: function(){alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtcgl_text_51"));}
		})
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
	function modifSmsTmp(tailid,title){
		var manCount=0;
		modif=true;
		$("#right").empty();
		$("#getChooseMan").empty();
		$("#rightSelectTemp").empty();
		$.ajax({
			type:"POST",
			url: "ydyw_postTailMgr.htm",
			data: {method: "getTailByID",tailid:tailid,isAsync:"yes"},
			success: function(result){
				var data = eval("("+result+")");
				$("#tmTitle").val(data.bustail_name);
				$("#tmMsg").val(data.content);
				$("#updateid").val(data.tailid);
				$("#strlen").text(data.content.length);
				var jsonarray=data.updateSelect;
				for(var i=0;i<jsonarray.length;i++){
					var forbidden ='';
					var bus_id = jsonarray[i].bus_id;
					var bus_name = jsonarray[i].bus_name;
					var bus_code = jsonarray[i].bus_code;
					var state = jsonarray[i].state;
					if(state=='1'){
						forbidden=" <font color='red'>"+getJsLocaleMessage("ydyw","ydyw_ywgl_ywbgl_text_5")+"</font>";
					}
					manCount=manCount+1;
					$("#right").append("<option value=\'"+bus_id+"\' >"+bus_name+"("+bus_code+")</option>");
					$("#getChooseMan").append("<li dataval='"+bus_id+"'>"+bus_name+"("+bus_code+")"+forbidden+"</li>");
					$("#rightSelectTemp").append("<option value=\'"+bus_id+"\'>"+bus_name+"("+bus_code+")</option>");
				}
				$("#manCount").html(manCount);
			},
			error: function(){alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtcgl_text_51"));}
		})
		
		//防止查询后，再次点击新建，还保存之前信息
		var epname=$("#epname").val();
		if(epname!=""){
			$("#epname").val("");//设置为空，然后重新查询一遍
		}
		searchName();
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
		if(confirm(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_24"))){
		$.ajax({
			type:"POST",
			url: "ydyw_postTailMgr.htm",
			data: {method: "deleteByID",id:id,isAsync:"yes"},
			success: function(result){
				if(result=='success'){
                    /*删除成功！*/
                    alert(getJsLocaleMessage("common","common_deleteSucceed"));
					window.location.href="ydyw_postTailMgr.htm?method=find&refresh=true&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;
				}else{
                    /*删除失败！*/
                    alert(getJsLocaleMessage("common","common_deleteFailed"));
				}
			},
			error: function(){alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtcgl_text_51"));}
		})
	}
	}
	

	
	//删除选中记录
	function deleteSelect(){
		var lguserid=$("#lguserid").val();
		var lgcorpcode=$("#lgcorpcode").val();
		var id=$("#selectid").val();
		if(id==","||id==""){
			alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywmbpz_text_31"));
			return;
		}
		
	if(confirm(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_25"))){
		$.ajax({
			type:"POST",
			url: "ydyw_postTailMgr.htm",
			data: {method: "deleteSelect",id:id,isAsync:"yes"},
			success: function(result){
				if(result=='success'){
					alert(getJsLocaleMessage("common","common_deleteSucceed"));
					window.location.href="ydyw_postTailMgr.htm?method=find&refresh=true&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;
				}else{
					alert(getJsLocaleMessage("common","common_deleteFailed"));
				}
			},
			error: function(){alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtcgl_text_51"));}
		})
	 }
	}
	//点击选择
	function router()
	{

		if($("#left").val()!=null && $("#left").val()!="")
		{
			moveLeft();
		}else{
			alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywbgl_text_19"));
			return;
		}
	}	
	
	//点击查询
	function searchName(){
        var epname = $("#epname").val();
        epname=$.trim(epname);
        $("#epname").val(epname);
    	var pattern=/[`~!@#\$%\^\&\*\(\)_\+<>\?:"\{\},\.\\\/;'\[\]]/im;  
    	if(pattern.test(epname)){
    		alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_26" +
				""));
    		return;
    	}
        
        //left
		$.ajax({
			type:"POST",
			url: "ydyw_postTailMgr.htm",
			data: {method: "search",epname:epname,isAsync:"yes"},
			success: function(result){
				var data = eval("("+result+")");
				if(data.length==0){
					$("#left").html("");
					alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywbgl_text_18"));
					return;
				}
				$("#left").html("");
				for(var i= 0;i<data.length;i++){
					var bus_code = data[i].bus_code;
					var bus_name = data[i].bus_name;
					var bus_id = data[i].bus_id;
					var tablelink= data[i].tablelink;
					var bustail_name=data[i].bustail_name;
					if(tablelink!=""){
						$("#left").append("<option value=\'"+bus_id+"\' style='color:#CDCDCD;height: 22px;' title='"+getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_12")+"("+bustail_name+")"+getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_13")+"。' ismove='not'>"+bus_name+"("+bus_code+")</option>");
					}else{
						$("#left").append("<option value=\'"+bus_id+"\' style='height: 22px;' title='"+bus_name+"("+bus_code+")' >"+bus_name+"("+bus_code+")</option>");
					}
				}
			},
			error: function(){alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtcgl_text_51"));}
		})
	}
	
	
	//***************获取机构代码******	
	setting3 = {									
			async : true,				
			asyncUrl : "ydyw_postTailMgr.htm?method=createDeptTree", //获取节点数据的URL地址
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
	    $("#depNam").attr("value", getJsLocaleMessage("common","common_pleaseSelect"));
		$("#deptid").attr("value","");	
	}
	function reloadTree(zNodes3) {
		hideMenu();
		setting3.expandSpeed = ($.browser.msie && parseInt($.browser.version)<=7)?"":"fast";
		zTree3 = $("#dropdownMenu").zTree(setting3, zNodes3);
		zTree3.expandAll(true);		
	}
	
	