//点击更改通道状态
function changeStatu(gateId,statu){                	 	
	 //if(confirm('您真的确定要更改该通道状态吗？')){
	if(confirm(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_20"))){
		$.post("w_passage.htm",{id:gateId,method:"changeState"},function(result){
			if(result=="error")
			{
				//alert("更改状态失败！");
				alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_18"));
			}else
			{
				//alert("更改状态成功！");
				alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_19"));
				//if(statu=="激活")
				if(statu==getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_50"))	
				{
					$("#st"+gateId).html("<a href=\"javascript:changeStatu("+gateId
						+","+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_49")+")\">"+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_50")+"</a>");
					//$('#lb'+gateId).text("失效");
					$('#lb'+gateId).text(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_49"));
				}else
				{
					$("#st"+gateId).html("<a href=\"javascript:changeStatu("+gateId
						+","+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_50")+")\">"+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_49")+"</a>");
					
					//$('#lb'+gateId).text("激活");
					$('#lb'+gateId).text(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_50"));
				}
			}
		});
	}
}

//新增页面，计算通道长度
function NumCount(){
	var num=$("#spageteNum").attr("value");
	$("#numlength").html("");
	$("#numlength").html(num.length);
	
}
var en_reg = /[\[\]\|\^\{\}\~\\]/g;
var maxlen = [20,20];
var minlen = [3, 5];
//新增通道，计算短信签名长度
function getSignlen(){
	
	if($("input[name='signMode']:checked").val()==0)
	{
		var spiscumn = $('#spiscumn').val();
		if(5 == spiscumn){
			$('#cnsignlenCount').text($.trim($('#cnSign').val()).length);
            var isSupportEn = $("input[name='isSupportEn']:checked").val();
            if(isSupportEn){
                var enSign = $.trim($('#enSign').val());
                var len = enSign.replace(en_reg, '**').length;
                var isMax = false;
                while(len> maxlen[1]){
                    isMax = true;
                    enSign = enSign.substr(0, enSign.length - 1);
                    len = enSign.replace(en_reg, '**').length;
                }
                if(isMax){
                    $('#enSign').val(enSign);
                }
                $('#ensignlenCount').text(len);
            }
		}else{
			var num=$.trim($("#signstr").attr("value"));
			$("#signlen").html(num.length);
		}
	}
}
	
	
//验证单条短信最大字数
function checkSinglelen(){
	var gatetype = $('#gatetype').val();//通道类型
	if(gatetype !=1){//不为短信
		return true;
	}
	var isGW = $('#spiscumn').val()==5;//是否为国外运营商
	var $obj = $("#singlelen");
	if(isGW){
		var isSupportEn = $("input[name='isSupportEn']:checked").val();
		if(isSupportEn-1==0){
			$obj = $('#cnsinglelen,#ensinglelen');
		}else{
			$obj = $('#cnsinglelen');
		}
	}
	var patrn=/^[0-9]*$/;
	var flag = false;
	var max = [70,160];
	//var lang = ['中文','英文'];
	var lang = [getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_21"),getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_22")];
	$obj.each(function(index){
		var len = $(this).val();
		var la = isGW?lang[index]:"";
		if(len==""){
			//$("#zhu").html("请输入"+la+"单条短信最大字数！");
			$("#zhu").html(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_23")+la+getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_24")
);
			return false;
		}
		if(!patrn.test(len)){
			//$("#zhu").html(la+"单条短信最大字数请输入正整数！");
			$("#zhu").html(la+getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_25"));
			return false;
		}
		if(len-max[index]>0){
			$("#zhu").html(la+getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_26")+max[index]+getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_27"));
			return false;
		}
		if(index == $obj.length-1){
			flag = true;
		}
	});
	if(flag){
		//$("#zhu").html("注：以上通道信息请按通道运营商提供的准确数据录入");
		$("#zhu").html(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_28"));
		return true;
	}else{
		return false;
	}
}
	
//验证短信签名
function checkSignstr(){
	getSignlen();
	var isGW = $('#spiscumn').val()==5;//是否为国外运营商
	var $obj = $("#signstr");
	//签名不能包含分号;和逗号,
    var regFenHao = RegExp(/;/);
    var regDouHao = RegExp(/,/);
    if(regFenHao.test($obj.val())||regDouHao.test($obj.val())){
        $("#zhu").html("签名不能包含分号或者逗号！");
		return false;
	}

	//签名不能包含&quot、&amp、&lt、&gt、&nbsp这5个字符串
    var regKongGe = RegExp(/&nbsp/);
    if(regKongGe.test($obj.val())){
        $("#zhu").html("签名不能包含&amp;nbsp字符串！");
        return false;
    }
    var regShuangYinHao = RegExp(/&quot/);
    if(regShuangYinHao.test($obj.val())){
        $("#zhu").html("签名不能包含&amp;quot字符串！");
        return false;
    }
    var regDaYuHao = RegExp(/&gt/);
    if(regDaYuHao.test($obj.val())){
        $("#zhu").html("签名不能包含&amp;gt字符串！");
        return false;
    }
    var regXiaoYuHao = RegExp(/&lt/);
    if(regXiaoYuHao.test($obj.val())){
        $("#zhu").html("签名不能包含&amp;lt字符串！");
        return false;
    }
    var regYuFuHao = RegExp(/&amp/);
    if(regYuFuHao.test($obj.val())){
        $("#zhu").html("签名不能包含&amp;amp字符串！");
         return false;
     }
	var gatetype = $('#gatetype').val();
	if(isGW&&gatetype-1==0){
		var isSupportEn = $("input[name='isSupportEn']:checked").val();
		if(isSupportEn-1==0){
			$obj = $('#cnSign,#enSign');
		}else{
			$obj = $('#cnSign');
		}
	}
	var pat=[/^\[.*\]$/,/^\[[\x00-\xff]*\]$/];
	var flag = false;
	//var lang = ['中文','英文'];
	var lang = [getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_21"),getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_22")];
	$obj.each(function(index){
		var signstr = $.trim($(this).val());
		$(this).val(signstr);
		if(signstr!="" )
		{
            var len =signstr.length;
            if(index==1){len =signstr.replace(en_reg,'**').length; }
			if( (len> maxlen[index] || len<minlen[index]))
			{
				
				if(isGW&&gatetype-1==0){
					//$("#zhu").html(lang[index]+"签名长度应为"+minlen[index]+"-"+maxlen[index]+"位！");
					$("#zhu").html(lang[index]+getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_29")+minlen[index]+"-"+maxlen[index]+getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_30"));
				}else if(gatetype==2)
				{
					//$("#zhu").html("彩信签名长度应为3-20位！");
					$("#zhu").html(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_31"));
				}
				else
				{
					//$("#zhu").html("短信签名长度应为3-20位！");
					$("#zhu").html(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_32"));
				}
				return false;
			}
			if(!checkSignLen($(this))){
				return false;
			}
			if(!pat[index].test(signstr)){
				//签名标识，0：不合法；1：合法
				var signflag = 0;
				//中文签名
				if(index == 0)
				{
					//中文签名支持中文【】
					if(signstr.substring(0,1).charAt(0).charCodeAt() == 12304 
					&&  signstr.substring(signstr.length-1).charAt(0).charCodeAt() == 12305)
					{
						//设置签名为合法
						signflag = 1;
					}
				}
				//签名不合法
				if(signflag == 0)
				{
					if(isGW&&gatetype-1==0){
						//$("#zhu").html(lang[index]+"签名输入格式不对！");
						$("#zhu").html(lang[index]+getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_33"));
					}else if(gatetype==2)
					{
						//$("#zhu").html("彩信签名输入格式不对！");
						$("#zhu").html(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_34"));
					}
					else
					{
						//$("#zhu").html("短信签名输入格式不对！");
						$("#zhu").html(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_35"));
					}
					return false;
				}
			}
		}
		if(index == $obj.length-1){
			flag = true;
		}
	});
	if(flag){
		//$("#zhu").html("注：以上通道信息请按通道运营商提供的准确数据录入");
		$("#zhu").html(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_36"));
		return true;
	}else{
		return false;
	}
}
	
//验证费率
function checkFee(){
	var fee=$("#fee").attr("value");
	var patrn5=/^\d+(\.\d{0,2})?$/;
//	if(fee==""){
//		$("#zhu").html("费率不能为空！");
//		return false;
//	}
	if(fee!=""){
		if(!patrn5.test(fee)){
			//$("#zhu").html("费率应在0.01~99.99之间,并且不能输入特殊字符！");
			$("#zhu").html(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_37"));
			return false;
		}
		if(fee==0){
			//$("#zhu").html("费率应在0.01~99.99之间！");
			$("#zhu").html(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_38"));
			return false;
		}
		if(fee>=100 || fee.length>5){
			//$("#zhu").html("费率应在0.01~99.99之间,并且不能输入特殊字符！");
			$("#zhu").html(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_39"));
			return false;
		}
	}
	//$("#zhu").html("注：以上通道信息请按通道运营商提供的准确数据录入");
	$("#zhu").html(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_40"));
	return true;
}

//验证后拆分短信最大字数
function checkEspLitMaxWd(){
	var gatetype = $('#gatetype').val();//通道类型
	if(gatetype !=1){//不为短信
		return true;
	}
	var isGW = $('#spiscumn').val()==5;//是否为国外运营商
	var $obj = $("#esplitmaxwd");
	if(isGW){
		var isSupportEn = $("input[name='isSupportEn']:checked").val();
		if(isSupportEn-1==0){
			$obj = $('#esplitmaxwd,#esplitenmaxwd');
		}else{
			$obj = $('#esplitmaxwd');
		}
	}
	var patrn=/^[0-9]*$/;
	var flag = false;
	//var lang = ['中文','英文'];
	var lang = [getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_21"),getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_22")];
	$obj.each(function(index){
		var litMaxWd = $(this).val();
		var la = isGW?lang[index]:"";
		if(!patrn.test(litMaxWd)){
			//$("#zhu").html("后拆分"+la+"最大字数请输入正整数！");
			$("#zhu").html(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_41")+la+getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_42"));
			return false;
		}
		//中文
		if(index == 0)
		{
			if(litMaxWd - 67 < 0 || litMaxWd - 1000 > 0){
				//$("#zhu").html("后拆分"+la+"最大字数取值范围为[67-1000]！");
				$("#zhu").html(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_41")+la+getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_43"));
				return false;
			}
		}
		else
		{
			if(litMaxWd - 143 < 0 || litMaxWd - 2000 > 0){
				$("#zhu").html(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_41")+la+getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_44"));
				return false;
			}
		}
		
		if(index == $obj.length-1){
			flag = true;
		}
	});
	if(flag){
		//$("#zhu").html("注：以上通道信息请按通道运营商提供的准确数据录入");
		$("#zhu").html(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_45"));
		return true;
	}else{
		return false;
	}
}

//验证通道扩展位数
function checkSublen(){
	var sublen=$("#sublen").attr("value");
	var spageteNum=$("#spageteNum").attr("value");
	var patrn4=/^[0-9]*$/;
	if(sublen==""){
		
		//$("#zhu").html("通道扩展位数不能为空！");
		$("#zhu").html(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_46"));
		return false;
	}
	if(!patrn4.test(sublen)){
		
		//$("#zhu").html("通道扩展位数请输入正整数！");
		$("#zhu").html(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_47"));
		return false;
	}
	if(parseInt(sublen)+spageteNum.length>21){
		
		//$("#zhu").html("通道号码位数与通道扩展位数之和不能超过21位！");
		$("#zhu").html(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_48"));
		return false;
	}
	if(parseInt(sublen)+spageteNum.length==21)
	{
		//alert("当前通道号码长度+扩展位数为21位，建议不超过21位！");
		alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_49"));
	}
	if(parseInt(sublen)==0)
	{
		//$("#zhu").html("通道扩展位数必须大于0！");
		$("#zhu").html(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_50"));
		$("#sublen").val("0");
	}
	
	//$("#zhu").html("注：以上通道信息请按通道运营商提供的准确数据录入");
	$("#zhu").html(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_51"));

	return true;
}
//短信签名长度模式改变时
function changeSignMode(modeValue){
	var spiscumn = $('#spiscumn').val();
	if(modeValue==0)
	{	
		if(5== spiscumn){
			$('.sign').css('width','260px');
			$('.signlen').hide();
			$('.signlenCount').show();
		}else{
			$('#trnine').hide();
			$('#signlenname').show();
		}
		getSignlen();
		if($("#trseven input:radio").length<2){
			$("#trseven").html("<td><span>"+getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_52")+"</span></td> <td>&nbsp;&nbsp;&nbsp;&nbsp;<input type='radio'  name='signdroptype' value='1' checked='checked'/>"+getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_53")+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
					"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
					"<input type='radio'  name='signdroptype' value='0' />"+getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_54"));
		}
	}else
	{
		$("#trseven").html("<td><span>"+getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_52")+"</span></td> <td>&nbsp;&nbsp;&nbsp;&nbsp;<input type='radio'  name='signdroptype' value='1' checked='checked'/>"+getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_53"));
		if(5== spiscumn){
			$('.sign').css('width','150px');
			$('.signlen').each(function(){
				var size = $(this).closest('td').next('td').find('.signlenCount').text();
				$(this).children('option[value="'+size+'"]').attr('selected',true);
			})
			$('.signlen').show();
			$('.signlenCount').hide();
		}else{
			$('#signlenname').hide();
			$('#trnine').show();
		}
		//$("#zhu").html("注：以上通道信息请按通道运营商提供的准确数据录入");
		$("#zhu").html(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_1"));
	}
}
	
//验证短信签名长度是否为空
function checkSignLen($obj)
{
	if($('#gatetype').val()-1!=0){return true;}
	var signlen=$obj.next('.signlen').val()||$('#signlen2').val();
	var signstr=$obj.val();
	if($("input[name='signMode']:checked").val()==1)
	{
        var len = signstr.length;
        if ($obj.attr('id')=='enSign') {
            len = signstr.replace(en_reg, '**').length;
        }
		if(len>signlen){
			//$("#zhu").html("固定签名长度时，短信签名的字符长度过长！");
			$("#zhu").html(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_56"));
			return false;
		}
	}
	return true;
}
	
//表单提交前验证 	
function checkAddPassage(){
	NumCount();
	var num=$("#spageteNum").attr("value");
	var gatetype=$("#gatetype").attr("value");
	var spiscumn=$("#spiscumn").attr("value");
	var patrn=/^\d*$/;
	var gatename=$.trim($("#gatename").attr("value"));
	var parth=/^(?:[\u4e00-\u9fa5]*\w*\s*)+$/;    
	if(gatetype==""){
		//$("#zhu").html("请选择通道类型！");
		$("#zhu").html(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_57"));
		return;
	}
	
	if(spiscumn==""){
		//$("#zhu").html("请选择运营商！");
		$("#zhu").html(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_58"));
		return;	
	}
	
	if(gatename==""){
		//$("#zhu").html("请输入通道名称!");
		$("#zhu").html(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_59"));
		return;
	}
	
	if(gatename.length>26){
		//$("#zhu").html("通道名称过长!");
		$("#zhu").html(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_60"));
		return ;
	}
	
	if(!parth.test(gatename)){
		//$("#zhu").html("通道名称 输入不合法!");
		$("#zhu").html(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_61"));
		return ;
	}
	
	if(num==""){
		//$("#zhu").html("请输入通道号码！");
		$("#zhu").html(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_62"));
		return ;
	}
	
	if(!patrn.test(num)){
		//$("#zhu").html("通道号码 只能是数字！");
		$("#zhu").html(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_63"));
		return;
	}
	
	if(num.length>21){
		//$("#zhu").html("通道号不能大于21位！");
		$("#zhu").html(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_64"));
		return;
	}
	
	if(gatetype=="2"){
		if(!checkSignstr() ||!checkSublen() || !checkFee() )
		{
			return;
		}
	}else{
		if(!checkSinglelen() 
				 || !checkSignstr() || !checkSublen() || !checkFee() || !checkEspLitMaxWd())
		{
			return;
		}
	}
	//$("#zhu").html("验证通道是否重复中...");
	$("#zhu").html(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_65"));
		
	$.post($("#path").val()+"/pas_passage.htm",
			{method:"checkGateNum",spgate:num,spiscumn:$('#spiscumn').val(),gatename:gatename,gatetype:$('#gatetype').val(),updateorinsert:$.trim($("#updateorinsert").val()),id:$.trim($("#id").val())},
			function(result){
				if(result=="numExist")
				{
					//$("#zhu").html("该运营商已存在该通道，请重新输入！");
					$("#zhu").html(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_66"));

				}else if(result=="nameExist")
				{
					//$("#zhu").html("该通道名称已存在，请重新输入！");
					$("#zhu").html(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_67"));

				}else if(result=="error")
				{
					//$("#zhu").html("验证运营商通道失败，请确认网络是否畅通！");
					$("#zhu").html(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_68"));

				}else
				{
					$("#zhu").html("");
					if(result=="false")
					{
						var isGW = $('#spiscumn').val()==5;//是否为国外运营商
						var isBlankSign = false;//是否存在空的签名
						var gatetype = $('#gatetype').val();
						if(isGW&&gatetype-1==0){
							var isSupportEn = $("input[name='isSupportEn']:checked").val();
							if(isSupportEn-1==0){
								isBlankSign = !$.trim($("#cnSign").val())||!$.trim($("#enSign").val());
							}else{
								isBlankSign = !$.trim($("#cnSign").val());
							}
						}else{
							isBlankSign = !$.trim($("#signstr").val());
						}
						if(isBlankSign)
						{
							//if(confirm("确定签名为空吗？"))
							if(confirm(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_69")))	
							{
									//$("#zhu").html("注：以上通道信息请按通道运营商提供的准确数据录入");
								$("#zhu").html(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_1"));
									//submitForm();
									yspgate(gatetype,spiscumn,gatename,num);
							}else{
								//$("#zhu").html("注：以上通道信息请按通道运营商提供的准确数据录入");
								$("#zhu").html(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_1"));
							}
						}
						else
						{
								//$("#zhu").html("注：以上通道信息请按通道运营商提供的准确数据录入");
							$("#zhu").html(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_1"));
								//submitForm();
								yspgate(gatetype,spiscumn,gatename,num);
						}
					}
				}
			}
		);
	
}

/*
 * 预览
 */
function yspgate(gatetype,spiscumn,gatename,num){
	var isGW = $('#spiscumn').val()==5;
	var isSupportEn = $("input[name='isSupportEn']:checked").val()-1==0;
	if(gatetype==1){
		if(isGW&&isSupportEn){
			if($('.rpLang:first').text().indexOf('/')==-1){
				$('.rpLang').each(function(){
					//$(this).text($(this).text().replace('：','（中文/英文）：'));
					$(this).text($(this).text().replace('：',getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_73")));
				})
			}
		}else{
			if($('.rpLang:first').text().indexOf('/')!=-1){
				$('.rpLang').each(function(){
					$(this).text($(this).text().replace(/（.*）/,''));
				})
			}
		}
		//$("#ygatetype").text("短信");
		$("#ygatetype").text(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_74"));
		if(spiscumn==0){
			//$("#yspisuncm").text("移动");
			$("#yspisuncm").text(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_75"));
		}else if(spiscumn==1){
			//$("#yspisuncm").text("联通");
			$("#yspisuncm").text(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_76"));
		}else if(spiscumn==21){
			//$("#yspisuncm").text("电信");
			$("#yspisuncm").text(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_77"));
		}else if(spiscumn==5){
			//$("#yspisuncm").text("国外");
			$("#yspisuncm").text(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_78"));
		}
		$("#ygatename").text(gatename).attr('title',gatename);
		$("#yspgate").text(num);
		$("#ysublen").text($("#sublen").val());
		if(isGW){
			if(isSupportEn){
				$("#ysignstr").text(($("#cnSign").val()||'-')+"/"+($("#enSign").val()||'-'));
				$("#ysignstr").attr('title',$("#ysignstr").text());
				$("#ysinglelen").text($("#cnsinglelen").val()+"/"+($("#ensinglelen").val()||'-'));
				$("#esplitmaxwdlen").text($("#esplitmaxwd").val()+"/"+($("#esplitenmaxwd").val()||'-'));
			}else{
				$("#ysignstr").text(($("#cnSign").val()||'-'));
				$("#ysinglelen").text($("#cnsinglelen").val());
				$("#esplitmaxwdlen").text($("#esplitmaxwd").val());
			}
		}else{
			$("#ysignstr").text($("#signstr").val()||'-');
			$("#ysinglelen").text($("#singlelen").val());
			$("#esplitmaxwdlen").text($("#esplitmaxwd").val());
		}
		if($("input[name='signMode']:checked").val()==0){
			//$("#ysigntype").text("自动计算");
			$("#ysigntype").text(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_79"));
		}else if($("input[name='signMode']:checked").val()==1){
			//$("#ysigntype").text("固定长度");
			$("#ysigntype").text(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_80"));
		}
		
		if($("input[name='signdroptype']:checked").val()==1){
			//$("#ysigndroptype").text("是");
			$("#ysigndroptype").text(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_53"));
		}else if($("input[name='signdroptype']:checked").val()==0){
			//$("#ysigndroptype").text("否");
			$("#ysigndroptype").text(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_54"));
		}
		
		if($("input[name='longsms']:checked").val()==1){
			//$("#ylongsms").text("是");
			$("#ylongsms").text(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_53"));
		}else if($("input[name='longsms']:checked").val()==0){
			//$("#ylongsms").text("否");
			$("#ylongsms").text(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_54"));
		}
		
		if($("input[name='splitRule']:checked").val()==1){
			//$("#ysplitrule").text("是");
			$("#ysplitrule").text(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_53"));
		}else if($("input[name='splitRule']:checked").val()==0){
			//$("#ysplitrule").text("否");
			$("#ysplitrule").text(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_54"));
		}
		
		if($("input[name='endsplit']:checked").val()==1){
			//$("#yendsplit").text("是");
			$("#yendsplit").text(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_53"));
		}else if($("input[name='endsplit']:checked").val()==0){
			//$("#yendsplit").text("否");
			$("#yendsplit").text(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_54"));
		}
		if($("input[name='gateprivilege']:checked").val()==1){
			//$("#ygateprivilege").text("前置");
			$("#ygateprivilege").text(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_81"));
		}else if($("input[name='gateprivilege']:checked").val()==0){
			//$("#ygateprivilege").text("后置");
			$("#ygateprivilege").text(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_82"));
		}
		if(isGW && $("input[name='isSupportEn']:checked").val()==1){
			//$("#yisen").text("是");
			$("#yisen").text(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_53"));
		}else{
			//$("#yisen").text("否");
			$("#yisen").text(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_54"));
		}
		$("#yfee").text($("#fee").val()||0.0);
		$('#gateyl').dialog('open');
	}else if(gatetype==2){
		//$("#ycgatetype").text("彩信");
		$("#ycgatetype").text(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_83"));
		$("#ycsublen").text($("#sublen").val());
//		if(isGW){
//			$("#ycsignstr").text(($("#cnSign").val()||'-')+"/"+($("#enSign").val()||'-'));
//		}else{
			$("#ycsignstr").text($("#signstr").val()||'-');
//		}
		if(spiscumn==0){
			//$("#ycspisuncm").text("移动");
			$("#ycspisuncm").text(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_75"));
		}else if(spiscumn==1){
			//$("#ycspisuncm").text("联通");
			$("#ycspisuncm").text(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_76"));
		}else if(spiscumn==21){
			//$("#ycspisuncm").text("电信");
			$("#ycspisuncm").text(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_77"));
		}else if(spiscumn==5){
			//$("#ycspisuncm").text("国外");
			$("#ycspisuncm").text(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_78"));
		}
		$("#ycgatename").text(gatename).attr('title',gatename);
		$("#ycspgate").text(num);
		$("#ycfee").text($("#fee").val()||0.0);
		$('#gateylc').dialog('open');
	}
}


function submitForm(){
	var path=$("#path").val();
	if($.trim($("#spageteNum").val())==""){
		//alert("通道号码不能为空！");
		alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_84"));
	}else if($.trim($("#gatename").val())==""){
		//alert("通道名称不能为空！");
		alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_85"));
	}else{
		$("#bt1").attr("disabled", true);
		$("#bt2").attr("disabled", true);
		$("#bt3").attr("disabled", true);
		$("#bt4").attr("disabled", true);
		var isSupportEn = "0";
		var spiscumn = $.trim($("#spiscumn").val());
		if(spiscumn -5 ==0){
			isSupportEn = $("input[name='isSupportEn']:checked").val();
		}
		$.post(path+"/pas_passage.htm?method=add",{
			spgate:$.trim($("#spageteNum").val()),
			gatename:$.trim($("#gatename").val()),
			areaType:0,
			spiscumn:spiscumn,
			porttype:$("#porttype").val(),
			status:$.trim($("#status").val()),
			longsms:$.trim($("input[name='longsms']:checked").val()),
			riselevel:$("#riselevel").val(),
			maxwords:$("#maxwords").val(),
			singlelen:$("#singlelen").val(),
			cnsinglelen:$('#cnsinglelen').val(),//中英文单条最大长度
			ensinglelen:$('#ensinglelen').val(),
			sublen:$("#sublen").val(),
			signstr:$.trim($("#signstr").val()),
			cnSign:$.trim($("#cnSign").val()),//中英文签名
			enSign:$.trim($("#enSign").val()),
			signlen:$("#signlen2").val(),
			cnsignlen:$('#cnsignlen').val(),//中英文固定长度
			ensignlen:$('#ensignlen').val(),
			endsplit:$("input[name='endsplit']:checked").val(),
			signdroptype:$("input[name='signdroptype']:checked").val(),
			signtype:$("input[name='signMode']:checked").val(),
			splitRule : $("input[name='splitRule']:checked").val(),
			eachSign :$("#eachSign").val(),
			gateprivilege:$("input[name='gateprivilege']:checked").val(),
			isSupportEn:isSupportEn,
			fee : $("#fee").val(),
			gatetype :$.trim($("#gatetype").val()),
			updateorinsert:$.trim($("#updateorinsert").val()),
			keyId:$.trim($("#keyId").val()),
			//后拆分中文最大字数
			esplitmaxwd:$.trim($("#esplitmaxwd").val()),
			//后拆分英文最大字数
			esplitenmaxwd:$.trim($("#esplitenmaxwd").val()),
			id:$.trim($("#id").val())
		},function(result){
			if(result == "itemExists"){
				//alert("此运营商已存在该通道，请重新输入！");
				alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_86"));
				$("#spageteNum").focus();
			}else if(result == "true"){
				//alert("创建成功！");
				alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_87"));
				location.href = location.href;
			}else if(result == "utrue"){
				//alert("修改成功！");
				alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_88"));
				location.href = location.href;
			}else if(result == "error"){
				//alert("操作失败！");
				alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_89"));
			}else if(result == "ufalse"){
				//alert("修改失败！");
				alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_90"));
			}else if(result == "false"){
				//alert("创建失败！");
				alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_91"));
			}
			$("#bt1").attr("disabled", false);
			$("#bt2").attr("disabled", false);
			$("#bt3").attr("disabled", false);
			$("#bt4").attr("disabled", false);
		});
	}
}


function setzhu(){
	if($("#gatetype").val()!=2){
		//$('#zhu').html('注：短信签名格式必须包含半角中括号,如：[梦网]');
		$('#zhu').html(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_92"));
	}else{
		//$('#zhu').html('注：彩信签名格式必须包含半角中括号,如：[梦网]');
		$('#zhu').html(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_92"));
	}
}
$(document).ready(function(){
	$('#spiscumn').change(function(){
		if($(this).val()-5==0){
			$('input[name="isSupportEn"]:eq(0)').attr('checked',true);
		}
		setSign();
	});
	$('input:text').filterChar(/['"<>]/g);
	$('input[name="isSupportEn"]').change(function(){
		setSign();
	});
	$('.sign,#signstr').bind('keyup',function(){
		getSignlen();
	});
	$('.sign,#signstr').bind('blur',function(){
		getSignlen();
		var $self = $(this);
		var signstr=$.trim($self.val());
		$self.val(signstr);
		var pat=/^\[.*\]$/;
		var gatetype=$("#gatetype").val();
		if(signstr!="")
		{
            var len = signstr.length;
            var index =0;
            if ($(this).attr('id') == 'enSign') {
                len = signstr.replace(en_reg, '**').length;
                index =1;
            }
			if( (len>maxlen[index] || len<minlen[index]))
			{
				if(gatetype==2)
				{
					//$("#zhu").html("彩信签名长度应为3-10位！");
					$("#zhu").html(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_94"));
				}
				else
				{
					//$("#zhu").html("短信签名长度应为"+minlen[index]+"-"+maxlen[index]+"位！");
					$("#zhu").html(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_95")+minlen[index]+"-"+maxlen[index]+getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_96"));
				}
				return false;
			}
			if(!checkSignLen($self)){
				return false;
			}
			if(!pat.test(signstr)){
				//签名标识，0：不合法；1：合法
				var signflag = 0;
				//中文签名
				if(index == 0)
				{
					//中文签名支持中文【】
					if(signstr.substring(0,1).charAt(0).charCodeAt() == 12304 
					&&  signstr.substring(signstr.length-1).charAt(0).charCodeAt() == 12305)
					{
						//设置签名为合法
						signflag = 1;
					}
				}
				if(signflag == 0)
				{
					if(gatetype==2)
					{
						//$("#zhu").html("彩信签名输入格式不对！");
						$("#zhu").html(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_97"));
					}
					else
					{
						//$("#zhu").html("短信签名输入格式不对！");
						$("#zhu").html(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_98"));
					}
					
					return false;
				}
			}
		}
		//$("#zhu").html("注：以上通道信息请按通道运营商提供的准确数据录入");
		$("#zhu").html(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_99"));

		return true;
	});
	setSign();
})
function setSign(){
	var option = $('<option value="5">'+getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_100")+'</option>');
	var gatetype = $('#gatetype').val()||1;//通道类型
	var spiscumn = $('#spiscumn').val();//运营商类型
	var signMode = $('input[name="signMode"]:checked').val();//签名模式
	var isGW = spiscumn == 5;
	if(gatetype == 1){//短信
		if($('#spiscumn option').length>0&&$('#spiscumn option[value="5"]').length==0){
			$('#spiscumn').append(option);
		}
		$("#trone").show();
		$("#trtwo").show();
		$("#trthree").show();
		$("#trfour").show();
		$("#trsix").show();
		$("#trseven").show();
		$("#treight").show();
		$('#esplitmaxwd').parents('tr').show();
		//$("#spanname").text("短信签名：");
		$("#spanname").text(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_101"));
		var zhu=$("#zhu").html();
		//if(zhu.indexOf("彩信")!=-1)
		if(zhu.indexOf(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_102"))!=-1)	
		{
			//zhu=zhu.replaceAll("彩信","短信");
			zhu=zhu.replaceAll(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_102"),getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_103"));
			$("#zhu").html(zhu);
		}
		if(isGW){//国外运营商
			var isSupportEn = $('input[name="isSupportEn"]:checked').val();
			$('#spanname').parents('tr').hide();
			$('#signlenname').hide();
			$('#trnine').hide();
			$('.worldSign').show();
			$('.worldSingle').show();
			$("#trsix").hide();
			$('.enmaxwd').show();
			if(isSupportEn-0 == 0){
				$('.worldSign:last').hide();
//				$('#enSign').val('');
//				$('#ensignlenCount').val(0);
//				$('#ensignlen option:selected').attr('selected',false);
//				$('#ensinglelen').val('');
				$('.worldSingle:last').hide();
				$('.enmaxwd').hide();
			}
		}else{
			$('#spanname').parents('tr').show();
			$('#signlenname').show();
			$('#trnine').show();
			$('.worldSign').hide();
			$('.worldSingle').hide();
			$('.enmaxwd').hide();
		}
		changeSignMode(signMode);
	}else{//彩信
		if($('#spiscumn option[value="5"]').length>0){
			$('#spiscumn option[value="5"]').remove();
		}
		$("#trone").hide();
		$("#trtwo").hide();
		$("#trthree").hide();
		$("#trfour").hide();
		$("#trsix").hide();
		$("#trseven").hide();
		$("#treight").hide();
		$("#trnine").hide();
		$('.worldSingle').hide();
		$('.enmaxwd').hide();
		$('#esplitmaxwd').parents('tr').hide();
		//$("#spanname").text("彩信签名：");
		$("#spanname").text(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_104"));
		var zhu=$("#zhu").html();
		//if(zhu.indexOf("短信")!=-1)
		if(zhu.indexOf(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_103"))!=-1)	
		{
			//zhu=zhu.replaceAll("短信","彩信");
			zhu=zhu.replaceAll(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_103"),getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_102"));
			$("#zhu").html(zhu);
		}
		$('#signlenname').hide();
		$('.signlenCount').hide();
//		if(isGW){//国外运营商
//			$('#spanname').parents('tr').hide();
//			$('.worldSign').show();
//			$('.sign').css('width','260px');
//			$('.signlen').hide();
//		}else{
			$('#spanname').parents('tr').show();
			$('.worldSign').hide();
//		}
		$('.isEN').hide();
	}
	
}

window.jQuery.fn.filterChar = function(handle){
	var isFun = (typeof handle == 'function');
	if(!isFun){
		var reg = handle;
		handle = function(){
			var evt = window.event || arguments.callee.caller.arguments[0];
			var src = evt.target || evt.srcElement;
    		var val = src.value;
    		if(reg.test(val)){
    			src.value = val.replace(reg,'');
    		}
		}
	}
	this.each(function(){
		var obj = $(this)[0];
	   if(obj.addEventListener){
	    	obj.addEventListener('input',handle,false);
	    }else if(obj.attachEvent){
	    	obj.attachEvent('onpropertychange',handle);
	    }
	})

}