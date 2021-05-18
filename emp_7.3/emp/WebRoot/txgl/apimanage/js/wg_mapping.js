$(document).ready(function() {
			if(sflage=="true"){
			/*请您检查格式中相应内容(可能存在重复字段等情况)！*/
			alert(getJsLocaleMessage("txgl","txgl_js_mapping_1"));
		}else if(sflage=="saveSuc"){
			/*alert("保存成功！");*/
			alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_10"));
		}
});


function toList(type){
	$("#cmdtype").val(type);
	if(type=='1'){
		/*请求格式示例*/
		$("#show").text(getJsLocaleMessage("txgl","txgl_js_mapping_2"));
	}else{
		/*响应格式示例*/
		$("#show").text(getJsLocaleMessage("txgl","txgl_js_mapping_3"));
	}
	var ecid=$('#ecid').val();
	var funtype=$('#funtype').val();
	var pathUrl = $("#pathUrl").val();
	var funname=$("#funname").val();
	var req=$("#req").val();
	var resp=$("#resp").val();
	window.location.href=pathUrl+"/wg_apiBaseMage.htm?method=mapping&ecid="+ecid+"&funname="+funname+"&req="+req+"&resp="+resp+"&funtype="+funtype+"&cmdtype="+type;
}
//保存映射关系
function save(){
	var status=$("#status").val();
	if(status=="1"){
		/*启用状态，不可设置映射关系！*/
		alert(getJsLocaleMessage("txgl","txgl_js_mapping_4"));
			return;
	}
	var parse=$("#newStyle").val();
	if($.trim(parse)==''){
		/*解析后的内容为空，请先解析格式示例！*/
		alert(getJsLocaleMessage("txgl","txgl_js_mapping_5"));
		return;
	}
	if(parse.length>3000){
		/*解析内容过多，请精简解析内容！*/
		alert(getJsLocaleMessage("txgl","txgl_js_mapping_6"));
		return;
	}
	var number=$("#number").val();
	for(var k=0;k<number;k++){
		var putValue=$("#fixed"+k).val();
		var mwfield=$("#mwfield"+k).val();
		if(putValue==''&&mwfield=='0'){
			/*固定值不能为空,请重新输入！*/
			alert(getJsLocaleMessage("txgl","txgl_js_mapping_7"));
			return;
		}
	}
	document.forms['pageForm'].submit();
	
	
}
//选择字段属性下拉框
function change(obj){
	var slt=obj.value;
	var na=obj.name;
	var guding=na.replace(/mwfield/g, "fixed");
	var thisname=$("input[name='"+guding+"']");
	if(slt=="0"){
		thisname.val("");
		thisname.attr("disabled","");
	}else{
		thisname.val("");
		thisname.attr("disabled","disabled"); 
	}
	
}
//选择字段属性时候，如果是json格式，需要把子属性显示出来
function propertySelect(obj,ind){
	var select_valule=obj.value;
	var na=obj.name;
	var guding=na.replace(/mwfield/g, "fixed");
	var thisname=$("input[name='"+guding+"']");
	if(select_valule=="0"){
		thisname.val("");
		thisname.attr("disabled",false);
	}else{
		thisname.val("");
		thisname.attr("disabled",true); 
	}


	//如果用户在urlencode类型中，某个字段选择json值需要校验
	var req=$("#req").val();
	var resp=$("#resp").val();
	var oldStyle=$("#oldStyle").val();
	var jsonname=na.replace(/mwattr/g, "cargname"); 
	var jsonselect=$("input[name='"+jsonname+"']").val();
	var flag="false";
	var cmdtype=$("#cmdtype").val();
	if(cmdtype=='1'&&req=='1'){
		flag="true";
	}else if(cmdtype=='1'&&req=='2'){
		flag="true";
	}else if(cmdtype=='2'&&resp=='1'){
		flag="true";
	}else if(cmdtype=='2'&&resp=='2'){
		flag="true";
	}else if(cmdtype=='3'&&resp=='1'){
		flag="true";
	}else if(cmdtype=='3'&&resp=='2'){
		flag="true";
	}else if(cmdtype=='4'&&resp=='1'){
		flag="true";
	}else if(cmdtype=='4'&&resp=='2'){
		flag="true";
	}else if(cmdtype=='5'&&resp=='1'){
		flag="true";
	}else if(cmdtype=='5'&&resp=='2'){
		flag="true";
	}
	if(select_valule=="2"||select_valule=="4"){
		/*该选择将改变原有结构体解析，请认证检查该字段是否要按此类型解析。*/
		if(!confirm(getJsLocaleMessage("txgl","txgl_js_mapping_8"))){
			 $("select[name='"+na+"']").find("option[value='"+ind+"']").attr("selected", true);
			return;
		}
			
	}
		var parnode=na.replace(/mwattr/g, "mainNode");
		 var main="<input name='"+parnode+"' value='1;' type='hidden'>";
		 $("input[name='"+jsonname+"']").after(main);

	//当选择的类型为urlcode处理下面，数据类型为json
	if(select_valule=="2"&&(resp=="4"||req=="4")){
	$(obj).attr("disabled","disabled"); 
		$.post('wg_apiBaseMage.htm?method=pashURLCode', {
				req:req,
				resp:resp,
				oldStyle:oldStyle,
				jsonselect:jsonselect,
				cmdtype:select_valule
				},function(msg)
			{
				// 转换成json对象   
			  var jsonresult = $.parseJSON(msg);
			  var result = jsonresult.style;
			  var nodeList=jsonresult.nodeList;
				if(result!=""){
					if(nodeList!=null&&nodeList!=''){
					var nodes= nodeList.split(',');
					var number=$('#number').val();
					var temp_number=parseInt(number)+parseInt(nodes.length);
					$('#number').val(temp_number);
					var num=0;
					if(flag){
						for(var i=0;i<nodes.length;i++){//解析出来的都是要用的，
							var client_node=nodes[i].split(':');
							var index=parseInt(i)+parseInt(number);//避免重复，加上之前的序号
						$("#content").append("<tr style='background:#dfe9f9' class='listAll'><td>"+client_node[0]+"<input name='cargname"+index+"' value='"+client_node[0]+"' type='hidden' /><input name='mainNode"+index+"' value='0;"+jsonselect+"' type='hidden' /></td>" +
						"<td> <select name='mwfield"+index+"' id='mwfield"+index+"' onchange='change(this)'>" +
						/*固定值 忽略值*/
						"<option value='0'>"+getJsLocaleMessage("txgl","txgl_js_mapping_9")+"</option>" +
						"<option value='-1'>"+getJsLocaleMessage("txgl","txgl_js_mapping_10")+"</option>" +mwcode+
						"</select></td><td>" +
						"<select name='mwattr"+index+"' onchange='propertySelect(this,8)' >" +
						"<option value='8'>string</option>" +
						"<option value='1'>xml</option>" +
						"<option value='2'>json</option>" +
						"<option value='4'>urlencode</option>" +
						"<option value='16'>int</option>" +
						"<option value='32'>time</option>" +
						"<option value='64'>other</option>" +
						"</select>" +
						"</td><td><input type='text' name='fixed"+index+"' value='' id='fixed"+index+"' style='width: 30px;' maxlength='30'></td></tr>");
						num=num+1;
						}
						
					}else{
						for(var i=0;i<nodes.length;i++){
						$("#content").append("<tr style='background:#dfe9f9' class='listAll'><td>"+nodes[i]+"<input name='cargname"+i+"' value='"+nodes[i]+"'  type='hidden' /></td>" +
						"<td> <select name='mwfield"+i+"' id='mwfield"+i+"' onchange='change(this)' >" +
						/*固定值 忽略值*/
						"<option value='0'>"+getJsLocaleMessage("txgl","txgl_js_mapping_9")+"</option>" +
						"<option value='-1'>"+getJsLocaleMessage("txgl","txgl_js_mapping_10")+"</option>" +mwcode+
						"</select></td><td>" +
						"<select name='mwattr"+i+"' onchange='propertySelect(this,8)' >" +
						"<option value='8'>string</option>" +
						"<option value='1'>xml</option>" +
						"<option value='2'>json</option>" +
						"<option value='4'>urlencode</option>" +
						"<option value='16'>int</option>" +
						"<option value='32'>time</option>" +
						"<option value='64'>other</option>" +
						"</select>" +
						"</td><td><input type='text' name='fixed"+i+"' id='fixed"+i+"' value='' style='width: 30px;' maxlength='30'></td></tr>");
					}
					}

				}
				}

			});
	}
}
