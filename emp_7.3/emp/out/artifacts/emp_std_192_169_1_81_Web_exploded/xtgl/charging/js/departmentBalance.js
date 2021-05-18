var aaaaa = 0 ;
function setLeftHeight()
{
	setLeftHeight2();
	window.onresize = function(){  
		if(aaaaa<1)
		{
			aaaaa=aaaaa+1;
		}else
		{
			aaaaa=0;
			return;
		}
		setLeftHeight2();
		setTimeout('aaaaa=0;',300);
	}
}

function setLeftHeight2()
{
	//var isIE = (document.all) ? true : false;
	//var isIE6 = isIE && (navigator.appVersion.match(/MSIE 6.0/i)=="MSIE 6.0" ? true : false);
	var hei=$(window).height();
	var bodyhei = $('.right_info').height();
	var tophei = $('.top:visible').height();
	var topMargin = $('.top:visible').css('margin-top'); 
	if(tophei==null)
	{
		tophei = 0;
	}
	if(topMargin != null)
	{
		tophei = tophei + 5;
	}
	if(bodyhei > hei)
	{
		hei = bodyhei;
		$('.left_dep').css('height',hei-tophei);
		$('.left_dep .list ').css('height',hei-50-tophei);
	}else
	{
		$('.left_dep').css('height',hei-20-tophei);
		$('.left_dep .list ').css('height',hei-70-tophei);
	}
}

function delDeps(id,name)
{
	$("#depId").val(id);
	$("#upDepName").attr("value",name);
	if($('#depId').val()!=""){
		var pathUrl = $("#pathUrl").attr("value");
		var depId= $('#depId').attr("value");
		var depName =$("#upDepName").attr("value");
		if(confirm("确认删除选中机构？")){// 提示框
			$.post("u_department.htm?method=delete",{depId:depId,depName:depName},function(data){
					if(data==0){
						alert("该机构有子机构或者该机构下有客户,员工,不能删除！");return;}
					else if(data==1){
						alert("删除成功！");
						location.reload();// 刷新页面
					}else if(data==-1)
					{
						alert("删除失败！");
					}
				});
		}
	}
	else
	{
        alert("请选择要删除的机构！");
	}
}
//按回车登录
function keydown(e) //支持ie 火狐 键盘按下事件      
{        
	var currKey=0,e=e||event;
       if(e.keyCode==13) return false;      
}
//获取总金额
function getMoney(){
	var path = 'cha_balanceMgr.htm';
	   $.post(path,{method:"getMoney",lgcorpcode:$("#lgcorpcode").val()},function(result){
		   if(result.indexOf("html") > 0){
    			window.location.href=location;
    		    return;
    	   }else{
    		   var rArr =result.split(',');
    		   $('#shortMsg').html(rArr[0]);
    		   $('#MMS').html(rArr[1]);
		    }
	   });
	
}
function toLog(){
    location.href="cha_balanceLog.htm?method=find&lgguid="+$("#lgguid").val();
}

function clearDefault(){
    if($('#depName').val() == ""){
        document.getElementById('depName').value="输入名称";
    }else if($('#depName').val() == "输入名称"){
        document.getElementById('depName').value="";
    }
}