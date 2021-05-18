//不可输入'
function noquotUserData(obj) {
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
    $(obj).keypress(function (e) {
        var iKeyCode = window.event ? e.keyCode
            : e.which;
        var vv = ((iKeyCode == 39));
        if (vv) {
            if (isIE) {
                event.returnValue = false;
            } else {
                e.preventDefault();
            }
        }
    });
}	
function delBind(uid,gateid){
		//if(confirm("确定解除此通道的绑定吗？")){
		if(confirm(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_12"))){
			$.post("gat_userdata.htm?method=delSpBind",
					{uid : uid,gateid : gateid},
					function(result){
						if(result == "true"){
							//alert("解除绑定成功！");
							alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_13"));
							location.href = location.href;
						}else if(result == "error"){
							//alert("解除绑定失败！");
							alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_14"));
						}else if(result == "false"){
							//alert("解除绑定失败！");
							alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_14"));
						}
					}
			)
		}
	}

    $(document).ready(function() {
    	var findResult = $("#findResult").val();
        if(findResult === "-1") {
            // alert("加载页面失败,请检查网络是否正常!");
            alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_1"));
            return;
        }

        $('#modify').dialog({
            autoOpen: false,
            width:250,
            height:200
        });
        $("#tdhm_key").next(".c_selectBox").find("ul.c_result li").each(function () {
            $(this).click(function () {
                var $option = $("#tdhm_key").find("option[value='"+ $(this).html() +"']");
                $("#gt_userid").val($option.val() == undefined ? "":$option.val());
            });
        });
        $("#tdhm_key_spgate").next(".c_selectBox").find("ul.c_result li").each(function () {
            $(this).click(function () {
                var value = $(this).html().substring(0,$(this).html().indexOf("("));
                var $option = $("#tdhm_key_spgate").find("option[value='"+ value +"']");
                var gate_id = $option.attr("gate_id");
                var gt_spgate = $option.val();
                $("#gt_spgate").val(gt_spgate);
                $("#gate_id").val(gate_id);
            });
        });
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
        $("#content select").isSearchSelect({'width':60,'isInput':false,'zindex':0,'isHideBorder':true,'isHide':true},function(data){
            var idx=$(data.box.self).attr("idx");
            $(data.box.self).change();
        },function(data){
            var self=$(data.box.self);
            self.siblings('.setControl').html(self.find("option:selected").text()).css({'width':'60px'});
        });
        $('#tdhm_key').isSearchSelect({'width':'150','zindex':0},function(data){
            //keyup click触发事件
            $("#gt_userid").val(data.value);
        },function(data){
            //初始化加载
            var val=$("#gt_userid").val();
            if(val){
                data.box.input.val(val);
            }
        });
        $('#tdhm_key_spgate').isSearchSelect({'width':'150','zindex':0},function(data){
            //keyup click触发事件
            $("#gt_spgate").val(data.value);
            if(typeof(data.selected)==="undefined") {
                $("#gate_id").val("");
            } else {
                $("#gate_id").val(data.selected.attr("gate_id"));
            }
        },function(data){
            //初始化加载
            var val=$("#gt_spgate").val();
            if(val){
                data.box.input.val(val);
            }
        });
       // $('#accouttype,#status,#gwstate,#gwbak').isSearchSelect({'width':'150','isInput':false,'zindex':0},function(data){});
        if(typeof(elNameWidth)!='undefined'){
        	$('#bind_spisuncm').isSearchSelect({'width':elNameWidth,'isInput':false,'zindex':0},function(data){});
        }
        $('#search').click(function(){
        	$("#gt_userid").val($("#tdhm_key option:selected").val());
        	$("#gt_spgate").val($("#tdhm_key_spgate option:selected").val());
        	$("#gate_id").val($("#tdhm_key_spgate option:selected").attr("gate_id"));
        	submitForm();
        });
        $('#gatenum').bind('keyup',function(){
            if($(this).val()===''){
                var uid=$("#spuseruid").val();
                var gatetype=$('#spgatetype').val();
                $.post("gat_userdata.htm",{
                        method:"getMsGate",
                        uid:uid,gatetype:gatetype},
                    function(result){
                        var htmlArr=result.split("【MWFG】");
                        if(htmlArr.length=2) {
                            $("#right").html(htmlArr[1]);
                            $("#left").html(htmlArr[0]);
                        } else {
                            $("#right").html("");
                            $("#left").html(result[0]);
                        }
                        //showInfo();
                    });
            }
        });

        //如果不是4.0的皮肤隐藏下拉框
        var skin = $("#skin").val();
        if(skin.lastIndexOf("frame4.0") === -1){
            $("#content select").each(function () {
                $(this).next(".c_selectBox ").css("display","none");
            });
        }
    });

    function toEditByWy(uid,accounttype,keyId) {
        window.location.href="gat_userdata.htm?method=toEdit&wy=1&uid="+uid+"&accouTtype="+accounttype+"&keyId="+keyId;
    }

	function showInfo()
	{
			$("#left label").click(function(e){
			    $(this).addClass('blue').siblings().removeClass('blue');
			    $(this).css("font-size","14px");
			    var s = $(this).attr("gateid");
				var userid=$('#userId').val();
				$.post("gat_userdata.htm",{
					method:"getPassage",
					id:s},
					function(result){
						 $("#right").html("");
						 $("#right").html(result);
				});
			},function(e){
			    $(this).css("color","");
	            $(this).css("font-size","");
			});

	}

    function modify(t) {
        $("#msgcont").empty();
        var text = $(t).siblings(".htmlStr").val();
        $("#msgcont").text(text);
        $('#modify').dialog('open');
    }


	function doSpBind(v,uid,gatetype,gtName){

		$("#gwspbind").css("display","block");
		$("#gwspbind").dialog({
			autoOpen: false,
			height: 480,
			width: 650,
			resizable:false,
			modal:true
		});
		if(gatetype+""=="1"){
			//v+="(<span class='span_nm'>"+gtName+"</span>短信)";
			v+="(<span class='span_nm'>"+gtName+"</span>"+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_15")+")";
		}else{
			v+="(<span class='span_nm'>"+gtName+"</span>"+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_16")+")";
		}
		$("#spbinduser").html(v);
		//$("#spbinduser").val(uid);
		$("#spuseruid").attr("value",uid);
		$("#spgatetype").attr("value",gatetype);
		$.post("gat_userdata.htm",{
			method:"getMsGate",
			uid:uid,gatetype:gatetype},
			function(result){
				var htmlArr=result.split("【MWFG】");
				if(htmlArr.length=2)
				{
					$("#right").html(htmlArr[1]);
					$("#left").html(htmlArr[0]);
				}
				else
				{
					$("#right").html("");
					$("#left").html(result[0]);
				}
				//showInfo();
				//给左边的加绑定事件
				leftjsbind();
				//给右边的通道加绑定事件
				rightjsbind();
				//设置一绑的条数
				setCount();
		});

		//$("#bkw").attr("value",v);
		//$("#fkw").attr("value",v);
		//$("#fkwid").attr("value",i);
		//$("#bkwState").attr("value",s);
		//$("#fkws").attr("value",s);
		$("#gwspbind").dialog('open');
	}
	function switchclick(){
		var getenum = $("#gatenum").val();
		var uid = $("#spuseruid").val();
		var bind_spisuncm = $("#bind_spisuncm").val();
		var spgatetype = $("#spgatetype").val();
		var t = /^\d+$/;
		if(t.test(getenum)||getenum==""){
			$.post("gat_userdata.htm",{
				method:"getMsGate",
				bind_spisuncm:bind_spisuncm,
				getenum:getenum,uid:uid,gatetype:spgatetype},
				function(result){
					var htmlArr=result.split("【MWFG】");
					if(htmlArr.length=2)
					{
						$("#left").html(htmlArr[0]);
					}
					else
					{
						$("#left").html(result[0]);
					}
					//showInfo();
			});

		}else{
			//alert("您输入的通道号码查询有误，请检查！");
			alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_17"));
		}

	}
	function btcancel(){
		$("#gwspbind").dialog('close');
	}

	function bindSure()
	{
	var items="";
	var leftitems ="";
	//var str="移动、联通、电信、";
	var str=getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_18");
	var suncm_Str="";
	if($(".div_con").length<=0)
	{
		//alert("没有需要操作的通道进行绑定！");
		alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_19"));
	    return;
	}
	$("#right").find(".div_con").each(function(){
		items=items+"s"+$(this).attr("gate_id")+",";
		suncm=$(this).attr("suncm");
		if(suncm=="0")
		{
			//suncm_Str="移动、";
			suncm_Str=getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_20");
		}
		else if(suncm=="1")
		{
			//suncm_Str="联通、";
			suncm_Str=getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_21");
		}
		else if(suncm=="21")
		{
			suncm_Str=getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_22");
		}
		str=str.replaceAll(suncm_Str, "");
	 });
	//if(items ==""){
		//alert("未绑定通道账户！");
		//return;
	//}
	if(str!="")
	{
		str=str.substring(0, str.lastIndexOf("、"));
//		if(str=="移动、联通、电信")
//		{
//			str="";
//		}
		//if(!confirm("该通道账户未绑定"+str+"通道，您确定要保存吗？"))
		if(!confirm(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_23")+str+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_24")))
		{
			return;
		}
	}
	var spuseruid=$("#spuseruid").val();
	$.post("gat_userdata.htm?method=addSpBind",{
			uid : spuseruid,
			leftgateid : leftitems,
			gateid : items
		},function(result){
			if(result == "true"){
				//alert("操作成功！");
				alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_25"));
				var url = location.href;
				location.href = location.href;
			}else if(result == "error"){
				//alert("操作异常！");
				alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_26"));
			}else if(result == "false"){
				//alert("操作失败(绑定通道中包含已绑账号的通道)！");
				alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_27"));
			}else
			{
			  alert(result);
			}
		});
	}


	//修改通道账号状态
	function changeStatu(i)
	{

	}

	function validatePort(obj){
		if(obj.value>65535){
			//alert("端口范围0-65535");
			alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_28"));
			obj.value="";
			$(obj).focus();
		}
	}

	function back(){
		var path=$("#path").val();
		location.href=path+'/gat_userdata.htm?method=find&isback=1';
	}
	function spCard(obj){
		var reg=/[^\w]/g,
		val=$(obj).val();
		if(reg.test(val)){
			$(obj).val(val.replace(reg,'').toUpperCase());
		}else{
			$(obj).val(val.toUpperCase());
		}
	}
	//js处理
	function leftjsbind()
	{
		$("#left .div_con").unbind();
		$("#left .div_con").hover(function() {
			$(this).addClass("div_bc_hover");
		}, function() {
			$(this).removeClass("div_bc_hover");
		});
		$("#left .con_unbind").live("click",function(){
			$("#left .div_sel").removeClass("div_sel");
			$(this).addClass("div_sel");
		});
		$("#left .con_bind").die("click");
		$("#left .con_bind").live("click",function(){
			//alert("该通道已被其它通道账号绑定，不允许重复绑定！");
			alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_29"));
		});
		$("#left .con_unbind").die("dblclick");
		$("#left .con_unbind").live("dblclick",function(){
			router();
		});
	}
	//事件绑定
	function rightjsbind()
	{
		$("#right .con_unbind").unbind();
		$("#right .con_unbind").hover(function() {
			$(this).addClass("div_bc_hover");
		}, function() {
			$(this).removeClass("div_bc_hover");
		});
		$("#right .con_unbind").bind("click",function(){
			$("#right .div_sel").removeClass("div_sel");
			$(this).addClass("div_sel");
		});
		$("#right .con_unbind").bind("dblclick",function(){
			moveRight();
		});
	}
	//选择
	function router()
	{
		if($("#left .div_sel").length<=0)
		{
			//alert("请选择要绑定的通道！");
			alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_30"));
			return;
		}
		$("#left .div_sel").each(function(){
			var id=$(this).attr("gate_id");
			if($("#right").find("#div_"+id).length>0)
			{
				//alert("选择记录重复，将自动过滤！");
				alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_31"));
			}
			else
			{
				var htmlStr=$(this).clone();
				$("#right").append(htmlStr);
			}
		});

		$(".div_sel").removeClass("div_sel");
		$(".div_bc_hover").removeClass("div_bc_hover");
		setCount();
		rightjsbind();
	}

	//设置条数
	function setCount()
	{
		var count = $("#right").find(".div_con").length;
		$("#bind_num").html(count);
	}

	//删除
	function moveRight()
	{
		if($("#right .div_sel").length<=0)
		{
			//alert("请选择要删除的通道！");
			alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_32"));
			return;
		}
		if($("#right .div_sel").attr("class").indexOf("con_usebind")!=-1)
		{
			//if(!confirm("该通道已被SP账号使用，您确定要移除吗？"))
			if(!confirm(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_33")))
			{
				return;
			}
		}
		$("#right .div_sel").remove();
		setCount();
	}

	//验证端口格式是否正解
	function isPort(str) {
	 return (isNumber(str) && str < 65536);
	}

	function isNumber(s) {
	    var regu = "^[0-9]+$";
	    var re = new RegExp(regu);
	    return s.search(re) != -1;
	}

	function checkIP2(sIPAddress)
	{
	     var exp=/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
	     var reg = sIPAddress.match(exp);
	     return reg == null;

	}
	
	function checkIP2New(sIPAddress)
	{
		//IP过滤的表达式 1.0.0.1  ~  255.255.255.255
    	var ipPatrn=/^(([1-9]|([1-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))\.)(([0-9]|([0-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))\.){2}([1-9]|([1-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))$/;
    	//域名匹配的表达式
    	var urlPatrn=/^([a-zA-Z]|[0-9])[-a-zA-Z0-9]{0,62}(\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+$/;
    	
    	var dataPatrn=/^\d+(\.\d*)+$/;
    	if(""==sIPAddress){
    		return false;
    	}
    	if(!(urlPatrn.exec(sIPAddress) || ipPatrn.exec(sIPAddress))){
    		return false;
    	}
    	//验证纯数字是否合法
    	if(sIPAddress.match(dataPatrn)!=null){
    		var tempArray=sIPAddress.split("\.");
    		for(var x=0;x<tempArray.length;x++){
    			if(tempArray.length<4){
    				return false;
    			}else if(tempArray[x]>255||tempArray[x]<0){
    				return false;
    			}
    		}
    	}
    	return true;
	}

	//删除sim卡排序
	function removeHandle(obj){
		//if(!confirm("您确定要删除该运营商IP及端口吗？"))
		if(!confirm(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_34")))
		{
			return;
		}
		$(obj).parent().parent().parent().parent().parent().remove();
		window.IE_UNLOAD = true;
		var oSim=$('#yysCont .temp_yys');
		oSim.each(function(i){
			$(this).attr('data',(i+1));
			//$(this).find('.iptext').html('运营商IP地址及端口'+(i+1)+'：');
			$(this).find('.iptext').html(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_35")+" "+(i+1)+'：');
			if(i==0)
			{
				$(this).find('.a3').attr("name","ip[]");
			}
			else
			{
				$(this).find('.a3').attr("name","ip"+i+"[]");
			}
			$(this).find(".llever").attr("name","linklevel"+i);
			$(this).find(".prot_by").attr("name","spPort"+i);
			$(this).find(".lstatus").attr("name","linkstatus"+i);
		})
	}

	//紧接下一个添加
	function addNextBackIp(obj)
	{
		var yys=$('#template').clone();
		var yysSize=$('#yysCont .temp_yys').size();
		var addcount=0;
		var strsptype="";
		if($("#singmutitype").val()=="0"){
			addcount=10;
			//strsptype="多链路多连接";
			strsptype=getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_36");
		}else{
			addcount=5;
			//strsptype="单链路多连接";
			strsptype=getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_37");
		}
		if(yysSize<addcount){
			if(yysSize>0){
				//yys.find('.iptext').html('运营商IP地址及端口'+(yysSize+1)+'：');
				yys.find('.iptext').html(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_38")+' '+(yysSize+1)+'：');
				//yys.find('.a3').attr('name','ip'+yysSize+'[]');
				yys.find('.iptext').attr("id","iptext-aip-"+(yysSize+1));
				yys.find('.aip').attr('id','aip-'+(yysSize+1));
				yys.find('.aip').attr('name','ip'+yysSize+'[]');
				yys.find('input[name="linklevel0"]').attr('name','linklevel'+yysSize);
				yys.find('input[name="spPort0"]').attr('name','spPort'+yysSize);
				yys.find('input[name="linkstatus0"]').attr('name','linkstatus'+yysSize);
				$(obj).parent().parent().parent().parent().parent().after(yys.html());
				var oSim=$('#yysCont .temp_yys');
				oSim.each(function(i){
					$(this).attr('data',(i+1));
					//$(this).find('.iptext').html('运营商IP地址及端口'+(i+1)+'：');
					$(this).find('.iptext').html(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_38")+' '+(i+1)+'：');
					if(i==0)
					{
						//$(this).find('.a3').attr("name","ip[]");
						$(this).find('.aip').attr("name","ip[]");
					}
					else
					{
						//$(this).find('.a3').attr("name","ip"+i+"[]");
						$(this).find('.aip').attr("name","ip"+i+"[]");
					}
					$(this).find(".llever").attr("name","linklevel"+i);
					$(this).find(".prot_by").attr("name","spPort"+i);
					$(this).find(".lstatus").attr("name","linkstatus"+i);

				})
			}else{
				$('#yysCont #yysIP').after(yys.html());
			}
		}else{
			//alert('通道帐号是'+strsptype+',最多添加'+addcount+'个服务器IP及通讯端口');
			alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_39")+strsptype+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_40")+addcount+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_41"));
		}
	}

	function addBackIp()
	{
		var yys=$('#template').clone();
		var yysSize=$('#yysCont .temp_yys').size();
		var addcount=0;
		var strsptype="";
		if($("#singmutitype").val()=="0"){
			addcount=10;
			//strsptype="多链路多连接";
			strsptype=getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_42");
		}else{
			addcount=5;
			strsptype=getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_43");
		}
		if(yysSize<addcount){
			if(yysSize>0){
				//yys.find('.iptext').html('运营商IP地址及端口'+(yysSize+1)+'：');
				yys.find('.iptext').html(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_35")+" "+(yysSize+1)+'：');
				yys.find('.iptext').attr("id","iptext-aip-"+(yysSize+1));
//				yys.find('.a3').attr('name','ip'+yysSize+'[]');
				yys.find('.aip').attr('name','ip'+yysSize+'[]');
				yys.find('.aip').attr('id','aip-'+(yysSize+1));
				yys.find('input[name="linklevel0"]').attr('name','linklevel'+yysSize);
				yys.find('input[name="spPort0"]').attr('name','spPort'+yysSize);
				yys.find('input[name="linkstatus0"]').attr('name','linkstatus'+yysSize);
				$('#yysCont .temp_yys:last').after(yys.html());
			}else{
				$('#yysCont #yysIP').after(yys.html());
			}
		}else{
			//alert('通道帐号是'+strsptype+',最多添加'+addcount+'个服务器IP及通讯端口');
			alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_39")+strsptype+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_40")+addcount+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_41"));

		}
	}

	function chty()
	{
		var spType=$("#spType").val();
		if(spType=="0")
		{
			$(".ch_ty").show();
			//if($('#yysCont .temp_yys').length<=0)
			//{
				//addBackIp();
			//}
		}
		else
		{
			$(".ch_ty").hide();
		}
	}


    //处理添加集群节点
    $(function(){
//        var tmp = "<tr class='cluster-addr'> <td> <span>"+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_44")+"<label class=\"cluster-index\"></label>：</span>" +
//            " </td> <td> <div class=\"backup1\">" +
//            " <input type=text name=\"cluster-ip\" maxlength=3 class=\"a3\" onkeyup=\"mask_dy(this,event)\" onbeforepaste=mask_c()> ." +
//            " <input type=text name=\"cluster-ip\" maxlength=3 class=\"a3\" onkeyup=\"mask_dy(this,event)\" onbeforepaste=mask_c()> . " +
//            "<input type=text name=\"cluster-ip\" maxlength=3 class=\"a3\" onkeyup=\"mask_dy(this,event)\" onbeforepaste=mask_c()> ." +
//            " <input type=text name=\"cluster-ip\" maxlength=3 class=\"a3\" onkeyup=\"mask_dy(this,event)\" onbeforepaste=mask_c()> </div>" +
//            " &nbsp; ： <input type=\"text\" name=\"cluster-port\" placeholder="+ getJsLocaleMessage("txgl","txgl_gateway_text_3") + " maxlength=\"5\" value=\"\" class=\"input_bd prot_by\" style=\"width: 60px;\" onkeyup=\"if(value != value.replace(/[^\\d]/g,'')) value = value.replace(/[^\\d]/g,'')\" onblur=\"validatePort(this)\" />" +
//            " &nbsp; <font color=\"red\" style=\"vertical-align: middle;\">*</font> <a class=\"cluster-add\">"+getJsLocaleMessage("txgl","txgl_gateway_text_4") +"</a>&nbsp;| <a class=\"cluster-del\">"+getJsLocaleMessage("txgl","txgl_gateway_text_5")+"</a> </td> </tr>";
//        
        
        var tmp = "<tr class='cluster-addr'> <td> <span>"+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_44")+"<label class=\"cluster-index\"></label>：</span>" +
        " </td> <td> <div class=\"backup1\">" +
        " <input type=\"text\" name=\"cluster-ip\" id=\"zxx\"  class=\"aip\"  placeholder=\"填写IP地址或域名\" maxlength=\"32\" />" +
        " </div>" +
        " &nbsp; ： <input type=\"text\" name=\"cluster-port\" placeholder="+ getJsLocaleMessage("txgl","txgl_gateway_text_3") + " maxlength=\"5\" value=\"\" class=\"input_bd prot_by\" style=\"width: 60px;\" onkeyup=\"if(value != value.replace(/[^\\d]/g,'')) value = value.replace(/[^\\d]/g,'')\" onblur=\"validatePort(this)\" />" +
        " &nbsp; <font color=\"red\" style=\"vertical-align: middle;\">*</font> <a class=\"cluster-add\">"+getJsLocaleMessage("txgl","txgl_gateway_text_4") +"</a>&nbsp;| <a class=\"cluster-del\">"+getJsLocaleMessage("txgl","txgl_gateway_text_5")+"</a> </td> </tr>";
        //刷新节点序号
        function refreshClusterIndex()
        {
            var $table = $('#inner-ac-info table');
            $table.find('.cluster-index').each(function(index){
                $(this).text(index+1);
            });
        }

        //添加集群节点
        $('.cluster-add').live('click',function(){
            var $table = $('#inner-ac-info table');
            var len = $table.find('.cluster-addr').length;
            if(len >= 10)
            {
                //alert('最多添加10个EMP网关IP地址及端口');
            	alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_45"));
                return;
            }
            var $tmp = $(tmp);
            $tmp.find('.cluster-index').text(len+1);
            $tmp.find('.cluster-index').attr("id","cluster-index-ip-"+(len+1));
            var $tmp2=$tmp.find('input[name=cluster-ip]');
            $tmp2.attr("id","ip-"+(len+1));
            var tempID="ip-"+(len+1);
            //$tmp2.attr("name","name-"+(len+1));
            $("#"+tempID).live('change',function checkDataIpByBlur(){
            	var dbconIp=$("#"+tempID).val();
            	//IP过滤的表达式 1.0.0.1  ~  255.255.255.255
            	var ipPatrn=/^(([1-9]|([1-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))\.)(([0-9]|([0-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))\.){2}([1-9]|([1-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))$/;
            	//域名匹配的表达式
            	var urlPatrn=/^([a-zA-Z]|[0-9])[-a-zA-Z0-9]{0,62}(\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+$/;
            	if(""==dbconIp){
            		/*EMP网关IP地址不能为空*/
            		alert("集群节点"+$("#cluster-index-"+tempID).text()+"的"+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_66"));
            		return false
            	}
            	if(!(urlPatrn.exec(dbconIp) || ipPatrn.exec(dbconIp))){
            		/*EMP网关IP地址错误！*/
            		alert("集群节点"+$("#cluster-index-"+tempID).text()+"的"+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_65"));
            		return false;
            	}
            	
            	var dataPatrn=/^\d+(\.\d*)+$/;
            	//验证纯数字是否合法
            	if(dbconIp.match(dataPatrn)!=null){
            		var tempArray=dbconIp.split("\.");
            		for(var x=0;x<tempArray.length;x++){
            			if(tempArray.length<4){
            				alert("集群节点"+$("#cluster-index-"+tempID).text()+"的"+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_65"));
            				return false;
            			}else if(tempArray[x]>255||tempArray[x]<0){
            				alert("集群节点"+$("#cluster-index-"+tempID).text()+"的"+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_65"));
            				return false;
            			}
            		}
            	}
            	return true;
            });
            $table.append($tmp);
        })

        //删除集群节点
        $('.cluster-del').live('click',function(){
            $(this).parents('.cluster-addr').remove();
            refreshClusterIndex();
        })

        //添加备用
        $('.bak-add').click(function(){
            var $bakAdd = $(this);
            var uid = $bakAdd.attr("uid");
            if(!uid)
            {
               //alert('参数异常！');
            	alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_46"));
                return;
            }

            var total = 0;
            //检查已添加的备用通道数
            $.ajax({
                type: "POST",
                url: "gat_userdata.htm",
                async: false,
                data: {method: 'checkBakNum',uid:uid,isAsync:"yes"},
                beforeSend: function () {
                    page_loading();
                },
                complete: function () {
                    page_complete();
                },
                success: function (data) {
                    if(data == "outOfLogin")
                    {
                        location.href="common/logoutEmp.jsp";
                        return;
                    }

                    total = data;
                }
            });

            if(total >= 1)
            {
               // alert("网关节点已达到最大限制数，不允许继续添加！");
            	alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_47"));
                return;
            }

            var $tr = $(this).parents('tr');
            $('#des-info').attr('uid',uid);
            $('#des-info label').each(function(){
                $this = $(this);
                var index = ($this.attr('name')||'').replace("td-","");
                if(/^\d$/.test(index))
                {
                    $this.text($tr.children().eq(index).text());
                }else{
                    $this.text($bakAdd.attr(index)||'');
                }
            })
            $('#des-info').dialog('open');
            var contentHeight = $('#des-info ul').outerHeight() + $('#des-info div').outerHeight();
            $('#des-info').dialog('option', 'height', contentHeight+70);
        });

        //修改状态
        $('select[name^="userState"]').change(function(){
            var $select = $(this);
            var ks=$select.val();
            var uid = $select.attr('idx');
            var lgcorpcode =$("#lgcorpcode").val();
            var lgusername = $("#lgusername").val();
                $.post("gat_userdata.htm?method=ChangeSate",{uid:uid,status:ks,lgcorpcode:lgcorpcode,lgusername:lgusername},function(result){

                    if (result == "true") {
                        var $selects = $('select[name="userState'+uid+'"]');
                        if(ks == 0)
                        {
                            //$selects.find('option[value="0"]').text('已激活');
                        	$selects.find('option[value="0"]').text(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_48"));
                            //$selects.find('option[value="1"]').text('失效');
                        	$selects.find('option[value="1"]').text(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_49"));
                        }
                        else
                        {
                            //$selects.find('option[value="0"]').text('激活');
                        	$selects.find('option[value="0"]').text(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_50"));
                            //$selects.find('option[value="1"]').text('已失效');
                        	$selects.find('option[value="1"]').text(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_51"));
                        }
                        $selects.find('option[value="'+ks+'"]').attr('selected',true);

                        $selects.next(".c_selectBox").remove();
                        $selects.isSearchSelect({'width':60,'isInput':false,'zindex':0,'isHideBorder':true,'isHide':true},function(data){
                            $(data.box.self).change();
                        },function(data){
                            var self=$(data.box.self);
                            self.siblings('.setControl').html(self.find("option:selected").text()).css({'width':'60px'});
                        });
                       // alert("更改状态成功！");
                        alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_52"));
                    }else{
                        //alert("修改失败！");
                    	alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_53"));
                    }
                });
        })

        if($('#des-info').size() >0)
        {
            $('#des-info').dialog({
                autoOpen: false,
                resizable: false,
                width:340,
                height:210
            });

            $('#bak-ok').click(function(){
                var uid = $('#des-info').attr('uid');
                $.ajax({
                    type: "POST",
                    url: "gat_userdata.htm",
                    data: {method: 'addBak',uid:uid,isAsync:"yes"},
                    beforeSend: function () {
                        page_loading();
                    },
                    complete: function () {
                        page_complete();
                    },
                    success: function (data) {
                        if(data == "outOfLogin")
                        {
                            location.href="common/logoutEmp.jsp";
                            return;
                        }

                        if(data == 'true')
                        {
                            $('#des-info').dialog('close');
                            //alert('添加网关节点成功！');
                            alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_54"));
                            $('#search').click();
                        }else if(data == 'overmax'){
                            //alert("网关节点已达到最大限制数，不允许继续添加！");
                        	alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_55"));
                            $('#des-info').dialog('close');
                            $('#search').click();
                        }else{
                            //alert('添加网关节点失败！');
                        	alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_56"));
                        }

                    }
                });
            });

            $('#bak-cancel').click(function(){
                $('#des-info').dialog('close');
            })
        }

        //动态插入绑定的备用网关信息
//        if($('#cluster-info').size() > 0)
//        {
//            var infos = $('#cluster-info').val();
//            var pat = /\{\d+\|\d:(\d+)\.(\d+)\.(\d+)\.(\d+)\:(\d+)[^\{]*\}/g;
//            var o;
//            var arrs = [];
//            while (o = pat.exec(infos)){
//                arrs.push(o);
//            }
//            for(var i = 1;i< arrs.length;i++){
//                var arr = arrs[i];
//                //console.log("arr"+arr);//{4|0:192.169.7.27:8081},192,169,7,27,8081
//                $('.cluster-add').first().trigger('click');
//                var $tr = $('tr.cluster-addr').eq(i-1);
//                $tr.find('input:text').each(function(index){
//                    $(this).val(arr[index+1]);
//                })
//            }
//        }
      //动态插入绑定的备用网关信息
        if($('#cluster-info').size() > 0)
        {
        	//node={0|0:www.baidu.comadfad:12345},{1|0:www.xiaotao.com:12345},{2|0:www.xiaoxian.com:12121},{3|0:192.169.7.27:8080},{4|0:192.169.7.27:8081}
            var infos = $('#cluster-info').val();
            var arrs = [];
            var temps=infos.split(",");
            var arrays=new Array();
            for(var x=1;x<temps.length;x++){
            	var dests=temps[x].split(":");
            	arrs.push(dests[1]);
            	arrs.push(dests[2].substring(0,dests[2].length-1));
            }
            for(var i = 1;i<temps.length;i++){
                $('.cluster-add').first().trigger('click');
                var $tr = $('tr.cluster-addr').eq(i-1);
                var x=2*(i-1);
                $tr.find('input:text').each(function(index){
                	if(index==0){
                		$(this).val(arrs[x]);
                	}else if(index==1){
                		$(this).val(arrs[x+1]);
                	}
                })
            }
            //删除多余的tr
            //$('div[class=tabContent] table').find("tr.cluster-addr:last").remove();
        }

    });
    //验证EMP网关IP地址
    function checkDataIp(dbconIp){
    	debugger
    	//IP过滤的表达式 1.0.0.1  ~  255.255.255.255
    	var ipPatrn=/^(([1-9]|([1-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))\.)(([0-9]|([0-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))\.){2}([1-9]|([1-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))$/;
    	//域名匹配的表达式
    	var urlPatrn=/^([a-zA-Z]|[0-9])[-a-zA-Z0-9]{0,62}(\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+$/;
    	if(""==dbconIp){
    		/*EMP网关IP地址不能为空*/
    		alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_66"));
    		return false;
    	}
    	if(!(urlPatrn.exec(dbconIp) || ipPatrn.exec(dbconIp))){
    		/*EMP网关IP地址错误！*/
    		alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_65"));
    		return false;
    	}
    	
    	var dataPatrn=/^\d+(\.\d*)+$/;
    	//验证纯数字是否合法
    	if(dbconIp.match(dataPatrn)!=null){
    		var tempArray=dbconIp.split("\.");
    		for(var x=0;x<tempArray.length;x++){
    			if(tempArray.length<4){
    				//验证不合法，给出错误提示信息
    				alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_65"));
    				return false;
    			}else if(tempArray[x]>255||tempArray[x]<0){
    			    //验证不合法，给出提示信息
    				alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_65"));
    				return false;
    			}
    		}
    	}

    	return true;
    };
    //验证运营商IP地址
    function checkDataIp2(obj){
    	var dbconIp=obj.value;
    	var currentID=obj.id;
    	//IP过滤的表达式 1.0.0.1  ~  255.255.255.255
    	var ipPatrn=/^(([1-9]|([1-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))\.)(([0-9]|([0-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))\.){2}([1-9]|([1-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))$/;
    	//域名匹配的表达式
    	var urlPatrn=/^([a-zA-Z]|[0-9])[-a-zA-Z0-9]{0,62}(\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+$/;
    	
    	if(""==dbconIp){
    		alert($("#iptext-"+currentID).text()+"IP或域名不能为空!");
    		return false;
    	}
    	if(!(urlPatrn.exec(dbconIp) || ipPatrn.exec(dbconIp))){
    		/*运营商IP地址错误！*/
    		alert($("#iptext-"+currentID).text()+"IP或域名错误！");
    		return false;
    	}
    	
    	var dataPatrn=/^\d+(\.\d*)+$/;
    	//验证纯数字是否合法
    	if(dbconIp.match(dataPatrn)!=null){
    		var tempArray=dbconIp.split("\.");
    		for(var x=0;x<tempArray.length;x++){
    			if(tempArray.length<4){
    				alert($("#iptext-"+currentID).text()+"IP或域名错误！");
    				return false;
    			}else if(tempArray[x]>255||tempArray[x]<0){
    				alert($("#iptext-"+currentID).text()+"IP或域名错误！");
    				return false;
    			}
    		}
    	}

    	return true;
    };
    //验证集群网关地址完整性
    function checkClusterAddr(){
        var result = true;
        var $table = $('#inner-ac-info table');
        $table.find('.cluster-addr input:text').each(function(){
            var val = $.trim($(this).val());
            var clusterSpan = $(this).parents('tr').find('span:eq(0)').text();
            if(val == '') {
                result = false;
                //alert(clusterSpan+"未填写完整！");
                alert(clusterSpan+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_57"));
            }else if(/\D/.test(val))
            {
                result = false;
                //alert(clusterSpan+"不能包含非数字！");
                alert(clusterSpan+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_58"));
            }else{
                val = parseInt(val);
                var isPort = $(this).attr('name')=='cluster-port';
                if((isPort && val > 65535) || (!isPort && val > 255)){
                    result = false;
                    //alert(clusterSpan+"ip或端口错误！");
                    alert(clusterSpan+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_59"));
                }
            }
            //出现不合法 返回
            if(!result)
            {
                $(this).focus();
                return false;
            }
        });

        if(result)
        {
            //验证是否存在重复
            var arrs = getClusterAddr();
            var mIp = $('#ip1').val()+'.'+$('#ip2').val()+'.'+$('#ip3').val()+'.'+$('#ip4').val()+':'+$('#ptPort').val();
            var status = {};
            status[mIp] = true;
            for(var i= 0;i<arrs.length;i++)
            {
                if(!status[arrs[i]])
                {
                    status[arrs[i]] = true;
                }else {
                   //alert("集群节点"+(i+1)+"地址"+arrs[i]+"重复！");
                	alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_60")+(i+1)+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_61")+arrs[i]+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_62"));
                    result = false;
                    break;
                }
            }
        }
        return result;
    }
    //验证集群网关地址完整性支持配置域名和IP
    function checkClusterAddrNew(){
    	var result = true;
    	var $table = $('#inner-ac-info table');
    	
    	//遍历集群的域名或ip部分
    	$table.find('.cluster-addr input[name=cluster-ip]').each(function(){
	   		var val = $.trim($(this).val());//域名和端口的值
	   		//console.log("集群域名或IP:"+val);
	   		var clusterSpan = $(this).parents('tr').find('span:eq(0)').text();
	   		 
	   	    //IP过滤的表达式 1.0.0.1  ~  255.255.255.255
	     	var ipPatrn=/^(([1-9]|([1-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))\.)(([0-9]|([0-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))\.){2}([1-9]|([1-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5])))$/;
	     	//域名匹配的表达式
	     	var urlPatrn=/^([a-zA-Z]|[0-9])[-a-zA-Z0-9]{0,62}(\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+$/;
	     	
	     	
            if(val == '') {
                result = false;
                //alert(clusterSpan+"未填写完整！");
                alert(clusterSpan+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_57"));
            }else if(!(urlPatrn.exec(val) || ipPatrn.exec(val)))
            {
                result = false;
                //alert(clusterSpan+"不能包含非数字！");
                //alert(clusterSpan+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_58"));
                alert(clusterSpan+"域名或IP配置错误");
            }else{
	            var dataPatrn=/^\d+(\.\d*)+$/;
	        	//验证纯数字是否合法
	        	if(val.match(dataPatrn)!=null){
	        		var tempArray=val.split("\.");
	        		for(var x=0;x<tempArray.length;x++){
	        			if(tempArray.length<4){
	        				result= false;
	        				alert(clusterSpan+"域名或IP配置错误");
	        			}else if(tempArray[x]>255||tempArray[x]<0){
	        				result=false;
	        				alert(clusterSpan+"域名或IP配置错误");
	        			}
	        		}
	        	}
            }
            
	   		//出现不合法 返回
	   		if(!result)
	   		{
	   			$(this).focus();
	   			return false;
	   		}
	   	});
    	if(result){
    		//遍历集群的端口部分
    		$table.find('.cluster-addr input[name=cluster-port]').each(function(){
    			var val = $.trim($(this).val());//域名和端口的值
    			//console.log("集群端口:"+val);
    			var clusterSpan = $(this).parents('tr').find('span:eq(0)').text();
    			if(val == '') {
    				result = false;
    				//alert(clusterSpan+"未填写完整！");
    				alert(clusterSpan+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_57"));
    			}else if(/\D/.test(val))
    			{
    				result = false;
    				//alert(clusterSpan+"不能包含非数字！");
    				//alert(clusterSpan+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_58"));
    				alert(clusterSpan+"端口不能为非数字");
    			}else{
    				val = parseInt(val);
    				var isPort = $(this).attr('name')=='cluster-port';
    				if((isPort && val > 65535) || (!isPort && val > 255)){
    					result = false;
    					//alert(clusterSpan+"ip或端口错误！");
    					alert(clusterSpan+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_59"));
    				}
    			}
    			//出现不合法 返回
    			if(!result)
    			{
    				$(this).focus();
    				return false;
    			}
    		});
    	}
    	
    	if(result)
    	{
    		//验证是否存在重复
    		var arrs = getClusterAddr();
    		//var mIp = $('#ip1').val()+'.'+$('#ip2').val()+'.'+$('#ip3').val()+'.'+$('#ip4').val()+':'+$('#ptPort').val();
    		var mIp = $('#ip1').val()+':'+$('#ptPort').val();
    		var status = {};
    		status[mIp] = true;
    		for(var i= 0;i<arrs.length;i++)
    		{
    			if(!status[arrs[i]])
    			{
    				status[arrs[i]] = true;
    			}else {
    				//alert("集群节点"+(i+1)+"地址"+arrs[i]+"重复！");
    				alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_60")+(i+1)+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_61")+arrs[i]+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_62"));
    				result = false;
    				break;
    			}
    		}
    	}
    	return result;
    }

    //获取网关集群地址数组
    function getClusterAddr(){
        var arrs = [];
        var $table = $('#inner-ac-info table');
        $table.find('.cluster-addr').each(function(){
            var arr = [];
            $(this).find('input:text').each(function(){
                arr.push($(this).val());
            });
            arr = arr.join('.');
            //console.log("集群节点数据first:"+arr)
            arr = arr.replace(/(\.)(\d+)$/,function(p0,p1,p2){
                return ':'+p2;
            });
            arrs.push(arr);
            //console.log("集群节点数据second:"+arr)
        });
        return arrs;
    }

    /*
    检测运营商ip与备用ip地址的重复性
     */
    function checkIPAddr(mIp,bIp)
    {
        var result = true;
        var status = {};
        //status[mIp] = true;
        bIp.replace(',$',"");
        var arrs = bIp.split(',');

        for(var i= 0;i<arrs.length;i++)
        {
            if(!status[arrs[i]])
            {
                status[arrs[i]] = true;
            }else {
                //alert("运营商地址及端口"+(i+1)+"("+arrs[i]+")重复！");
            	alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_63")+(i+1)+"("+arrs[i]+")"+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_64"));
                result = false;
                break;
            }
        }
        return result;
    }