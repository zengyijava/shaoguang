	/**
	* 该文件通过查询尚未被引用（由于之前人员所加，待真正确认后删除）
	* @base 网讯素材
	* @author Administrator
	* @date 2013-10-10
	* ***/
		var curDepName = ""
		var time=new Date();
		var bookType = "employee";//标明操作模块。
		
		//**********兼容IE6，IE7，IE8，火狐浏览器模态对话框的宽高
		var isfIE = (document.all) ? true : false;
		var isfIE6 = isfIE && (navigator.appVersion.match(/MSIE 6.0/i)=="MSIE 6.0" ? true : false);
		var app=navigator.appName;
		var ipWidth = 13;
		
		if (isfIE6)
		{
			ipWidth = 0;
		}
		if (app == "Netscape")
		{
			ipWidth = 10;
		}
		//***************
		
		var total=0;// 总页数
		var pageIndex=0;// 当前页数
		var pageSize=0;// 每页记录数
		var totalRec=0;// 总记录数
		function initPage(total,pageIndex,pageSize,totalRec){// 初始化页面
			this.total=total;
			this.pageIndex=pageIndex;
			this.pageSize=pageSize;
			this.totalRec=totalRec;
			
			showPageInfo();
		}
		
		function showPageInfo(){
			var $pa = $('#pageInfo');
			$pa.empty();
		
			$pa.append('<span id="p_goto" onclick="jumpMenu()"></span>');
			if(pageIndex<total){
				$pa.append('<a class="p_a_h" id="p_last" href="javascript:goPage('+total+')"></a>');
				$pa.append('<a id="p_next" href="javascript:goPage('+(pageIndex-0+1)+')" class="p_a_h" ></a>');
			}else{
				$pa.append('<a id="p_last" href="javascript:void(0)"></a>');
				$pa.append('<a id="p_next" href="javascript:void(0)"></a>');
			}
			if(pageIndex<=1){
				$pa.append('<a id="p_back" href="javascript:void(0)"></a>');
				$pa.append('<a id="p_first" href="javascript:void(0)"></a>');
			}else{
				$pa.append('<a class="p_a_h" id="p_back" href="javascript:goPage('+(pageIndex-1)+')"></a>');
				$pa.append('<a class="p_a_h" id="p_first" href="javascript:goPage(1)"></a>');
			}

            /*共  条，第   页， 条/页  */
            $pa.append('<span id="p_info">'+getJsLocaleMessage("common","common_all")+totalRec+getJsLocaleMessage("common","common_js_pageInfo_10")+pageIndex+'/'+total+getJsLocaleMessage("common","common_page_p")+pageSize+getJsLocaleMessage("common","common_js_pageInfo_8")+'</span>');
			$pa.append('<input type="hidden" name="pageSize" id="pageSize" type="text" value="'+pageSize+'"  />');
			$pa.append('<input type="hidden" name="pageIndex" id="txtPage" type="text" value="'+pageIndex+'" />');
		
			var newDiv = document.createElement("div");
			newDiv.id = "p_jump_menu";
			newDiv.style.position = "absolute";
			newDiv.style.top = "0";
			newDiv.style.left = "0";
			newDiv.style.display = "none";
			newDiv.style.zIndex = "100";
			newDiv.className="div_bd div_bg";
            /*如果为英文增加宽度*/
            var empLangName = getJsLocaleMessage("common","common_empLangName");
            if("zh_HK" === empLangName){
                newDiv.style.width="200px";
            }
            /*第 页，条/页 */
            newDiv.innerHTML = "<center><div>"+getJsLocaleMessage("common","common_js_pageInfo_11")+"<input name='page_input' class='input_bd' id='page_input' onfocus='jumpDisplay(1)' onblur='jumpDisplay(0)' style='height: 15px;' id='page_input' type='text' value='"+pageIndex+"' size='4' />"+getJsLocaleMessage("common","common_page_p") +
                "<input  class='input_bd' name='size_input' id='size_input' onfocus='jumpDisplay(1)' onblur='jumpDisplay(0)' style='height: 15px;' id='size_input' type='text' value='"+pageSize+"' size='4' />"+getJsLocaleMessage("common","common_js_pageInfo_8")+"</div>" +
                "<div><a id='p_jump_sure' href='javascript:jumpSure()' ></a></div></center><input type='hidden' id='isHd'/>";
			document.body.appendChild(newDiv);
		}
		function jumpMenu()
		{
			
			var bodyheight=$(window).height();
			var top = $("#p_goto").offset().top;
			var left = $("#p_goto").offset().left;
			left = left - $("#p_jump_menu").width()+$("#p_goto").width();
			if(top+84 > bodyheight)
			{
				top = top - $("#p_jump_menu").height()-4;
			}else
			{
				top = top + $("#p_goto").height()+4;
			}
			
			$("#p_jump_menu").css("left",left+"px")
			$("#p_jump_menu").css("top",top+"px")
			$("#p_jump_menu").show();
			$("#page_input").focus();

            //修改确认
            var confirm=getJsLocaleMessage("common","common_confirm");
            $("#p_jump_sure").html('<font style="color:#000;">'+confirm+'</font>');
		}
		function jumpDisplay(ddd)
		{
			$("#isHd").val(ddd);
			setTimeout("isHid()",200);
		}
		function isHid()
		{
			if($("#isHd").val() == 0)
			{
				 $("#p_jump_menu").hide();
			}
		}
		function jumpSure()
		{
			var page=$("#page_input").val();
			var size=$("#size_input").val();
			var checkPage=/^\d+$/;// 正则表达示验证数字
			var pagetotal=total;
			if(page-0>pagetotal){
                /*输入页数大于最大页数！*/
                alert(getJsLocaleMessage("common","common_js_pageInfo_3"));
				//document.forms['pageForm'].elements["pageIndex"].value="";
				$("#page_input").focus();
				return;
			}
			if(page<1 ||  !checkPage.test(page) ){
                /*跳转页必须为一个大于0的整数！*/
                alert(getJsLocaleMessage("common","common_js_pageInfo_4"));
				$("#page_input").focus();
				return;
			}
			if(size<1 ||  !checkPage.test(size) ){
                /*每页显示数量必须为一个大于0的整数！*/
                alert(getJsLocaleMessage("common","common_js_pageInfo_1"));
				$("#size_input").focus();
				return;
			}
			if(size>500)
			{
                /*每页显示数量不能大于500！*/
                alert(getJsLocaleMessage("common","common_js_pageInfo_2"));
				$("#size_input").focus();
				return;
			}
			$("#pageSize").val($("#size_input").val());
			$("#txtPage").val($("#page_input").val());
			submitForm();
		}
		function showAll()
		{
			/*$("#ifra").contents().find("li span[link] a").each(function(e){
				$(this).removeClass("selected");
			});*/
			/*$("#depId").val($("#curDepId").val());
			$("#depName").text("");
			/*$("#userName").find("> option").eq(0).attr("selected","selected");
			$("#username").val("");
			var booktype = $("#bookType").val();
			if(booktype == "client")
			{
				 $('#servletUrl').val("epl_permissions.htm?method=toClientPm");
				 
				 //路径修改增加 “addrbook” by郭凯
				 $('#ifra').attr('src',$('#inheritPath').val()+'/addrbook/a_addrbookDepTree.jsp?treemethod=getClientDepTreeJson&getType=privi');
				
			}else
			{
				$('#servletUrl').val("epl_permissions.htm?method=toEmployeePm");
				
				//路径修改增加 “addrbook” by郭凯
				 $('#ifra').attr('src',$('#inheritPath').val()+'/addrbook/a_addrbookDepTree.jsp?treemethod=getEntDepTreeJson&getType=privi');
			}
			submitForm();*/
			location.href = location.href;
		}
		function doDel()
		{
		 var bookType2 = $('#bookType').val();
			var i=0;		//统计勾选中的个数
			var dsgIds="";
			$('input[name="checklist"]:checked').each(function(index){	
				dsgIds=dsgIds+$(this).val()+",";
				i=i+1;
			});
			dsgIds=dsgIds.substring(0,dsgIds.lastIndexOf(','));
			if(i==0)
			{
				/*未勾选信息！*/
				alert(getJsLocaleMessage("ydwx","ydwx_js_permission_1"));
				return;
			}else
			{
				/*确定删除勾选信息吗？*/
				if(confirm(getJsLocaleMessage("ydwx","ydwx_js_permission_2"))){
						
				    $.ajax({
				        url: 'epl_permissions.htm?method=getInfo',
				        data:{
				    		ids:dsgIds,
				    		opType:"delete",
				    		bookType:bookType2
				        },
				        type: 'post',
				       // timeout: 3000,
				        error: function(){
                            /*删除失败！*/
                            alert(getJsLocaleMessage("common","common_deleteFailed"));
				           return;
				        },
				        success: function(result){
							if (result>=0) {
								/*删除成功,共移除   条信息！*/
								alert(getJsLocaleMessage("ydwx","ydwx_js_permission_3")+result+getJsLocaleMessage("ydwx","ydwx_js_permission_4"));
								submitForm();
							}else{
                                /*删除失败！*/
                                alert(getJsLocaleMessage("common","common_deleteFailed"));
							}		   
				        }
				    });
				}
			}
		}
		
		function doDelCliDom()
		{
			var i=0;		//统计勾选中的个数
			var dsgIds="";
			$('input[name="checklist"]:checked').each(function(index){	
				dsgIds=dsgIds+$(this).val()+",";
				i=i+1;
			});
			dsgIds=dsgIds.substring(0,dsgIds.lastIndexOf(','));
			if(i==0)
			{
                /*未勾选信息！*/
                alert(getJsLocaleMessage("ydwx","ydwx_js_permission_1"));
				return;
			}else
			{
				var lgcorpcode = $('#lgcorpcode').val();
                /*确定删除勾选信息吗？*/
                if(confirm(getJsLocaleMessage("ydwx","ydwx_js_permission_2"))){
						
				    $.ajax({
				        url: 'mcsa_mcsPermissions.htm?method=getInfo',
				        data:{
				    		ids:dsgIds,
				    		opType:"delete",
				    		lgcorpcode:lgcorpcode
				        },
				        type: 'post',
				       // timeout: 3000,
				        error: function(){
                            /*删除失败！*/
                            alert(getJsLocaleMessage("common","common_deleteFailed"));
				           return;
				        },
				        success: function(result){
							if (result.indexOf("删除") == -1 && result>0) {
                                /*删除成功,共移除   条信息！*/
                                alert(getJsLocaleMessage("ydwx","ydwx_js_permission_3")+result+getJsLocaleMessage("ydwx","ydwx_js_permission_4"));
								submitForm();
							}else if (result.indexOf("删除") != -1 && result.indexOf("0") != -1) {
								/*企业管理员顶级部门权限不能被删除*/
								alert(getJsLocaleMessage("ydwx","ydwx_js_permission_5"));
								return;
							}
							else if(result.indexOf("删除") != -1 && result.indexOf("0") == -1){
								/*共移除  条,其中企业管理员顶级部门权限不能被删除*/
								alert(getJsLocaleMessage("ydwx","ydwx_js_permission_6")+result.substring(0,1)+getJsLocaleMessage("ydwx","ydwx_js_permission_7")+getJsLocaleMessage("ydwx","ydwx_js_permission_5"));
								submitForm();
							}	   
				        }
				    });
				}
			}
		}
		
		function doDelQuanXian()
		{
		 var bookType2 = $('#bookType').val();
			var i=0;		//统计勾选中的个数
			var dsgIds="";
			$('input[name="checklist"]:checked').each(function(index){	
				dsgIds=dsgIds+$(this).val()+",";
				i=i+1;
			});
			dsgIds=dsgIds.substring(0,dsgIds.lastIndexOf(','));
			if(i==0)
			{
                /*未勾选信息！*/
                alert(getJsLocaleMessage("ydwx","ydwx_js_permission_1"));
				return;
			}else
			{
				var lgcorpcode = $('#lgcorpcode').val();
                /*确定删除勾选信息吗？*/
                if(confirm(getJsLocaleMessage("ydwx","ydwx_js_permission_2"))){
						
				    $.ajax({
				        url: 'epl_permissions.htm?method=getInfo',
				        data:{
				    		ids:dsgIds,
				    		opType:"delete",
				    		bookType:bookType2,
				    		lgcorpcode:lgcorpcode
				        },
				        type: 'post',
				       // timeout: 3000,
				        error: function(){
                            /*删除失败！*/
                            alert(getJsLocaleMessage("common","common_deleteFailed"));
				           return;
				        },
				        success: function(result){
                            if (result.indexOf("删除") == -1 && result>0) {
                                /*删除成功,共移除   条信息！*/
                                alert(getJsLocaleMessage("ydwx","ydwx_js_permission_3")+result+getJsLocaleMessage("ydwx","ydwx_js_permission_4"));
                                submitForm();
                            }else if (result.indexOf("删除") != -1 && result.indexOf("0") != -1) {
                                /*企业管理员顶级部门权限不能被删除*/
                                alert(getJsLocaleMessage("ydwx","ydwx_js_permission_5"));
                                return;
                            }
                            else if(result.indexOf("删除") != -1 && result.indexOf("0") == -1){
                                /*共移除  条,其中企业管理员顶级部门权限不能被删除*/
                                alert(getJsLocaleMessage("ydwx","ydwx_js_permission_6")+result.substring(0,1)+getJsLocaleMessage("ydwx","ydwx_js_permission_7")+getJsLocaleMessage("ydwx","ydwx_js_permission_5"));
                                submitForm();
                            }
                        }
				    });
				}
			}
		}
		
		
		function selectUserName(va)
		{
			//$("#curSysUser").text(va);
		}
		
		function bangding()
		{
			var dCodeThird=$('#depId').val();
			if(dCodeThird=="")
			{
				/*请先选择一个机构！*/
				alert(getJsLocaleMessage("ydwx","ydwx_js_permission_8"));
			}else
			{ 
		        $("#com_add_Dom2 curDep").html($('#depName').text());
		        var time=new Date();
		        $('#showSysuser').load(path+'/epl_permissions.htm?method=getSysuserList&opType='+"getSysuserList"+'&bookType='+bookType,{time:time});
		        /*
				$.post("epl_permissions.htm?method=getInfo",{opType:"getSysuserList",bookType:bookType},function(data){
					$("#showSysuser").html(data);
					
					//$("#curSysUser").text("");
					//$("#sysDiv").css("display","block");
					$('#showSysuser table tr').hover(function(){
						$(this).css('background-color','#EDEDED');
					},function(){
						if($(this).find('> td input:checkbox').attr("checked")==true)
						{
							return;
						}
						$(this).css('background-color','#FFFFFF');
					});
					$('#showSysuser table tr').click(function(){
						if($(this).find('> td input:checkbox').attr('checked')==true)
						{
							$(this).find('> td input:checkbox').attr('checked',false);
						}else
						{
							$(this).find('> td input:checkbox').attr('checked',true);
						}
						
						checkRadion();
					});
				});
				*/
			}
		}
		
		$(function(){
		 
		//	$("#bindOk").click(function(){
			
			//});
		//	$("#bindNo").click(function(){
			//    $("#sysDiv").css("display","none");
			//    qdzz();
			//});
			
			//选项卡切换
			$("#s_ud_s_list li").each(function(index){
				$(this).click(function(){
					$("div.s_ud_contentin").removeClass("s_ud_contentin");
					$("li.s_ud_tabin").removeClass("s_ud_tabin");
					$(this).addClass("s_ud_tabin");
					var booktype = $(this).find("> input").val();
					$("#bookType").val(booktype);
					if(booktype!=bookType)
					{
						bookType = booktype;
						$('#servletUrl').val("epl_permissions.htm?method=toEmployeePm");
						$('#ifra').attr('src',$('#iPath').val()+'/epl_permissionsTree.jsp?treemethod=getEmpSecondDepJson');
							 
						var servletUrl = $('#servletUrl').val();
						var time=new Date();
						 
						 
		 				var servletUrl=$('#servletUrl').val();
						// $('#permissionsInfo').load(servletUrl,
							//		{time:time});
						//showAll();
		 				$("#depId").val("");
		 				$("#depName").text("");
		 				$("#userName").find("> option").eq(0).attr("selected","selected");
		 				submitForm();
					}
				});
			});
		});
		function checkRadion()
		{
			$('#showSysuser table tr').each(function(){
				if($(this).find('> td input:checkbox').attr("checked")==true)
				{
					$(this).css('background-color','#EDEDED');
				}else
				{
					$(this).css('background-color','#FFFFFF');
				}
			});
		}
		function submitForm()
		{
			var time=new Date();
			var pageIndex=$('#txtPage').val();
			var pageSize=$('#pageSize').val();
		 	var depCode=$('#depId').val();
			var userName =$('#username').val();
			var servletUrl=$('#servletUrl').val();
			var lguserid=$('#lguserid').val();
			var lgcorpcode=$('#lgcorpcode').val();
			$('#permissionsInfo').load(servletUrl,{time:time,depCode:depCode,pageIndex:pageIndex,pageSize:pageSize,userName:userName,lguserid:lguserid,lgcorpcode:lgcorpcode});
			afterSubmitForm();
		}
		//跳到第几页
		//表单提交
		function goPage(i){
			var page;
			if(i<0){
				page=$("#txtPage").attr("value");
			}else{
				page=i;
			}
			var size=$('#pageSize').attr("value");
			var checkPage=/^\d+$/;// 正则表达示验证数字
			var pagetotal=total;
			if(size<1 ||  !checkPage.test(size) ){
                /*每页显示数量必须为一个大于0的整数！*/
                alert(getJsLocaleMessage("common","common_js_pageInfo_1"));
				return;
			}
			if(size>500)
			{
                /*每页显示数量不能大于500！*/
                alert(getJsLocaleMessage("common","common_js_pageInfo_2"));
				return;
			}
			if(page==null || page==""){
				document.forms['pageForm'].elements["pageIndex"].value =1;
				submitForm();
				return;
			}
			if(page-0>pagetotal){
                /*输入页数大于最大页数！*/
                alert(getJsLocaleMessage("common","common_js_pageInfo_3"));
				document.forms['pageForm'].elements["pageIndex"].value="";
				return;
			}
			if(page<1 ||  !checkPage.test(page) ){
                /*跳转页必须为一个大于0的整数！*/
                alert(getJsLocaleMessage("common","common_js_pageInfo_4"));
				return;
			}
			document.forms['pageForm'].elements["pageIndex"].value=page;
			submitForm();
		}
		//跳到第几页
		//表单提交
		function goPage3(i){
			var page;
			if(i<0){
				page=$("#txtPage3").attr("value");
			}else{
				page=i;
			}
			var size=$('#pageSize3').attr("value");
			var checkPage=/^\d+$/;// 正则表达示验证数字
			var pagetotal=total3;
			if(size<1 ||  !checkPage.test(size) ){
                /*每页显示数量必须为一个大于0的整数！*/
                alert(getJsLocaleMessage("common","common_js_pageInfo_1"));
				return;
			}
			if(size>500)
			{
                /*每页显示数量不能大于500！*/
                alert(getJsLocaleMessage("common","common_js_pageInfo_2"));
				return;
			}
			if(page==null || page==""){
				document.forms['pageForm3'].elements["pageIndex3"].value =1;
				submitForm3();
				return;
			}
			if(page-0>pagetotal){
                /*输入页数大于最大页数！*/
                alert(getJsLocaleMessage("common","common_js_pageInfo_3"));
				document.forms['pageForm3'].elements["pageIndex3"].value="";
				return;
			}
			if(page<1 ||  !checkPage.test(page) ){
                /*跳转页必须为一个大于0的整数！*/
                alert(getJsLocaleMessage("common","common_js_pageInfo_4"));
				return;
			}
			document.forms['pageForm3'].elements["pageIndex3"].value=page;
			submitForm3();
		}
		
		//点击页数
		//表单提交
		function pageClick(i){
			document.forms['pageForm'].elements["pageIndex"].value =i;
			submitForm();
		}
		
		//向前向后翻页
		function page(method){
			var pageIndex=$("#pageIndex").attr("value");
			if(method=="back"){
				pageIndex=(Math.ceil(pageIndex/5)-1)*5;
			}
			else{
				pageIndex=(Math.ceil(pageIndex/5))*5+1;
			}
			document.forms['pageForm'].elements["pageIndex"].value =pageIndex;
			submitForm();
		}
		//员工机构树
		function getEmpDepTree()
		{
			$("#bookType").val('client');
			 DepTree("getEmpSecondDepJson");
		}
		
		function getClinetDepTree()
		{
			 $("#bookType").val('employee');
			 DepTree("getClientDepTreeJson");
		}
		//获取机构树
		function DepTree(method)
		{
			var inheritPath = $("#inheritPath").val();
			var time=new Date();
			$('#DepTree').load(inheritPath+'/a_addrbookDepTree.jsp?treemethod='+method,{time:time});
		}
		
		function doClientBind()
		{
			if($("#permissionType").val()=="1")
			{
				/*对不起，您是个人权限，不能进行绑定操作！*/
				alert(getJsLocaleMessage("common","common_js_pageInfo_9"));
				return;
			}
			var bookType = $("#bookType").val();
			 
			if(bookType =='')
			{
				bookType = 'employee';
			}
		 	var dCodeThird=$('#depId').val();
			if(dCodeThird=="")
			{
                /*请先选择一个机构！*/
                alert(getJsLocaleMessage("ydwx","ydwx_js_permission_8"));
				return;
			} 
			$("#com_add_Dom2").css("display","block");
			var selDepName = $('#depName2').val();
			var selDepId = $('#depId').val();
		    //$("#curDep").html($('#depName2').val());
		    var lguserid = $("#lguserid").val();
		    var lgcorpcode = $("#lgcorpcode").val();
		    $("#bindUserFrame").attr("src",'mcsa_mcsPermissions.htm?method=getSysuserList&pageSize=10&opType='+"getSysuserList"+'&selDepName='+selDepName+'&selDepId='+selDepId+'&lguserid='+lguserid+'&lgcorpcode='+lgcorpcode,{time:time});
			
			$('#com_add_Dom2').dialog('open');
		}
		
		function doBind()
		{
			if($("#permissionType").val()=="1")
			{
                /*对不起，您是个人权限，不能进行绑定操作！*/
                alert(getJsLocaleMessage("common","common_js_pageInfo_9"));
				return;
			}
			var bookType = $("#bookType").val();
			 
			if(bookType =='')
			{
				bookType = 'employee';
			}
		 	var dCodeThird=$('#depId').val();
			if(dCodeThird=="")
			{
                /*请先选择一个机构！*/
                alert(getJsLocaleMessage("ydwx","ydwx_js_permission_8"));
				return;
			} 
			$("#com_add_Dom2").css("display","block");
		    $("#curDep").html($('#depName2').val());
		    var lguserid = $("#lguserid").val();
		    var lgcorpcode = $("#lgcorpcode").val();
		    var selDepName = $('#depName2').val();
			var selDepId = $('#depId').val();
			 $("#bindUserFrame").attr("src",'epl_permissions.htm?method=getSysuserList&pageSize=10&opType='+"getSysuserList"+'&selDepName='+selDepName+'&selDepId='+selDepId+'&bookType='+bookType+'&lguserid='+lguserid+'&lgcorpcode='+lgcorpcode,{time:time});
			 $('#com_add_Dom2').dialog('open');
		}
		var total3=0;// 总页数
		var pageIndex3=0;// 当前页数
		var pageSize3=0;// 每页记录数
		var totalRec3=0;// 总记录数
		function initPage3(total,pageIndex,pageSize,totalRec){// 初始化页面
			this.total3=total;
			this.pageIndex3=pageIndex;
			this.pageSize3=pageSize;
			this.totalRec3=totalRec;
			
			showPageInfo3();
		}
		function showPageInfo3(){
			var $pa = $('#pageInfo3');
			$pa.empty();
			$pa.append(getJsLocaleMessage("common","common_all")).append(totalRec3).append(getJsLocaleMessage("common","common_js_pageInfo_10")).append(pageIndex3).append('/').append(total3).append(getJsLocaleMessage("common","common_page"));
			$pa.append('&nbsp;');
			if(pageIndex3<=1){
				$pa.append('<img src="website/images/first.gif" width="26" height="15" />');
				$pa.append('&nbsp;');
				$pa.append('<img src="website/images/back.gif" width="26" height="15" />');
			}else{
				$pa.append('<a href="javascript:goPage3(1)"><img border="0" src="website/images/first.gif" width="26" height="15" /></a>');
				$pa.append('&nbsp;');
				$pa.append('<a href="javascript:goPage3('+(pageIndex3-1)+')"><img border="0" src="website/images/back.gif" width="26" height="15" /></a>');
			}
			$pa.append('&nbsp;');
			if(pageIndex3<total3){
				$pa.append('<a href="javascript:goPage3('+(pageIndex3-0+1)+')"><img border="0" src="website/images/next.gif" width="26" height="15" /></a>');
				$pa.append('&nbsp;');
				$pa.append('<a href="javascript:goPage3('+total3+')"><img border="0" src="website/images/last.gif" width="26" height="15" /></a>');
			}else{
				$pa.append('<img src="website/images/next.gif" width="26" height="15" />');
				$pa.append('&nbsp;');
				$pa.append('<img src="website/images/last.gif" width="26" height="15" />');
			}
			$pa.append('&nbsp;');
			$pa.append(getJsLocaleMessage("ydwx","ydwx_js_permission_10")).append('<input name="pageSize3" id="pageSize3" type="text" value="'+pageSize3+'" size="4" />').append(getJsLocaleMessage("ydwx","ydwx_js_permission_11"));
			$pa.append('<input name="pageIndex3" id="txtPage3" type="text" value="'+pageIndex3+'" size="4" />');
			$pa.append('&nbsp;');
			$pa.append('<a href="javascript:goPage3(-1)"><img border="0" src="website/images/go.gif" id="goPage3" width="26" height="15" /></a>');
		}
		
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
			var isIE = (document.all) ? true : false;
			var isIE6 = isIE && (navigator.appVersion.match(/MSIE 6.0/i)=="MSIE 6.0" ? true : false);
			var hei=$(window).height();
			var bodyhei = $('.right_info').height();
			if(bodyhei > hei)
			{
				//alert("d");
				hei = bodyhei;
				$('.left_dep').css('height',hei);
				$('.left_dep .list ').css('height',hei-50);
				$('.left_dep .list iframe').css('height',hei-53);
			}else
			{
				//alert("ds");
				$('.left_dep').css('height',hei-20);
				$('.left_dep .list ').css('height',hei-70);
				$('.left_dep .list iframe').css('height',hei-73);
			}
		}
		
		function afterSubmitForm()
		{
			aaaaa = 3;
			setLeftHeight2();
			aaaaa = 0;
		}
