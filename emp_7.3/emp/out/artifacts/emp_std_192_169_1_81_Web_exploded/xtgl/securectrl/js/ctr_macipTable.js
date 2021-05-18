$(document).ready(function(){
			$("#binddiv").dialog({
				autoOpen: false,
				height: 350,
				width: 250,
				modal: true,
				close:function(){
				},
				open:function(){
				}
			});
			$("#ipsDiv").dialog({
				autoOpen: false,
				height:200,
				width: 250,
				modal: true,
				close:function(){
					showSelect();
				},
				open:function(){
					hideSelect();
				}
			});	
			$("#macsDiv").dialog({
				autoOpen: false,
				height:200,
				width: 250,
				modal: true,
				close:function(){
					showSelect();
				},
				open:function(){
					hideSelect();
				}
			});		
			
		});
		
		$("#content tbody tr").hover(function() {
				$(this).addClass("hoverColor");
			}, function() {
				$(this).removeClass("hoverColor");
			});
			
		
		function bineSure(){
		var lmiid = $("#lmiid").val();
		var ipStr = "";
		var macStr = "";
		$("input[name='checkIp']:checked").each(function(){
			ipStr = ipStr + $(this).val()+",";
		});
		$("input[name='checkMac']:checked").each(function(){
			macStr = macStr + $(this).val()+",";
		});
		$.post("ctr_securectrl.htm?method=cancelBind",{method:"cancelBind",lmiid:lmiid,ips:ipStr,macs:macStr},
			function(result){
				if(result=="true"){
					alert(getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_7"));
					location.href = location.href;
				}else if(result == "error"){
					alert(getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_8"));
				}
			}
		);
	}
	function allIps(t){
    		$("#ipMsg").empty();
    		var str = $(t).children("label").text().split(",");
    		for(var i=0;i<str.length;i++){
    			$("#ipMsg").append(str[i]+"</br>");
    		}
		$("#ipsDiv").dialog("option","title", getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_9"));
    		$("#ipsDiv").dialog("open");
	}
	
	function allMacs(t){
    		$("#macMsg").empty();
    		var str = $(t).children("label").text().split(",");
    		for(var i=0;i<str.length;i++){
    			$("#macMsg").append(str[i]+"</br>");
    		}
		$("#macsDiv").dialog("option","title", getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_10"));
    		$("#macsDiv").dialog("open");
	}
			
	function enablePwd(guid){
		if(confirm(getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_11"))){
			$.post("ctr_securectrl.htm",{method:"enablePwd",guid:guid},
			function(result){
				if(result=="true"){
					alert(getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_12"));
					location.href = location.href;
				}else if(result == "error" || result == "false"){
					alert(getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_13"));
				}
			}
		);
		}
	}
	function cancelPwd(lmiid){
		if(confirm(getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_14"))){
			$.post("ctr_securectrl.htm",{method:"cancelPwd",lmiid:lmiid},
			function(result){
				if(result=="true"){
					alert(getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_15"));
					location.href = location.href;
				}else if(result == "error" || result == "false"){
					alert(getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_16"));
				}
			}
		);
		}
	}		
	function checkAlls(e,str){    
		var a = document.getElementsByName(str);    
		var n = a.length;    
		for (var i=0 ; i < n ; i++ ){   
			a[i].checked =e.checked;
		}
	}