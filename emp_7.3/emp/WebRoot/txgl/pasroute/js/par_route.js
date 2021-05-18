function checkAlls(e,str)    
{  
	var a = document.getElementsByName(str);    
	var n = a.length;    
	for (var i=0; i<n; i=i+1)    
		a[i].checked =e.checked;    
}
	
function toAdd()
{
	location.href="par_route.htm?method=toAdd&"+backfind("#corpCode");
}
//修改SP账号状态
function changestate(i,keyidx)
{
	var ks=$.trim($("#gtState"+i).attr("value"));
	//提示有个问题没解决，先不提示了直接改状态
	//if(confirm("您确定要修改该路由的状态吗?")==true){
	if(true){
		var lgcorpcode =$("#lgcorpcode").val();
		var lgusername = $("#lgusername").val();
		$.post("par_route.htm?method=ChangeSate",{id:i,status:ks,lgcorpcode:lgcorpcode,lgusername:lgusername,keyId:keyidx},function(result){
			
	        if (result == "true") {
				//alert("更改状态成功！");
	        	alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_1"));
				$("#gtState"+i).empty();
				if(ks == 0)
				{
				    $("#gtState"+i).append("<option value='0' selected='selected'>"+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_2")+"</option>");
				    $("#gtState"+i).append("<option value='1' >"+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_3")+"</option>");
				    
				}
				else
				{
					$("#gtState"+i).append("<option value='1' selected='selected'>"+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_4")+"</option>");
					$("#gtState"+i).append("<option value='0' >"+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_5")+"</option>");
				}
				//changeEmpSelect($("#gtState"+i),80,function(){
				  //changestate(i);
				//});
				$("#gtState"+i).next(".c_selectBox").remove();
				$("#gtState"+i).isSearchSelectNew({'width':60,'isInput':false,'zindex':0,'isHideBorder':true,'isHide':true},function(){
					changestate(i,keyidx);
				},function(data){
					 var self=$(data.box.self);
	  				self.siblings('.setControl').html(self.find("option:selected").text()).css({'width':'60px','display':'block'});
				});
				//black();
			}else{
				//alert("修改失败！");
				alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_6"));
			}		
		});
	}else{
		
		//$("#gtState"+i).empty();
		if(ks != 0)
		{
		    $("#gtState"+i).append("<option value='0' selected='selected'>"+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_2")+"</option>");
		    $("#gtState"+i).append("<option value='1' >"+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_3")+"</option>");
		}
		else
		{
			$("#gtState"+i).append("<option value='1' selected='selected'>"+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_4")+"</option>");
			$("#gtState"+i).append("<option value='0' >"+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_5")+"</option>");
		}
		//changeEmpSelect($("#gtState"+i),80,function(){
				 // changestate(i);
				//});
		$("#gtState"+i).next(".c_selectBox").remove();
		$("#gtState"+i).isSearchSelect({'width':60,'isInput':false,'zindex':0,'isHideBorder':true,'isHide':true},function(){
					changestate(i,keyidx);
				},function(data){
					 var self=$(data.box.self);
	  				self.siblings('.setControl').html(self.find("option:selected").text()).css({'width':'60px'});
				});
		
	}
}

$(document).ready(function() {
			$("#tdhm_key").next(".c_selectBox").find("ul.c_result li").each(function () {
				$(this).click(function () {
					var value = $(this).html().substring(0,$(this).html().indexOf("("));
					var $option = $("#tdhm_key").find("option[value='"+ value +"']");
					var spgatetype = $option.attr("gatetype");
					var spgate = $option.val();
					$("#spgatetype").val(spgatetype);
					$("#spgate").val(spgate);
                });
            });
			$("#spCard").next(".c_selectBox").find("ul.c_result li").each(function () {
				$(this).click(function () {
					var value = $(this).html().substring(0,$(this).html().indexOf("("));
					var $option = $("#spCard").find("option[value='"+ value +"']");
					var gatetype = $option.attr("gatetype");
					var userId = $option.val();
					$("#accouttype").val(gatetype);
					$("#userId").val(userId);
				});
			});
		  getLoginInfo("#corpCode");
			
		    if(findresult=="-1")
		    {
		       //alert("加载页面失败,请检查网络是否正常!");	
		    	alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_7"));
		       return;			       
		    }
			$("#toggleDiv").toggle(function() {
					$("#condition").hide();
					$(this).addClass("collapse");
				}, function() {
					$("#condition").show();
					$(this).removeClass("collapse");
				});
			$("#content tbody tr").hover(function() {
					$(this).addClass("hoverColor");
				}, function() {
					$(this).removeClass("hoverColor");
				});
			initPage(total,pageIndex,pageSize,totalRec);
			$('#search').click(function(){
				$('#spgate').val($('#tdhm_key option:selected').val());
				$("#spgatetype").val($('#tdhm_key option:selected').attr('gatetype'));
				$("#accouttype").val($("#spCard option:selected").attr('gatetype'));
				$('#userId').val($('#spCard option:selected').val());
				submitForm();
			});
			$("#delete").click(function(){
				var ids;
				var i=0;	
				$('input[name="delRouteId"]:checked').each(function(index){	
					if(index>0){
						ids=ids+",";
						ids=ids+$(this).val();
					}else
					{
						ids=$(this).val();
					}
					i=i+1;
				});
				if(i==0){
					//alert("请选择您要删除的路由信息！");
					alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_8"));
				}else{
					if(confirm(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_9")+i+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_10"))==true){
						$.post(par_route_path,{ids:ids,method:"delete"},function(result){
							if(result>0)
							{
								alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_11")+result+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_12"));
								document.forms["pageForm"].submit();
							}else{
								alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_13"));
							}
						});
								
					}
				}
			});
			init_set(inheritPath);
			$('#tdhm_key').isSearchSelect({'width':'158','zindex':0},function(data){
				//keyup click触发事件
					$("#spgate").val(data.value);
					$("#spgatetype").val(data.selected.attr('gatetype'));
			},function(data){
				//初始化加载
				var val=$("#spgate").val();
				if(val){
					data.box.input.val(val);
				}
				
			});
			$('#spCard').isSearchSelect({'width':'158','zindex':0},function(data){
				//keyup click触发事件
					$("#userId").val(data.value);
					$("#accouttype").val(data.selected.attr('gatetype'));
			},function(data){
				//初始化加载
				var val=$("#userId").val();
				if(val){
					data.box.input.val(val);
				}
			});
			$('#status,#spisuncm,#routeFlag').isSearchSelect({'width':'158','isInput':false,'zindex':0});
			
			$("#content select").isSearchSelectNew({'width':60,'isInput':false,'zindex':0,'isHideBorder':true,'isHide':true},function(data){
					var idx=$(data.box.self).attr("idx");
					var keyidx=$(data.box.self).attr("keyidx");
					changestate(idx, keyidx);
				},function(data){
					 var self=$(data.box.self);
	  				self.siblings('.setControl').html(self.find("option:selected").text()).css({'width':'60px'});
				});
		});
		function del(i,gatetype,keyId){
			//if(confirm("您确定要删除该路由信息?")==true){
			if(confirm(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_14"))==true){
				$.post(par_route_path,{ids:keyId,gatetype:gatetype,method:"delete"},function(result){
					if(result>0)
					{
						//alert("删除成功,共删除"+result+"条信息！");
						alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_11")+result+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_12"));
						document.forms["pageForm"].submit();
					}else{
						//alert("删除失败！");
						alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_13"));
					}
				});
			}
		}