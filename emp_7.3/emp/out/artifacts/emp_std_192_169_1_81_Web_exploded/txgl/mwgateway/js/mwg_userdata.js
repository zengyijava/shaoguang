	function showInfo()
	{
			$("#left label").click(function(e){
			    $(this).addClass('blue').siblings().removeClass('blue');
			    $(this).css("font-size","14px");
			    var s = $(this).attr("gateid");
				var userid=$('#userId').val();
				$.post("mwg_userdata.htm",{
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

	function switchclick(){
		var getenum = $("#gatenum").val();
		var uid = $("#spuseruid").val();
		var bind_spisuncm = $("#bind_spisuncm").val();
		var spgatetype = $("#spgatetype").val();
		var t = /^\d+$/;
		if(t.test(getenum)||getenum==""){
			$.post("mwg_userdata.htm",{
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
			/*您输入的通道号码查询有误，请检查！*/
			alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_17"));
		}
		
	}
	
	function validatePort(obj){
		if(obj.value>65535){
			/*端口范围0-65535*/
			alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_28"));
			obj.value="";
			$(obj).focus();
		}
	}
	
	function back(){
		var path=$("#path").val();
		location.href=path+'/mwg_userdata.htm?method=find&isback=1';
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
			/*该通道已被其它通道账号绑定，不允许重复绑定！*/
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
			/*请选择要绑定的通道！*/
			alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_30"));
			return;
		}
		$("#left .div_sel").each(function(){
			var id=$(this).attr("gate_id");
			if($("#right").find("#div_"+id).length>0)
			{
                /*选择记录重复，将自动过滤！*/
                alert(getJsLocaleMessage("common","common_errorInfo_text_1"));
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
            /*alert("请选择要删除的通道！");*/
            alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_32"));
			return;
		}
		if($("#right .div_sel").attr("class").indexOf("con_usebind")!=-1)
		{
			/*该通道已被SP账号使用，您确定要移除吗？*/
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
	
	//删除sim卡排序
	function removeHandle(obj){
		/*您确定要删除该运营商IP及端口吗？*/
        if(!confirm(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_34")))
        {
			return;
		}
		$(obj).parent().parent().parent().parent().parent().remove();
		window.IE_UNLOAD = true;
		var oSim=$('#yysCont .temp_yys');
		oSim.each(function(i){
			$(this).attr('data',(i+1));
			/*运营商IP地址及端口*/
            $(this).find('.iptext').html(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_35")+' '+(i+1)+'：');
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
		if($("input[name='keepconn']:checked").val()=="1"){
			addcount=10;
			/*主备多链路连接*/
			strsptype=getJsLocaleMessage("txgl","txgl_js_userdata_1");
		}else{
			addcount=5;
			/*单链路连接*/
			strsptype=getJsLocaleMessage("txgl","txgl_js_userdata_2");
		}
		if(yysSize<addcount){
			if(yysSize>0){
                /*运营商IP地址及端口*/
				yys.find('.iptext').html(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_35")+' '+(yysSize+1)+'：');
				yys.find('.a3').attr('name','ip'+yysSize+'[]');
				yys.find('input[name="linklevel0"]').attr('name','linklevel'+yysSize);
				yys.find('input[name="spPort0"]').attr('name','spPort'+yysSize);
				yys.find('input[name="linkstatus0"]').attr('name','linkstatus'+yysSize);
				$(obj).parent().parent().parent().parent().parent().after(yys.html());
				var oSim=$('#yysCont .temp_yys');
				oSim.each(function(i){
					$(this).attr('data',(i+1));
                    /*运营商IP地址及端口*/
					$(this).find('.iptext').html(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_35")+' '+(i+1)+'：');
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
			}else{
				$('#yysCont #yysIP').after(yys.html());
			}
		}else{
            /*alert('通道帐号是'+strsptype+',最多添加'+addcount+'个服务器IP及通讯端口');*/
            alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_39")+strsptype+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_40")+addcount+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_41"));
		}
	}
	
	function addBackIp()
	{
		var yys=$('#template').clone();
		var yysSize=$('#yysCont .temp_yys').size();
		var addcount=0;
		var strsptype="";
		if($("input[name='keepconn']:checked").val()=="1"){
			addcount=10;
            /*主备多链路连接*/
            strsptype=getJsLocaleMessage("txgl","txgl_js_userdata_1");
		}else{
			addcount=5;
            /*单链路连接*/
            strsptype=getJsLocaleMessage("txgl","txgl_js_userdata_2");
		}
		if(yysSize<addcount){
			if(yysSize>0){
                /*运营商IP地址及端口*/
                yys.find('.iptext').html(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_35")+' '+(yysSize+1)+'：');
				yys.find('.a3').attr('name','ip'+yysSize+'[]');
				yys.find('input[name="linklevel0"]').attr('name','linklevel'+yysSize);
				yys.find('input[name="spPort0"]').attr('name','spPort'+yysSize);
				yys.find('input[name="linkstatus0"]').attr('name','linkstatus'+yysSize);
				$('#yysCont .temp_yys:last').after(yys.html());
			}else{
				$('#yysCont #yysIP').after(yys.html());
			}
		}else{
            /*alert('通道帐号是'+strsptype+',最多添加'+addcount+'个服务器IP及通讯端口');*/
            alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_39")+strsptype+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_40")+addcount+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_41"));
		}
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
                /*alert("运营商IP地址及端口"+(i+1)+"("+arrs[i]+")重复！");*/
                alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_63")+(i+1)+"("+arrs[i]+")"+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_64"));
                result = false;
                break;
            }
        }
        return result;
    }