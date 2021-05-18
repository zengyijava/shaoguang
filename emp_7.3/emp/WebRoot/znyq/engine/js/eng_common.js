
$(document).ready(function() {
	//floatingRemind("databaseTip","输入用于执行的sql语句，sql语句可以带执行条件，如select * from xxx where name = '#P_0#'，其中#P_0#为上行信息中的手机号，#P_1#，#P_2#，..,#P_n#为短信中的用户参数");
	floatingRemind("databaseTip",getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_sryyzxdsqlyj"));
	
	getLoginInfo("#hiddenValueDiv");
	
	$('#addFrame').dialog({
			autoOpen: false,
			height: 450,
			width:400,
			modal:true
	});
	
});

function toAddDB(){
	$("#addDataSource").attr("src","eng_mtProcess.htm?method=toAddDB");
	$('#addFrame').dialog('open');
}

function closeAddFrame()
{
	$("#addFrame").dialog("close");
	$("#addDataSource").attr("src","");
}

//获取新数据源
function getDBInfo() {
	
	var lgcorpcode=$("#lgcorpcode").val();
	$.post("eng_mtProcess.htm?method=getdb&lgcorpcode="+lgcorpcode,function(msg)
			{
				if(msg== "" || msg=="errdb" || msg=="nodata"){
					//alert("获取数据库连接信息失败。");
					alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_hqsjkljxxsb"));
					return;
				}
				msg = eval("("+msg+")");
				//var selStr = '<option value="" style="color:#666666">请选择</option>';
				var selStr = '<option value="" style="color:#666666">'+getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_qxz")+'</option>';
				$.each(msg, function(idx,item){
					selStr += '<option value="'+item.dbid+'" >'+item.dbname+'</option>';
				});
				$("#dbId").find("option").remove();
				$("#dbId").append(selStr);
			}
	);
}


function showAddSmsTmp(showType)
{
	$("#addSmsTmpDiv").css("display","block");
	$("#addSmsTmpFrame").css("display","block");

	$("#addSmsTmpDiv").dialog({
		autoOpen: false,
		height:500,
		width: 620,
		resizable:false,
		modal: true,
		open:function(){
			
		},
		close:function(){
			$("#addSmsTmpDiv").css("display","none");
			$("#addSmsTmpFrame").css("display","none");
		}
	});
	//新增
    $("#addSmsTmpFrame").attr("src","eng_mtProcess.htm?method=toAddSmsTmp&lguserid="
    	    +$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val()+"&showType="+showType+"&time="+new Date().getTime());
          
	$("#addSmsTmpDiv").dialog("open");
}

function closeAddSmsTmpdiv()
{
	$("#addSmsTmpDiv").dialog("close");
	$("#addSmsTmpFrame").attr("src","");
	
}


