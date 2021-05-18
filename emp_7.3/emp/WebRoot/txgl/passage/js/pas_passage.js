//点击更改通道状态
function changeStatu(id,keyid){
	var ks=$.trim($("#gateState"+id).attr("value"));
	//提示有个问题没解决，先不提示了直接改状态
	//if(confirm('您真的确定要更改该通道状态吗？')){
	if(true){
	 	var lgusername = $("#lgusername").val();
	 	var lgcorpcode= $("#lgcorpcode").val();
		$.post("pas_passage.htm",{id:id,method:"changeState",lgusername:lgusername,lgcorpcode:lgcorpcode,keyId:keyid},function(result){
			if(result=="error")
			{
				//alert("更改状态失败！");
				alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_18"));
			}else
			{
				//alert("更改状态成功！");
				alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_19"));
				$("#gateState"+id).empty();
				if(ks == 0)
				{
					/*已激活 失效*/
				    $("#gateState"+id).append("<option value='0' selected='selected'>"+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_48")+"</option>");
				    $("#gateState"+id).append("<option value='1' >"+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_49")+"</option>");
				}
				else
				{
					/*已失效  激活*/
					$("#gateState"+id).append("<option value='1' selected='selected'>"+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_51")+"</option>");
					$("#gateState"+id).append("<option value='0' >"+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_50")+"</option>");
				}
				//changeEmpSelect($("#gateState"+id),80,function(){
					//changeStatu(id);
				//});
                $("#gateState"+id).next(".c_selectBox").remove();
				$("#gateState"+id).isSearchSelectNew({'width':60,'isInput':false,'zindex':0,'isHideBorder':true,'isHide':true},function(){
					changeStatu(id,keyid);
				},function(data){
					 var self=$(data.box.self);
	  				self.siblings('.setControl').html(self.find("option:selected").text()).css({'width':'60px','display':'block'});
				});
			}
		});
	}else{
		//$("#gateState"+id).empty();
		if(ks != 0)
		{
		    $("#gateState"+id).append("<option value='0' selected='selected'>"+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_48")+"</option>");
		    $("#gateState"+id).append("<option value='1' >"+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_49")+"</option>");
		}
		else
		{
			$("#gateState"+id).append("<option value='1' selected='selected'>"+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_51")+"</option>");
			$("#gateState"+id).append("<option value='0' >"+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_50")+"</option>");
		}
		//changeEmpSelect($("#gateState"+id),80,function(){
					//changeStatu(id);
				//});
		$("#gateState"+id).next(".c_selectBox").remove();
		$("#gateState"+id).isSearchSelectNew({'width':60,'isInput':false,'zindex':0,'isHideBorder':true,'isHide':true},function(){
			changeStatu(id,keyid);
		},function(data){
			 var self=$(data.box.self);
				self.siblings('.setControl').html(self.find("option:selected").text()).css({'width':'60px'});
		});
	}
}
$(document).ready(function() {
		getLoginInfo("#corpCode");
			
		    if(findresult=="-1")
		    {
		       //alert("加载页面失败,请检查网络是否正常!");	
		    	alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_1"));
		       return;			       
		    }
		    $("#toggleDiv").toggle(function() {
				$("#condition").hide();
				$(this).addClass("collapse");
			}, function() {
				$("#condition").show();
				$(this).removeClass("collapse");
			});
		    $('#search').click(function(){submitForm();});
			$("#content tbody tr").hover(function() {
					$(this).addClass("hoverColor");
				}, function() {
					$(this).removeClass("hoverColor");
				});
			
			initPage(total,pageIndex,pageSize,totalRec);
			$('#tdhm_key').isSearchSelect({'width':'150','zindex':0},function(data){
				//keyup click触发事件
					$("#spgate").val(data.value);
			},function(data){
				//初始化加载
				var val=$("#spgate").val();
				if(val){
					data.box.input.val(val);
				}
			});
			$('#spisuncm').isSearchSelect({'width':'150','isInput':false,'zindex':0});
			$('#routeFlag').isSearchSelect({'width':'150','isInput':false,'zindex':0});
			$("#content select").isSearchSelectNew({'width':60,'isInput':false,'zindex':0,'isHideBorder':true,'isHide':true},function(data){
				var idx=$(data.box.self).attr("idx");
				var keyid=$(data.box.self).attr("keyidx");
				changeStatu(idx,keyid);
			},function(data){
				 var self=$(data.box.self);
  				self.siblings('.setControl').html(self.find("option:selected").text()).css({'width':'60px','display':'block'});
			});

			
		});
		