$(function(){
	$('input:text').filterChar(/['"<>]/g);
   noquot("#cpno");
   $("#searchBut").click(function(e){
   var paramJson={
     params:["cpno"],
     form:"pageForm"
    }
 submitCheck(paramJson);//检察是否有非法符号,在head.js里
 });
    //处理签名字数过长的显示问题
//    $('#sms').hover(
//        function(){
//            $(this).stop().animate({width:350});
//        },
//        function(){
//            $(this).stop().animate({width:260});
//        }
//    )
	$("#checkall").click(function(){ 
		$("input[name='checklist']").attr("checked",$(this).attr("checked")); 
	}); 
	$("#btnDel").click(function(){
		var ids;
		var i=0;	
		$('input[name="checklist"]:checked').each(function(index){	
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
			alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_15"));
		}else{
			//if(confirm("您确定要删除"+i+"条信息?")==true){
			if(confirm(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_16")+i+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_17"))==true){
				$.post("updateRoute.htm",{ids:ids,hidOpType:"delete"},function(result){
					if(result>0)
					{
						//alert("删除成功,共删除"+result+"条信息！");
						alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_18")+result+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_19"));					
						document.forms["pageForm"].submit();
					}else{
						//alert("删除失败！");
						alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_20"));						
					}
				});
						
			}
		}
	});
	
	
	$('#birds').keyup(function(){
		var inputkey = $('#birds').val();
		$('.ac_results').hide();
		$('.ac_results').empty();
		$('#spgate>option').each(function(){
			var litxt = $(this).text();
			if(litxt.indexOf(inputkey) > -1)
			{
				var gid = $(this).attr("title");
				//var gtype = $(this).attr("gatetype");
				 $('.ac_results').append('<li liid='+gid+'>'+litxt+'</li>');
			}
		})
		 $('.ac_results>li').hover(function(){
		    $(this).addClass('div_hover_bg').siblings().removeClass('div_hover_bg');
		  },function(){
		   $(this).removeClass('div_hover_bg');
		  }).click(function(){
			  bindClick($(this));
		  });
		  $('.ac_results').show();

	});
	$('.ac_img').bind('click',function(e){
		$('.ac_results').toggle();
		if($('.ac_results').is(":visible"))
		{
			$('.ac_results').empty();
			$('#spgate > option').each(function(){
				var litxt = $(this).text();
				var gid = $(this).attr("title");
				 $('.ac_results').append('<li liid='+gid+' >'+litxt+'</li>');
			});
		 $('.ac_results>li').hover(function(){
			    $(this).addClass('div_hover_bg').siblings().removeClass('div_hover_bg');
			  },function(){
			   $(this).removeClass('div_hover_bg');
			  }).click(function(){
				  bindClick($(this));
		  });
		  $('.ac_results').show();
		}
		e.stopPropagation();
	})
	$("body,html").click(function(){
		$('.ac_results').hide();
		
		
	})

    $('.sign').bind('blur keyup',function(){
        getSignlen();
    });
});

function bindClick($ob)
{
	 $('#birds').val($ob.text());
	    var gid = $ob.attr("liid");
	   // var gtype = $ob.attr("litype");
		$('.ac_results').hide();
		//$("#spgatetype").val(gtype);
		$('#spgate').val($('#spgate > option[title='+gid+']').attr('value'));
		getSublen(gid);
}
function del(i){
	//if(confirm("您确定要删除该路由信息?")==true){
	if(confirm(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_82"))==true){
		
		$.post("updateRoute.htm",{ids:i,hidOpType:"delete"},function(result){
			if(result>0)
			{
				//alert("删除成功,共删除"+result+"条信息！");
				alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_18")+result+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_19"));				
				document.forms["pageForm"].submit();
			}else{
				//alert("删除失败！");
				alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_20"));
			}
		});
	}
}

function checkNull(){
	$("#arOk").attr("disabled", true);
	$("#arNo").attr("disabled", true);
	var memo=$("#memo").val();
	var cpno=$("#cpno").val();
	 var e = /^\d+\([\u4e00-\u9fa5]+\)$/;
	var usercode = $("#usercode").val();
	var usercodeRpl=usercode.replace(/[^\w]/g,'');
	var routeflag = $("#routeflag").val();
	var userType=$('#userType').val();
	if(memo!=null && $("#memo").val().length>201){
          //window.alert("路由描述信息超过200字符！");
		window.alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_21"));
          $("#arOk").attr("disabled", "");
		  $("#arNo").attr("disabled", "");
       }else
		if($("#routeflag").val()==3){
			//alert("请选择路由类型！");
			alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_22"));
			$("#arOk").attr("disabled", "");
			$("#arNo").attr("disabled", "");
		}
		else if($("#spgate").val()==0){
			//window.alert("请选择通道号码！");
			alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_23"));
			$("#arOk").attr("disabled", "");
			$("#arNo").attr("disabled", "");
		}else if(!checkSignstr()){//验证签名合法性
			$("#arOk").attr("disabled", "");
			$("#arNo").attr("disabled", "");
            return;
       }else if(cpno!="" && !/^\d*$/.test(cpno)){
			//alert("子号必须是整数！");
    	   alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_24"));
			$("#arOk").attr("disabled", "");
			$("#arNo").attr("disabled", "");
		}else if(cpno!="" && cpno.length>$('#sublen').val()){
			//alert("通道子号位数不能大于最大通道扩展位数("+$('#sublen').val()+"位)，请重新输入！");
			alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_25")+$('#sublen').val()+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_26"));
			$("#arOk").attr("disabled", "");
			$("#arNo").attr("disabled", "");
		}
		else if(routeflag!=1&&usercode!=usercodeRpl)
		{
			//alert("指令代码只能是数字或字符串组成！");
			alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_27"));
			$("#arOk").attr("disabled", "");
			$("#arNo").attr("disabled", "");
		}
		else{
			var test = $('#hidOpType').val();
			//if($('#hidOpType').val()=="edit" && $('#oldCpno').val() == cpno){
				//document.forms["updateInfo"].submit();
				//return;
			//}
			var userid=$('#userid').val();
			var spgate=$('#spgate').val();
			var spisuncm=$('#spisuncm').val();
			var routeflag=$('#routeflag').val();
			var spId = $('#userType').val();
			var gatetype=$('#gatetype').val();
			var spisuncmId=$.trim($('#spgate').find('> option[selected]').attr("id")).toString().substring(1);
			var tempCpno;
			if(cpno == ""){
				tempCpno = getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_28");
			}else{
				tempCpno = "["+cpno+"]";
			}
			$.post("par_route.htm",{
				method:"checkRoute",
				userid:userid,
				gatetype:gatetype,
				spgate:spgate,
				spisuncm:spisuncm,
				routeflag:routeflag,
				cpno:cpno,
				userType:$('#userType').val(),
				opType:"check"},
			function(result){
					//用逗号截取值。
				var rs =result.split(",");
				//如果范围超过20。。。
				if(rs[1]!=null){
					if(rs[1] =="OutOfLength" ){
						//计算通道长度
						var a=spgate.length+cpno.length;
						//超出了范围20,点取消了。就不创建
						//if(!confirm("该通道长度为"+a+"位，拓展尾号长度为"+rs[2]+"位，总长度超过20位，您确定要创建？")){
						if(!confirm(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_29")+a+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_30")+rs[2]+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_31"))){	
							$("#arOk").attr("disabled", "");
							$("#arNo").attr("disabled", "");
							return;
						}
					}
				}
				if(rs[0]=="cpnoContain") {
					//alert("已存在子号包含所输入子号，不能再使用！");
					alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_32"));
					$("#arOk").attr("disabled", "");
					$("#arNo").attr("disabled", "");
					$("#cpno").select();
				}else if(rs[0] == "upDownSpisuncmExists"){
					//alert("SP账号["+userid+"]已绑定过该运营商的上下行路由！");
					alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_33")+userid+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_34"));
					$("#arOk").attr("disabled", "");
					$("#arNo").attr("disabled", "");
				}else if(rs[0] == "allUpExists"){
					//alert("已存在SP账号为["+userid+"]，通道号为["+spgate+"]的上行路由，请先确认，再尝试添加上/下行路由！");
					alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_35")+userid+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_36")+spgate+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_37"));
					$("#arOk").attr("disabled", "");
					$("#arNo").attr("disabled", "");
				}else if(rs[0] == "allDownExists"){
					alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_38")+userid+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_39"));
					$("#arOk").attr("disabled", "");
					$("#arNo").attr("disabled", "");
				}else if(rs[0] == "allUpDownExists"){
					alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_40")+spgate+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_41")+tempCpno+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_42"));
					$("#arOk").attr("disabled", "");
					$("#arNo").attr("disabled", "");
				}else if(rs[0] == "allupGateExists1"){
					//alert("已存在通道号为["+spgate+"]，子号为"+tempCpno+"的上行路由，请先确认，再尝试添加上/下行路由！");
					alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_40")+spgate+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_41")+tempCpno+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_43"));
					$("#arOk").attr("disabled", "");
					$("#arNo").attr("disabled", "");
				}else if(rs[0] == "upGateExists1"){
					//alert("已存在SP账号为["+userid+"]，通道号为["+spgate+"]的上/下行路由，请先确认，再尝试添加上行路由！");
					alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_35")+userid+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_36")+spgate+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_44"));
					$("#arOk").attr("disabled", "");
					$("#arNo").attr("disabled", "");
				}else if(rs[0] == "upExists"){
					//alert("已存在SP账号为["+userid+"]，通道号为["+spgate+"]的上行路由，请先确认，再尝试添加上行路由！");
					alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_35")+userid+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_36")+spgate+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_45"));
					$("#arOk").attr("disabled", "");
					$("#arNo").attr("disabled", "");
				}else if(rs[0] == "allupGateExists"){
					//alert("已存在通道号为["+spgate+"]，子号为"+tempCpno+"的上/下行路由，请先确认，再尝试添加上行路由！");
					alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_40")+spgate+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_41")+tempCpno+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_44"));
					$("#arOk").attr("disabled", "");
					$("#arNo").attr("disabled", "");
				}else if(rs[0] == "upGateExists"){
					//alert("已存在通道号为["+spgate+"]，子号为"+tempCpno+"的上行路由，请先确认，再尝试添加上行路由！");
					alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_40")+spgate+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_41")+tempCpno+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_45"));
					$("#arOk").attr("disabled", "");
					$("#arNo").attr("disabled", "");
				}else if(rs[0] == "downInUpExists"){
					//alert("SP账号["+userid+"]已绑定过该运营商的上下行路由！");
					alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_38")+userid+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_34"));
					$("#arOk").attr("disabled", "");
					$("#arNo").attr("disabled", "");
				}else if(rs[0] =="downExists"){
					//alert("SP账号["+userid+"]已绑定过该运营商的下行路由！");
					alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_33")+userid+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_39"));
					$("#arOk").attr("disabled", "");
					$("#arNo").attr("disabled", "");
				}else {
					if(cpno != ""){
						document.forms["updateInfo"].submit();
					}
					else
					{
						//if(confirm("确定子号为空吗？"))
						if(confirm(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_46")))	
						{
							document.forms["updateInfo"].submit();
						}
						else
						{
							$("#arOk").attr("disabled", "");
							$("#arNo").attr("disabled", "");
						}
					}
				}
			});
		}
	}
 
function getSmslength(){
	var length=$.trim(document.getElementById("sms").value).length;
	document.getElementById("smslen").value=length;
}

function getSublen(id){
	//var id=$('#spgate').find('> option[selected]').attr("title");
	var button = $('#arOk');
	if(id!=""){
		button.attr('disabled',true);
        $('.signlabel').empty();
		$.post("par_route.htm",{method:"getSublen",id:id},function(result){
			if(!result)
			{
				return;
			}
			result = eval('('+result+')');
			if(result.errcode==-1){
				return;
			}
			button.attr('disabled',false);
			$('#gtId').val(id);
			//$('#isSupportEn').val(result.isSupportEn==1?"是":"否");
			$('#isSupportEn').val(result.isSupportEn==1?getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_47"):getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_48"));
			$('#sublen').val(result.sublen);
			$('#maxwords').val(result.maxWords);
			$('#singlelens').val(result.singleLen);
			$('#multilen1').val(result.multilen1);
			$('#multilen2').val(result.multilen2);
			$('#sms').val(result.sign).attr('maxlength', result.maxsignlen);
            if(result.isSupportEn == 1){
                $('#ensms').val(result.ensign).attr('maxlength', result.maxensignlen);
                //$('.signlabel:eq(0)').text('中文');
                $('.signlabel:eq(0)').text(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_49"));
               // $('.signlabel:eq(1)').text('英文');
                $('.signlabel:eq(1)').text(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_50"));
                $('#ensms').closest('tr').show();
            }else{
                $('#ensms').val('');
                $('#ensms').closest('tr').hide();
            }

			$('#smslen').html(result.signlen);
			$('#signMode').val(result.signMode);
			$('#spgateName').val(result.gateName);
			$('#gatetype').val(result.gatetype);
			$('#spisuncm').val(result.spisuncm);
		});
	}else{
		button.attr('disabled',true);
		$('#sublen').val("");
		$('#maxwords').val("");
		$('#singlelens').val("");
		$('#multilen1').val("");
		$('#multilen2').val("");
		$('#smslen').val("");
		$('#sms').val("");
		$('#spgateName').val("");
		$('#spisuncm').val("");
		//$('#sms').attr("readonly",true);
		$('#gatetype').val("");
		//$('#sms').attr("style","background-color: #EEE");
		if($('#signMode').val()==0){
			$("#smslen").html("0");
		}
	}
}
function getSublenadd(id){

	if(id!=""){
		$.post("par_route.htm",{method:"getSublen",id:id},function(result){
			if(result == "error")
			{
				return;
			}
			if(result.indexOf("sublen:") != 0)
			{
				return;
			}
			result = result.replace("sublen:","");
			var results=new Array();
			results=result.split(",");
			$('#gtId').val(id);
			$('#sublen').val(results[0]);
			$('#maxwords').val(results[1]);
			$('#singlelens').val(results[2]);
			$('#multilen1').val(results[3]);
			$('#multilen2').val(results[4]);
			$('#sms').val(results[6]);
			$('#gatetype').val(results[9]);
			if(results[5]=='0')
			{
				$('#smslen').html($.trim(results[6]).length);
				$('#signMode').attr("value",0);
				$('#signMode2').attr("value",0);
				//changeSignMode(1);
			}else
			{
				$('#smslen').html(results[5]);
				$('#signMode').attr("value",1);
				$('#signMode2').attr("value",1);
				$('#signlen2').val(results[5]);
				//changeSignMode(0);
			}
			$('#spgateName').val(results[7]);
			$('#spisuncm').val(results[8]);
		});
	}else{
		$('#sublen').val("");
		$('#maxwords').val("");
		$('#singlelens').val("");
		$('#multilen1').val("");
		$('#multilen2').val("");
		$('#smslen').val("");
		$('#sms').val("");
		$('#spgateName').val("");
		$('#spisuncm').val("");
		$('#sms').attr("readonly",true);
		$('#gatetype').val("");
		$('#sms').attr("style","background-color: #EEE");
		if($('#signMode').val()==0){
			$("#smslen").html("0");
		}
	}
}
function check(aa){
	$('#usercode').val('');
	if(aa==1){
		document.getElementById("usercodediv").style.display="none";
		document.getElementById("usercodediv2").style.display="none";
	}
	else{
		document.getElementById("usercodediv").style.display="block";
		document.getElementById("usercodediv2").style.display="block";
	}
}

function  getUserType()
{
	$('#userType').val($('#userid').find('> option[selected]').attr('id'));
}
//不可输入',",<,>
function noquot(obj)
{  
	var isIE = false;
	var isFF = false;
	var isSa = false;
	if ((navigator.userAgent.indexOf("MSIE") > 0)
			&& (parseInt(navigator.appVersion) >= 4))
		isIE = true;
	if (navigator.userAgent.indexOf("Firefox") > 0)
		isFF = true;
	if (navigator.userAgent.indexOf("Safari") > 0)
		isSa = true;
	$(obj).keypress(function(e) {
		var iKeyCode = window.event ? e.keyCode
				: e.which;
		var vv = ((iKeyCode == 39)
				|| (iKeyCode == 62)
				|| (iKeyCode == 34)
				||(iKeyCode == 60));
		if (vv) {
			if (isIE) {
				event.returnValue = false;
			} else {
				e.preventDefault();
			}
		}
	});
}

function butg(id1,id2,id3)
{
	if(id1 != null && id1 != "")
	{
		$(id1).attr("disabled",true);
	}
	if(id2 != null && id2 != "")
	{
		$(id2).attr("disabled",true);
	}
	if(id3 != null && id3 != "")
	{
		$(id3).attr("disabled",true);
	}
}

function butk(id1,id2,id3)
{
	if(id1 != null && id1 != "")
	{
		$(id1).attr("disabled",false);
	}
	if(id2 != null && id2 != "")
	{
		$(id2).attr("disabled",false);
	}
	if(id3 != null && id3 != "")
	{
		$(id3).attr("disabled",false);
	}
}
function show(i,corpcode,userid)
{
	if(i == 1)
	{
		//alert("新建账户路由成功！");
		alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_51"));
		location.href="par_route.htm?lgcorpcode="+corpcode+"&lguserid="+userid;
	}else if(i == 2)
	{
		//alert("修改账户路由成功！");
		alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_52"));
		location.href="par_route.htm?isback=1&lgcorpcode="+corpcode+"&lguserid="+userid;
	}else if(i != -1)
	{
		//alert("操作失败！");
		alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_53"));
	}
}
var pat = [/^\[.*\]$/, /^\[[\x00-\xff]*\]$/];
var en_reg = /[\[\]\|\^\{\}\~\\]/g;
var maxlen = [20, 20];
var minlen = [3, 5];
//计算短信签名长度
function getSignlen(){
    var gatetype = $('#userid').find('> option[selected]').attr('accounttype')||1;
	if(gatetype ==1 && $('#signMode').val()==0)//自动计算
	{
        var $sign = $('.sign:visible');
        var signlen = [];
        var multilen1 = ($('#multilen1').val()||'').match(/\d+/g);
        $sign.each(function (index) {
           if(index == 0){
               signlen.push($.trim($(this).val()).length);
           }else{
               var maxLen = $(this).attr('maxlength') || maxlen[index];
               var enSign = $.trim($(this).val());
               var len = enSign.replace(en_reg, '**').length;
               var isMax = false;
               while (len > maxLen) {
                   isMax = true;
                   enSign = enSign.substr(0, enSign.length - 1);
                   len = enSign.replace(en_reg, '**').length;
               }
               if (isMax) {
                   $(this).val(enSign);
               }
               signlen.push(len);
           }
        });
        if(signlen.length>1){
            //$('#smslen').text('中文：'+signlen[0]+" 英文："+signlen[1]);
            $('#smslen').text(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_54")+signlen[0]+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_55")+signlen[1]);
        }else{
            $('#smslen').text(signlen[0]);
        }
        //处理拆分更新
        var index =0;
       var multilen2 = ($('#multilen2').val()||'').replace(/\d+/g,function(){
            var result = multilen1[index]-signlen[index];
           index++;
           return result;
        });
        $('#multilen2').val(multilen2);
	}
}

//验证短信签名
function checkSignstr(){
    var gatetype = $('#userid').find('> option[selected]').attr('accounttype');
    var $sign = $('.sign:visible');
    var flag = false;
    $sign.each(function(index){
        var signstr = $.trim($(this).val());
        $(this).val(signstr);
        var span = $(this).closest('td').siblings().eq(0).children('span').text().replace(/：$/, '');
        if(signstr!=''){
            var len = signstr.length;
            var maxLen = $(this).attr('maxlength')||maxlen[index];
            if (index == 1) {
                len = signstr.replace(en_reg, '**').length;
            }
            if ((len > maxLen || len < minlen[index])) {
                //alert(span + "长度应为" + minlen[index] + "-" + maxLen + "位！");
            	alert(span + getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_56") + minlen[index] + "-" + maxLen + getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_57"));
                return false;
            }
            //正则验证
            if (!pat[index].test(signstr)){
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
	                //alert(span+"输入格式不对！");
					alert(span+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_58"));
	                return false;
				}
            }
            if (index == $sign.length - 1) {
                flag = true;
            }
        }else{
            flag = true;
        }

    })
	return flag;
}

//验证短信签名长度是否为空
function checkSignLen()
{
	var signlen=$('#signlen2').val();
	var signstr=$('#sms').val();
	if($('#signMode').val()==1)
	{
		if(signstr.length>signlen){
			//alert("固定签名长度时，短信签名的字符长度过长！");
			alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_59"));
			return false;
		}
	}
	return true;
}

function checkEditNull(){
	$("#arOk").attr("disabled", true);
	$("#arNo").attr("disabled", true);
	var memo=$("#memo").val();
	var cpno=$("#cpno").val();
	var userType=$('#userType').val();
	var routeflag = $('#routeflag').val();
	var usercode = $("#usercode").val()||'';
	var usercodeRpl=usercode.replace(/[^\w]/g,'');
	
	if(memo!=null && $("#memo").val().length>201){
       // window.alert("路由描述信息超过200字符！");
		 window.alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_60"));
		$("#arOk").attr("disabled", "");
		$("#arNo").attr("disabled", "");
    }else if($("#routeflag").val()==3){
		//alert("请选择路由类型！");
    	alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_61"));
		$("#arOk").attr("disabled", "");
		$("#arNo").attr("disabled", "");
	}
    else if($("#spgate").val()==0 ){
		//window.alert("请选择通道号码！");
		window.alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_62"));
		$("#arOk").attr("disabled", "");
		$("#arNo").attr("disabled", "");
	}else if (!checkSignstr()) {//验证签名合法性
		$("#arOk").attr("disabled", "");
		$("#arNo").attr("disabled", "");
        return;
    }else if(cpno!="" && !/^\d*$/.test(cpno)){
		//alert("子号必须是整数！");
		alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_63"));
    	$("#arOk").attr("disabled", "");
		$("#arNo").attr("disabled", "");
	}else if(cpno!="" && cpno.length>$('#sublen').val()){
		//alert("通道子号位数不能大于最大通道扩展位数("+$('#sublen').val()+"位)，请重新输入！");
		alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_64")+$('#sublen').val()+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_65"));
		$("#arOk").attr("disabled", "");
		$("#arNo").attr("disabled", "");
	}
	else if(routeflag!=1&&usercode!=usercodeRpl)
	{
		//alert("指令代码只能是数字或字符串组成！");
		alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_66"));
		$("#arOk").attr("disabled", "");
		$("#arNo").attr("disabled", "");
	}
	else
	{
		var test = $('#hidOpType').val();
		//if($('#hidOpType').val()=="edit" && $('#oldCpno').val() == cpno){
			//document.forms["updateInfo"].submit();
			//return;
		//}
		var userid=$('#userId').val();
		var spgate=$('#spgate').val();
		var spisuncm=$('#spisuncm').val();
		var routeflag=$('#routeflag').val();
		var spId = $('#userType').val();
		var spisuncmId=$.trim($('#spgate').find('> option[selected]').attr("id")).toString().substring(1);
		var tempCpno;
		if(cpno == ""){
			//tempCpno = "空";
			tempCpno = getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_67");
		}else{
			tempCpno = "["+cpno+"]";
		}
		$.post("par_route.htm",{
			method:"checkUpdateRoute",
			userid:userid,
			spgate:spgate,
			spisuncm:spisuncm,
			routeflag:routeflag,
			cpno:cpno,
			userType:$('#userType').val(),
			opType:"check"},
		function(result){
			//用逗号截取值。
			var rs =result.split(",");
			//如果范围超过20。。。
			if(rs[1]!=null){
				if(rs[1] =="OutOfLength" ){
					//计算通道长度
					var a=spgate.length+cpno.length;
					//超出了范围20,点取消了。就不创建
					//if(!confirm("该通道长度为"+a+"位，拓展尾号最大可为"+rs[2]+"位，总长度超过20位，您确定要修改？")){
					if(!confirm(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_68")+a+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_69")+rs[2]+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_70"))){	
						$("#arOk").attr("disabled", "");
						$("#arNo").attr("disabled", "");
						return;
					}
				}
			}
			if(rs[0]=="cpnoContain") {
				//alert("已存在子号包含所输入子号，不能再使用！");
				alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_71"));
				$("#arOk").attr("disabled", "");
				$("#arNo").attr("disabled", "");
				$("#cpno").select();
			}else if(rs[0] == "allUpDownExists"){
				//alert("已存在通道号为["+spgate+"]，子号为"+tempCpno+"的上/下行路由，请先确认，再尝试修改上/下行路由！");
				alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_72")+spgate+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_73")+tempCpno+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_74"));
				$("#arOk").attr("disabled", "");
				$("#arNo").attr("disabled", "");
			}else if(rs[0] == "allupGateExists1"){
				alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_72")+spgate+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_73")+tempCpno+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_75"));
				$("#arOk").attr("disabled", "");
				$("#arNo").attr("disabled", "");
			}else if(rs[0] == "allupGateExists"){
				alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_72")+spgate+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_73")+tempCpno+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_76"));
				$("#arOk").attr("disabled", "");
				$("#arNo").attr("disabled", "");
			}else if(rs[0] == "upGateExists"){
				alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_72")+spgate+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_73")+tempCpno+getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_77"));
				$("#arOk").attr("disabled", "");
				$("#arNo").attr("disabled", "");
			}else {
				if(cpno != ""){
					document.forms["updateInfo"].submit();
				}else
				{
					//if(confirm("确定子号为空吗？"))
					if(confirm(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_78")))	
					{
						document.forms["updateInfo"].submit();
					}
					else
					{
						$("#arOk").attr("disabled", "");
						$("#arNo").attr("disabled", "");
					}
				} 
			}
		});
	}
}

	function back()
	{
	       var url = 'par_route.htm';
			var conditionUrl = "";
			if(url.indexOf("?")>-1)
			{
				conditionUrl="&";
			}else
			{
				conditionUrl="?";
			}
	        conditionUrl = conditionUrl+backfind("#corpCode");
	        location.href=url+conditionUrl+"isback=1";
	}
	var sww = false;
	function switchclick(){
		 var word = $("#switch").html();
		 	$("#spgate").toggle();
            $("#gatebut").toggle();
            //if(word=="下拉选择"){
            if(word==getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_79")){ 	
            	$("#gatebut").val("");
            	hideswtich("#search_complete");
            	sww = false;
                // $("#switch").html("输入查询");
            	 $("#switch").html(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_80")); 
           // }else if(word=="输入查询"){
            }else if(word==getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_80")){	
            	$("#spgate").val("");
            	hideswtich("");
            	sww = true;
                 //$("#switch").html("下拉选择");
            	$("#switch").html(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_79"));
            }else{

            }
	}
	function hideswtich(hideobj){
		$(hideobj).hide();
		$('#sublen').val("");
		$('#maxwords').val("");
		$('#singlelens').val("");
		$('#multilen1').val("");
		$('#multilen2').val("");
		$('#smslen').val("");
		$('#sms').val("");
		$('#spgateName').val("");
		$('#spisuncm').val("");
		$('#sms').attr("readonly",true);
		$('#gatetype').val("");
		$('#sms').attr("style","background-color: #EEE");
		if($('#signMode').val()==0){
			$("#smslen").html("0");
		}
	}
	jQuery.fn.filterChar = function(handle){
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