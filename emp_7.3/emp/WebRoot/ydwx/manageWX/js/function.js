// JavaScript Document

//根据传入的checkbox的选中状态设置所有checkbox的选中状态
	 function checkAlls(e,str)    
	{  
		var a = document.getElementsByName(str);    
		var n = a.length;    
		for (var i=0; i<n; i=i+1)    
			a[i].checked =e.checked;    
	}
		
   function numberOf()
	{
	  	  var fileName=$("#filename").val();
	  	  var scdate=$("#scdate").val();
	  	  var enddate=$("#enddate").val();
		var number=$("#pagesize").val();
		var tt = $("#type").val();
		var time=new Date();
		 var query = $("#querySucai").val();
		 var returnFication=$("#fication1").val();
		if(tt=='undefined'){
			if(query=="OK"){
					$('#bookInfo').load('wx_attached.htm?method=LookUp',{
						fileName:fileName,
						scdate:scdate,
						enddate:enddate,
						pagesize:number,
						querySucai:query,
						returnFication:returnFication,
						type:tt
					});
			}
			else
				$('#bookInfo').load('wx_attached.htm?method=LookUp&&pagesize='+number+'&&returnFication='+returnFication,{time:time});
		}
		else
		{
			if(query=="OK"){
				$('#bookInfo').load('wx_attached.htm?method=LookUp',{
						fileName:fileName,
						scdate:scdate,
						enddate:enddate,
						pagesize:number,
						type:tt,
						returnFication:returnFication,
						querySucai:query
					});
			}
			else
				$('#bookInfo').load('wx_attached.htm?method=LookUp&&pagesize='+number+'&&type='+tt+'&&returnFication='+returnFication,{time:time});
		}
		
	}
	
	//网讯预览 //
    function showNetDlg()
    {
   		document.getElementById("netInfoDiv").innerHTML=editor.getContent();
        //禁用select
     	hideOrShowSelect();
        //改变样式
        document.getElementById('showdivBox').className='showNetbox';
        //调整位置至居中
       Location();
        
    }
    function cancelNETDiv()
    {
        document.getElementById('showdivBox').className='hideNetDlg';
        hideOrShowSelect(false);
    }
    
    function Location()
    {
        var netbox=document.getElementById('showdivBox');
        var imgs = document.getElementById('img');
        var netInfoDiv = document.getElementById('netInfoDiv'); 
         if(isFirefox=navigator.userAgent.indexOf("Firefox")>0){
        
			if (netbox !=null && netbox.style.display !="none")
	        {
	            var w=239;
	            var h=450;
	            var oLeft,oTop;
	            if (window.innerWidth)
	            {
	                oLeft=window.pageXOffset+(window.innerWidth-w)/2.5 +"px";
	                oTop=window.pageYOffset+(window.innerHeight-h)/2.5 +"px";
	            }
	            else
	            {
	                var dde=document.documentElement;
	                oLeft=dde.scrollLeft+(dde.offsetWidth-w)/2 +"px";
	                oTop=dde.scrollTop+(dde.offsetHeight-h)/2 +"px";
	            }
	            netInfoDiv.style.left="0.8px";
	            netInfoDiv.style.top="50px"; 
	            imgs.style.left="0px";
	            imgs.style.top="-50px"; 
	            netbox.style.left=oLeft;
	            netbox.style.top=oTop;
	      
	        }
	        
		}else{
	        if (netbox !=null && netbox.style.display !="none")
	        {
	            var w=239;
	            var h=450;
	            var oLeft,oTop;
	            
	            if (window.innerWidth)
	            {
	                oLeft=window.pageXOffset+(window.innerWidth-w)/2 +"px";
	                oTop=window.pageYOffset+(window.innerHeight-h)/2 +"px";
	                
	                
	            }
	            else
	            {
	                var dde=document.documentElement;
	                oLeft=dde.scrollLeft+(dde.offsetWidth-w)/2 +"px";
	                oTop=dde.scrollTop+(dde.offsetHeight-h)/2 +"px";
	                
	            }
	            
	           
	            imgs.style.left="0.8px";
	            imgs.style.top="3px"; 
	            netbox.style.left=oLeft;
	            netbox.style.top=oTop;
	        }
	    }
    }
    
//设置模板	
function SetType(type,netid)
{
	if(type==0)
	{
		/*该网讯已经是系统默认模板！*/
		alert(getJsLocaleMessage("common","common_js_function_1"));
		return;
	}
	/*确定将该网讯设置为系统默认模板？*/
	else if(confirm(getJsLocaleMessage("common","common_js_function_2")))
	{
		window.open("wx_manger.htm?method=SetType&&netid="+netid+"","_self");
	}
}	
//模糊查询
function numericCheck(type)
		{ 
		
		//附件管理 
		if(type==0)
		{
		//编号只能为数字
		   var fileId=$("#fileid").val();
	  	  var fileName=$("#filename").val();
	  	  var fileBanb=$("#filebanb").val();
	  	  var scdate=$("#scdate").val();
	  	  var enddate=$("#enddate").val();
	  	    var t=$("#enddate").val();
	  	  var status=$("#status").val();
	  	  var typeid = $("#type").val();
	  	 var returnFication=$("#fication1").val();
			nr1=document.AttachForm.fileid.value;  
			flg=0;  
			str="";  
			spc=""  
			arw="";  
			for (var i=0;i<nr1.length;i++)
			{  
				cmp="0123456789"  
				tst=nr1.substring(i,i+1)  
				if (cmp.indexOf(tst)<0)
				{  
					flg++;  
					str+=" "+tst;  
					spc+=tst;  
					arw+="^";  
				} else{arw+="_";}  
			}  
			if (flg!=0)
			{
				/*文件编号只能为数字!*/
				alert(getJsLocaleMessage("common","common_js_function_3"));
				$("#fileid").select();
				return false;  
			}  
			else
			{
			    var time=new Date();
			  	if(enddate==undefined){
			  		
			 	 	enddate = "";
			 	 	}
			    if(typeid==undefined){
				    $('#bookInfo').load('wx_attached.htm?method=LookUp',{
						fileId:fileId,
						fileName:fileName,
						fileBanb:fileBanb,
						scdate:scdate,
						enddate:enddate,
						status:status,
						returnFication:returnFication,
						type:null
					});
				}
				else{
					 $('#bookInfo').load('wx_attached.htm?method=LookUp',{
							fileId:fileId,
							fileName:fileName,
							fileBanb:fileBanb,
							scdate:scdate,
							enddate:enddate,
							status:status,
							type:typeid,
							returnFication:returnFication,
							
							querySucai:"OK"
							});
				}
				//$('#bookInfo').load('wx_attached.htm?method=LookUp&&fileId='+fileId+'&&fileName='+fileName+'&&fileBanb='+fileBanb+'&&scdate='+scdate+'&&enddate='+enddate+'&&status='+status,{time:time});
				// window.open('wx_attached.htm?method=LookUp&&fileId='+fileId+'&&fileName='+fileName+'&&fileBanb='+fileBanb+'&&scdate='+scdate+'&&enddate='+enddate+'&&status='+status+'',"_self");

			}
		}
		//网讯管理
		if(type==1)
		{
	   var wxid=$("#wxid").val();
	    var wxname=$("#wxname").val();
	    var zhuangtai=$("#rState").val();
	    var startdate=$("#startdate").val();
	    var enddate=$("#enddate").val();
		
	   wxid=document.managerForm.wxid.value;  
	   if (wxid.indexOf(" ")>-1) 
			{  
			wxid=wxid.replace(/(^\s*)|(\s*$)/g,"");
			} 
		flg=0;  
		str="";  
		spc=""  
		arw="";  
		for (var i=0;i<wxid.length;i++)
		{  
			cmp="0123456789"  
			tst=wxid.substring(i,i+1)  
			if (cmp.indexOf(tst)<0)
			{  
				flg++;  
				str+=" "+tst;  
				spc+=tst;  
				arw+="^";  
			} else{arw+="_";}  
		}  
		if (flg!=0)
		{
            /*网讯编号只能为数字!*/
            alert(getJsLocaleMessage("common","common_js_function_4"));
			$("#wxid").select();
			return;  
		}  
		
		wxname=document.managerForm.wxname.value; 
		if (wxname.indexOf(" ")>-1) 
			{  
			wxname=wxname.replace(/(^\s*)|(\s*$)/g,"");
			} 
		
		if(startdate>enddate)
		{
            /*结束时间不能早于起始时间！*/
            alert(getJsLocaleMessage("common","common_js_function_5"));
			$("#startdate").select();
			return;
		}
		
		else
		{
		  
			wxname=encodeURI(wxname); 
			wxname=encodeURI(wxname); 
			 
			window.open('wx_manger.htm?method=lookup&&wxid='+wxid+'&&wxname='+wxname+'&&zhuangtai='+zhuangtai+'&&startdate='+startdate+'&&enddate='+enddate+'',"_self");
		}
	 }
}
		
		/*单个删除*/
	function del(id,delType)
    { 
		
    	//附件管理删除
	    if(delType==0)
	    {
	    	var flag = document.getElementById("ufile").value;
            /*确定删除所选记录？*/
            if(confirm(getJsLocaleMessage("common","common_js_function_6")))
	  	 	{
	  	 		window.location.href='wx_attached.htm?method=del&&fileid='+id;
	  	 		if(flag=='true')
	  	 		{
                    /*删除失败！*/
                    alert(getJsLocaleMessage("common","common_deleteFailed"));
	  	 		}
	  	 		else
	  	 		{
                    /*删除成功！*/
                    alert(getJsLocaleMessage("common","common_deleteSucceed"));
	  	 		}
	  	 	}	
	  	}
	  	//网讯管理删除
	  	if(delType==1)
	  	{
            /*确定删除所选记录？*/
            if(confirm(getJsLocaleMessage("common","common_js_function_6")))
	  	 	{
	  	 		$.post("wx_manger.htm",{method:"del",id:id},function(data)
				{
					if(data=="true")
					{
                        /*删除成功！*/
                        alert(getJsLocaleMessage("common","common_deleteSucceed"));
						var lguserid = $("#lguserid").val();
						var path=$("#pathUrl").val();
						$("#pageForm").attr("action",path+"/wx_manger.htm?method=find&&lguserid="+lguserid+"&&pageIndex="+pageIndex+"&&pagesize="+pageSize);
						submitForm();
					}else
					{
                        /*删除失败！*/
                        alert(getJsLocaleMessage("common","common_deleteFailed"));
					}
				});
	  	 	}	
	  	}
    }
    
    /*多个删除*/	
	function alldel(delType)
{
	var s=document.getElementsByName("checklist");
	var s2="";
	for(var i=0;i<s.length;i++)//循环遍历出所选数据的id
	{
		if(s[i].checked)
		{
			s2+=s[i].value+',';
		}
	}
	s2=s2.substr(0,s2.length-1);
	if(s2!="")
	{
        /*确定删除所选记录？*/
        if(confirm(getJsLocaleMessage("common","common_js_function_6")))
		{
			//附件管理
			if(delType==0)
			{
				window.location.href='wx_attached.htm?method=delAll&&idlist='+s2;
				var flag = document.getElementById("bb").value;
				if(flag=='true')
				{
                    /*操作失败！*/
                    alert(getJsLocaleMessage("common","common_operateFailed"));
					return;
				}else
				{
                    /*删除成功！*/
                    alert(getJsLocaleMessage("common","common_deleteSucceed"));
					return;
				}
			}
			//网讯管理
			if(delType==1)
			{
				
				window.location.href='wx_manger.htm?method=delAll&&idlist='+s2;
				var flag1 = document.getElementById("ba").value;
				if(flag1=='true')
				{
                    /*删除失败！*/
                    alert(getJsLocaleMessage("common","common_deleteFailed"));
				}else
				{
                    /*删除成功！*/
                    alert(getJsLocaleMessage("common","common_deleteSucceed"));
				}
				$("#checklist").removeAttr("checkbox");
			}
		}
	}
	else
	{
        /*请选择要删除的记录！*/
        alert(getJsLocaleMessage("common","common_js_function_7"));
	}
}
	
	
		
		//修改状态
	function StatusUpdate(status,id,type)
	{	
		if(status==0)
		{
			if(type==0)
			{
				if(confirm('确认修改为未审核？'))
				{
					window.location.href='wx_attached.htm?method=UpdateStatus&&status='+status+'&&id='+id;
				}
			}
			if(type==1)
			{
                /*确认修改为未审核？*/
                if(confirm(getJsLocaleMessage("common","common_js_function_9")))
					window.location.href='wx_manger.htm?method=UpdateStatus&&status='+status+'&&netid='+id;
			}
		}
		if(status==1)
		{
			if(type==0)
			{
                /*确认修改为已审核？*/
                if(confirm(getJsLocaleMessage("common","common_js_function_8")))
				{
					window.location.href='wx_attached.htm?method=UpdateStatus&&status='+status+'&&id='+id;
				}
			}
			if(type==1)
			{
                /*确认修改为未审核？*/
                if(confirm(getJsLocaleMessage("common","common_js_function_9")))
					window.location.href='wx_manger.htm?method=UpdateStatus&&status='+status+'&&netid='+id;
			}
		}
	}
	
	//网讯管理判断网讯的有效期
	     function checkwxTime(timeType,netid)
     {
     	var wxTime    = document.getElementById("wxTime").value;
        var strArray=wxTime.split(" ");   
        var strDate=strArray[0].split("-");   
        var strTime=strArray[1].split(":");   
        var wxTime=new   Date(strDate[0],(strDate[1]-parseInt(1)),strDate[2],strTime[0],strTime[1],strTime[2]);    
      	var recentTimeVar = new Date();
        if(timeType == 1)
        {
      		if(recentTimeVar > wxTime)
      		{
                /*此网讯已超过有效日期*/
                alert(getJsLocaleMessage("common","common_js_function_10"));
      		}else 
      		{
      			window.location.href="wx_ueditor.htm?method=sendMsgInfo&&netid="+netid+"&&sendtype=0";
      		}
      }else
      {
       		window.location.href="wx_ueditor.htm?method=sendMsgInfo&&netid="+netid+"&&sendtype=0 "; 
       }
     }
     
      //网讯访问模糊查询分页
     function netVisitPages(pageNo,pagesize)
	{
	
	if(isNaN(pageNo))
		{
            /*请输入数字*/
            alert(getJsLocaleMessage("common","common_js_function_11"));
			$("#pages").select();
			return;
		}
			var re = /^[1-9]\d*$/;
		  if (!re.test(pageNo))
            {
            /*必须为正整数!*/
            alert(getJsLocaleMessage("common","common_js_function_12"));
            $("#pages").select();
         return ;
     }
		if(pageNo>$("#totalpages").html()){
		 pageNo=$("#totalpages").html();
		}
		if(pageNo<=1){
		pageNo=1;
		} 	
		
	  var timetype = document.getElementById("timeType").value;
	  if(timetype==0){
	    var begin = document.getElementById("beginTime").value;
	    var end = document.getElementById("endTime").value;
	    } else{
	    var begin = document.getElementById("beginmonth").value;
		var end = document.getElementById("endmonth").value;
		
	}
		window.open('wx_count.htm?method=visitDateCount&sType=1&&timetype='+timetype+'&&begintime='+begin+'&&endtime='+end+'&&pageno='+pageNo+'&&pagesize='+pagesize+'',"_self");
	}

		//查看发送网讯详细信息  
		function seeReplyPageList(busid,tableName){
			var lguserid = $("#lguserid").val();
                window.open("wx_count.htm?method=getReplyQueList&pageno=1&busId="+busid+"&tableName="+tableName+"&lguserid="+lguserid,"_self");
       }
		
       	//查看发送网讯详细信息  
		function seeReplyQuesValList(pageid){   
                window.open("wx_count.htm?method=getReplyQueValList&pageno=1&pageID="+pageid+"","_self");
       }
	
	//查看发送网讯详细信息   按批次
		function seeSendBaseInfoByTime(obj,time){
        
                window.open("wx_count.htm?method=findSendBaseInfoByTime&pageno=1&sendTime="+time+"&netId="+obj+"","_self");
       }
	//查看发送网讯详细信息  
		function seeSendBaseInfoByID(obj){

                window.open("wx_count.htm?method=getSendCount&pageno=1&netId="+obj+"","_self");
       }
		function seeSendInfoByID(obj){

                window.open("wx_count.htm?method=getSendInfoByID&pageno=1&CType=0&netId="+obj+"","_self");
       }
       		function seeSendFalInfoByID(obj){
                window.open("wx_count.htm?method=getSendInfoByID&pageno=1&CType=1&netId="+obj+"","_self");
       }
       
	function visitPageByID(obj,netId,netName){
                window.open("wx_count.htm?method=findPageByNetID&netId="+netId+"&netName="+netName+"&pageno=1&pageId="+obj+"","_self");
       }
       
       			/*网讯统计按ID，统计次数*/  
	function seeVisitPageInfoByPID(obj,pName){
                window.open("wx_count.htm?method=getVisitPageInfoByPID&pageno=1&pageName="+pName+"&pageId="+obj+"","_self");
       }
       
       			/*网讯统计按ID，统计次数*/  
	function seeReplyInfo(obj){
                window.open("wx_count.htm?method=replytrustView&pageno=1&pageID="+obj+"","_self");
       }
       
			/*网讯统计按ID，统计次数*/  
	function seeVisitInfoByID(obj){
 
                window.open("wx_count.htm?method=getVisitInfoByID&pageno=1&netId="+obj+"","_self");
       }
       
       		/*网讯统计按ID，统计人数*/
	function seeVisitByphoneID(obj){

                window.open("wx_count.htm?method=getVisitInfoByPhone&pageno=1&netId="+obj+"","_self");
       }
	     
        function replyPagesCount(busID,tName,pageNo,pagesize)
	{   
	
		if(isNaN(pageNo))
		{
            /*请输入数字*/
            alert(getJsLocaleMessage("common","common_js_function_11"));
			$("#pages").select();
			return;
		}
			var re = /^[1-9]\d*$/;
		  if (!re.test(pageNo))
            {
            /*必须为正整数!*/
            alert(getJsLocaleMessage("common","common_js_function_12"));
            $("#pages").select();
         return ;
     }
		if(pageNo>$("#totalpages").html()){
		 pageNo=$("#totalpages").html();
		}
		if(pageNo<=1){
		pageNo=1;
		} 	
	    window.open("wx_count.htm?method=getReplyQueList&busId="+busID+"&tableName="+tName+"&&pageNo="+pageNo+"&&pagesize="+pagesize+" ","_self");
	}
       
        function VisitPagesCount(netidP,nName,pageNo,pagesize)
	{   

		if(isNaN(pageNo))
		{
            /*请输入数字*/
            alert(getJsLocaleMessage("common","common_js_function_11"));
			$("#pages").select();
			return;
		}
			var re = /^[1-9]\d*$/;
		  if (!re.test(pageNo))
            {
            /*必须为正整数!*/
            alert(getJsLocaleMessage("common","common_js_function_12"));
            $("#pages").select();
         return ;
     }
		if(pageNo>$("#totalpages").html()){
		 pageNo=$("#totalpages").html();
		}
		if(pageNo<=1){
		pageNo=1;
		} 	
	    window.open("wx_count.htm?method=findPageByNetID&netId="+netidP+"&&netName="+nName+"&&pageNo="+pageNo+"&&pagesize="+pagesize+" ","_self");
	}
	
		//网讯访问统计分页：按网讯ID
	   function VisitInfoPages(pageId,pageName,pageNo,pagesize)
	{   
		if(isNaN(pageNo))
		{
            /*请输入数字*/
            alert(getJsLocaleMessage("common","common_js_function_11"));
			$("#pages").select();
			return;
		}
			var re = /^[1-9]\d*$/;
		  if (!re.test(pageNo))
            {
            /*必须为正整数!*/
            alert(getJsLocaleMessage("common","common_js_function_12"));
            $("#pages").select();
         return ;
     }
		if(pageNo>$("#totalpages").html()){
		 pageNo=$("#totalpages").html();
		}
		if(pageNo<=1){
		pageNo=1;
		} 	
	   	var nr1=$("#phone").val();

   if (nr1.indexOf(" ")>-1) 
			{  
			nr1=nr1.replace(/(^\s*)|(\s*$)/g,"");

			}   
             var pagesize=$("#pagesize").val();  
		    flg=0;  
		    str="";  
	     	spc=""  
	       	arw="";  
		    for (var i=0;i<nr1.length;i++)
	   	{  
			cmp="0123456789"  
			tst=nr1.substring(i,i+1)  
			if (cmp.indexOf(tst)<0)
			{  
				flg++;  
				str+=" "+tst;  
				spc+=tst;  
				arw+="^";  
			} else{arw+="_";}  
		}  
		if (flg!=0)
		{
            /*请输入正确的手机号码！*/
            alert(getJsLocaleMessage("common","common_js_function_13"));
			$("#phone").focus(); 
			$("#phone").select(); 
			return;  
		} 
		
	  var begintime = $("#beginTime").val();
      var endTime = $("#endTime").val();
     		   
		window.open('wx_count.htm?method=getVisitPageInfoByPID&&pageName='+pageName+'&&pageId='+pageId+'&&phone='+nr1+'&&pageNo='+pageNo+'&&pagesize='+pagesize+'&&begintime='+begintime+'&&endtime='+endTime,"_self");
	}
	
			//网讯访问统计分页：按网讯ID
	   function netVisitInfoPage(netId,pageNo,pagesize)
	{   
		if(isNaN(pageNo))
		{
            /*请输入数字*/
            alert(getJsLocaleMessage("common","common_js_function_11"));
			$("#pages").select();
			return;
		}
			var re = /^[1-9]\d*$/;
		  if (!re.test(pageNo))
            {
            /*必须为正整数!*/
            alert(getJsLocaleMessage("common","common_js_function_12"));
            $("#pages").select();
         return ;
     }
		if(pageNo>$("#totalpages").html()){
		 pageNo=$("#totalpages").html();
		}
		if(pageNo<=1){
		pageNo=1;
		} 	
	   	var nr1=$("#phone").val();

   if (nr1.indexOf(" ")>-1) 
			{  
			nr1=nr1.replace(/(^\s*)|(\s*$)/g,"");

			}   
             var pagesize=$("#pagesize").val();  
		    flg=0;  
		    str="";  
	     	spc=""  
	       	arw="";  
		    for (var i=0;i<nr1.length;i++)
	   	{  
			cmp="0123456789"  
			tst=nr1.substring(i,i+1)  
			if (cmp.indexOf(tst)<0)
			{  
				flg++;  
				str+=" "+tst;  
				spc+=tst;  
				arw+="^";  
			} else{arw+="_";}  
		}  
		if (flg!=0)
		{
            /*请输入正确的手机号码！*/
            alert(getJsLocaleMessage("common","common_js_function_13"));
			$("#phone").focus(); 
			$("#phone").select(); 
			return;  
		}   
		window.open('wx_count.htm?method=getVisitInfoByID&&netId='+netId+'&&phone='+nr1+'&&pageNo='+pageNo+'&&pagesize='+pagesize+'',"_self");
	}
	

	//网讯发送统计分页：按网讯ID
	   function sendNetPages(sendTime,netid,pageNo,pagesize)
	{ 
		if(isNaN(pageNo))
		{
            /*请输入数字*/
            alert(getJsLocaleMessage("common","common_js_function_11"));
			$("#pages").select();
			return;
		}
			var re = /^[1-9]\d*$/;
		  if (!re.test(pageNo))
            {
            /*必须为正整数!*/
            alert(getJsLocaleMessage("common","common_js_function_12"));
            $("#pages").select();
         return ;
     }
		if(pageNo>$("#totalpages").html()){
		 pageNo=$("#totalpages").html();
		}
		if(pageNo<=1){
		pageNo=1;
		} 	
	   	var nr1=$("#phone").val();

   if (nr1.indexOf(" ")>-1) 
			{  
			nr1=nr1.replace(/(^\s*)|(\s*$)/g,"");

			}   
             var pagesize=$("#pagesize").val();  
		    flg=0;  
		    str="";  
	     	spc=""  
	       	arw="";  
		    for (var i=0;i<nr1.length;i++)
	   	{  
			cmp="0123456789"  
			tst=nr1.substring(i,i+1)  
			if (cmp.indexOf(tst)<0)
			{  
				flg++;  
				str+=" "+tst;  
				spc+=tst;  
				arw+="^";  
			} else{arw+="_";}  
		}  
		if (flg!=0)
		{
            /*请输入正确的手机号码！*/
            alert(getJsLocaleMessage("common","common_js_function_13"));
			$("#phone").focus(); 
			$("#phone").select(); 
			return;  
		}   
				window.open('wx_count.htm?method=findSendBaseInfoByTime&&sendTime='+sendTime+'&&netId='+netid+'&&phone='+nr1+'&&pageNo='+pageNo+'&&pagesize='+pagesize+'',"_self");
	}
	
	//网讯发送统计分页：按网讯ID
	   function SendallInfoPages(type,netid,pageNo,pagesize)
	{ 
		if(isNaN(pageNo))
		{
            /*请输入数字*/
            alert(getJsLocaleMessage("common","common_js_function_11"));
			$("#pages").select();
			return;
		}
			var re = /^[1-9]\d*$/;
		  if (!re.test(pageNo))
            {
            /*必须为正整数!*/
            alert(getJsLocaleMessage("common","common_js_function_12"));
            $("#pages").select();
         return ;
     }
		if(pageNo>$("#totalpages").html()){
		 pageNo=$("#totalpages").html();
		}
		if(pageNo<=1){
		pageNo=1;
		} 	
	   	var nr1=$("#phone").val();

   if (nr1.indexOf(" ")>-1) 
			{  
			nr1=nr1.replace(/(^\s*)|(\s*$)/g,"");

			}   
             var pagesize=$("#pagesize").val();  
		    flg=0;  
		    str="";  
	     	spc=""  
	       	arw="";  
		    for (var i=0;i<nr1.length;i++)
	   	{  
			cmp="0123456789"  
			tst=nr1.substring(i,i+1)  
			if (cmp.indexOf(tst)<0)
			{  
				flg++;  
				str+=" "+tst;  
				spc+=tst;  
				arw+="^";  
			} else{arw+="_";}  
		}  
		if (flg!=0)
		{
            /*请输入正确的手机号码！*/
            alert(getJsLocaleMessage("common","common_js_function_13"));
			$("#phone").focus(); 
			$("#phone").select(); 
			return;  
		}   
		window.open('wx_count.htm?method=getSendInfoByID&&CType='+type+'&&netId='+netid+'&&phone='+nr1+'&&pageNo='+pageNo+'&&pagesize='+pagesize+'',"_self");
	}
	
	//网讯发送统计分页：按网讯ID
	   function SendInfoPages(netid,pageNo,pagesize)
	{ 
		if(isNaN(pageNo))
		{
            /*请输入数字*/
            alert(getJsLocaleMessage("common","common_js_function_11"));
			$("#pages").select();
			return;
		}
			var re = /^[1-9]\d*$/;
		  if (!re.test(pageNo))
            {
            /*必须为正整数!*/
            alert(getJsLocaleMessage("common","common_js_function_12"));
            $("#pages").select();
         return ;
     }
		if(pageNo>$("#totalpages").html()){
		 pageNo=$("#totalpages").html();
		}
		if(pageNo<=1){
		pageNo=1;
		} 	
	   	var nr1=$("#phone").val();

   if (nr1.indexOf(" ")>-1) 
			{  
			nr1=nr1.replace(/(^\s*)|(\s*$)/g,"");

			}   
             var pagesize=$("#pagesize").val();  
		    flg=0;  
		    str="";  
	     	spc=""  
	       	arw="";  
		    for (var i=0;i<nr1.length;i++)
	   	{  
			cmp="0123456789"  
			tst=nr1.substring(i,i+1)  
			if (cmp.indexOf(tst)<0)
			{  
				flg++;  
				str+=" "+tst;  
				spc+=tst;  
				arw+="^";  
			} else{arw+="_";}  
		}  
		if (flg!=0)
		{
            /*请输入正确的手机号码！*/
            alert(getJsLocaleMessage("common","common_js_function_13"));
			$("#phone").focus(); 
			$("#phone").select(); 
			return;  
		}   
		window.open('wx_count.htm?method=getSendBaseInfoByID&&netId='+netid+'&&phone='+nr1+'&&pageNo='+pageNo+'&&pagesize='+pagesize+'',"_self");
	}
	
			//网讯访问统计分页：按网讯ID PHONE
	   function VisitInfoPagesP(netid,pageNo,pagesize)
	{  
   	if(isNaN(pageNo))
		{
            /*请输入数字*/
            alert(getJsLocaleMessage("common","common_js_function_11"));
			$("#pages").select();
			return;
		}
			var re = /^[1-9]\d*$/;
		  if (!re.test(pageNo))
            {
            /*必须为正整数!*/
            alert(getJsLocaleMessage("common","common_js_function_12"));
            $("#pages").select();
         return ;
     }
		if(pageNo>$("#totalpages").html()){
		 pageNo=$("#totalpages").html();
		}
		if(pageNo<=1){
		pageNo=1;
		} 	
	  	var phone=$("#phone").val();
	  	
		window.open('wx_count.htm?method=getVisitInfoByPhone&&netId='+netid+'&&phone='+phone+'&&pageNo='+pageNo+'&&pagesize='+pagesize+'',"_self");
	}
     
     		//网讯访问统计分页：按时间 
	   function VisitByTimePages(time,pageNo,pagesize)
	{   
		if(isNaN(pageNo))
		{
            /*请输入数字*/
            alert(getJsLocaleMessage("common","common_js_function_11"));
			$("#pages").select();
			return;
		}
			var re = /^[1-9]\d*$/;
		  if (!re.test(pageNo))
            {
            /*必须为正整数!*/
            alert(getJsLocaleMessage("common","common_js_function_12"));
            $("#pages").select();
         return ;
     }
		if(pageNo>$("#totalpages").html()){
		 pageNo=$("#totalpages").html();
		}
		if(pageNo<=1){
		pageNo=1;
		} 	
      var phone=$("#phone").val();
		window.open('wx_count.htm?method=getVisitBytime&&time='+time+'&&phone='+phone+'&&pageNo='+pageNo+'&&pagesize='+pagesize+'',"_self");
	}
	   //按时间统计，人数
          function VisitByTimePagesP(time,pageNo,pagesize){
          	if(isNaN(pageNo))
		{
            /*请输入数字*/
            alert(getJsLocaleMessage("common","common_js_function_11"));
			$("#pages").select();
			return;
		}
			var re = /^[1-9]\d*$/;
		  if (!re.test(pageNo))
            {
            /*必须为正整数!*/
            alert(getJsLocaleMessage("common","common_js_function_12"));
            $("#pages").select();
         return ;
     }
		if(pageNo>$("#totalpages").html()){
		 pageNo=$("#totalpages").html();
		}
		if(pageNo<=1){
		pageNo=1;
		} 	
          var phone=$("#phone").val();
         window.open('wx_count.htm?method=getVisitBytimeWithPhone&&time='+time+'&&phone='+phone+'&&pageNo='+pageNo+'&&pagesize='+pagesize+'',"_self");

       }
     
       //网讯发送统计报表模糊查询分页    netVisitPages
     function netVisitPagesByID(pageNo,pagesize)
	{   
		if(isNaN(pageNo))
		{
            /*请输入数字*/
            alert(getJsLocaleMessage("common","common_js_function_11"));
			$("#pages").select();
			return;
		}
		var re = /^[1-9]\d*$/;
		  if (!re.test(pageNo))
            {
            /*必须为正整数!*/
            alert(getJsLocaleMessage("common","common_js_function_12"));
            $("#pages").select();
         return ;
     }
		
		if(pageNo>$("#totalpages").html()){
		 pageNo=$("#totalpages").html();
		}
		if(pageNo<=1){
		pageNo=1;
		} 	
		var netname=$("#nname").val();
	    var opName=$("#chUser").val();
	    var netid=$("#nid2").val();
	    
	    	if (netid.indexOf(" ")>-1) 
			{  
			netid=netid.replace(/(^\s*)|(\s*$)/g,"");
			
			} 
			   if(isNaN(netid)){
                   /*请输入正确的编号！*/
                   alert(getJsLocaleMessage("common","common_js_function_14"));
                     return;
                   }
						
		if (netname.indexOf(" ")>-1) 
			{  
			netname=netname.replace(/(^\s*)|(\s*$)/g,"");

			}else if(!reg_exp(netname)){
            /*网讯名称含有非法字符!*/
            alert(getJsLocaleMessage("common","common_js_function_15"));
					$("#nname").select();
					    return;
			}
			
				if (opName.indexOf(" ")>-1) 
			{  
			opName=opName.replace(/(^\s*)|(\s*$)/g,"");
			
			} 
	       
		window.open('wx_count.htm?method=visitDateCount&&netId='+netid+'&&netName='+netname +'&&opName='+opName +'&&pageNo='+pageNo+'&&pagesize='+pagesize+'',"_self");
	}
     
     
//特殊字符串验证。过滤
function reg_exp(s) { 
	
	var pattern = "`~!#$^&*={};',<>?~！#￥……&*\\——|‘；：”“'\"%～";
	 
	for (var i = 0; i < pattern.length; i++) {

		if(s.indexOf(pattern.charAt(i)) > -1){
			return false;
		}
	} 
	
	return true; 
}
	
	
	 function netAccountPage(pageNo,pagesize)
	{   
		if(isNaN(pageNo))
		{
			/*请输入数字*/
			alert(getJsLocaleMessage("common","common_js_function_11"));
			$("#pages").select();
			return;
		}
		var re = /^[1-9]\d*$/;
		  if (!re.test(pageNo))
            {
            /*必须为正整数!*/
            alert(getJsLocaleMessage("common","common_js_function_12"));
            $("#pages").select();
         return ;
     }
		
		if(pageNo>$("#totalpages").html()){
		 pageNo=$("#totalpages").html();
		}
		if(pageNo<=1){
		pageNo=1;
		} 	
	    
		window.open('wx_count.htm?method=accountCheckByTime&&pageNo='+pageNo+'&&pagesize='+pagesize+'',"_self");
	}
     
       //网讯发送统计报表模糊查询分页    
     function netSendPages(pageNo,pagesize)
	{   
			if(isNaN(pageNo))
		{
			/*请输入数字*/
			alert(getJsLocaleMessage("common","common_js_function_11"));
			$("#pages").select();
			return;
		}
			var re = /^[1-9]\d*$/;
		  if (!re.test(pageNo))
            {
            /*必须为正整数!*/
            alert(getJsLocaleMessage("common","common_js_function_12"));
            $("#pages").select();
         return ;
     }
		if(pageNo>$("#totalpages").html()){
		 pageNo=$("#totalpages").html();
		}
		if(pageNo<=1){
		pageNo=1;
		} 	
    	
	       	var netID=$("#netid").val();
	    	var netname=$("#nname").val();
		window.open('wx_count.htm?method=find&&netid='+netID+'&&nname='+netname +'&&pageNo='+pageNo+'&&pagesize='+pagesize+'',"_self");
	}
	
	
	    function netReplyPages(pageNo,pagesize)
	{   
			if(isNaN(pageNo))
		{
			/*请输入数字*/
			alert(getJsLocaleMessage("common","common_js_function_11"));
			$("#pages").select();
			return;
		}
			var re = /^[1-9]\d*$/;
		  if (!re.test(pageNo))
            {
            /*必须为正整数!*/
            alert(getJsLocaleMessage("common","common_js_function_12"));
            $("#pages").select();
         return ;
     }
		if(pageNo>$("#totalpages").html()){
		 pageNo=$("#totalpages").html();
		}
		if(pageNo<=1){
		pageNo=1;
		} 	
    	    var opName=$("#chUser").val();
	       	var busid=$("#busid").val();
	    	var busname=$("#busname").val();

	    		if (opName.indexOf(" ")>-1) 
			{  
			opName=opName.replace(/(^\s*)|(\s*$)/g,"");
			
			} 
	    	if (busid.indexOf(" ")>-1) 
			{  
			busid=busid.replace(/(^\s*)|(\s*$)/g,"");
			
			} 
			   if(isNaN(busid)){
                   /*请输入正确的编号！*/
                   alert(getJsLocaleMessage("common","common_js_function_14"));
                     return;
                   }
				
			
		if (busname.indexOf(" ")>-1) 
			{  
			busname=busname.replace(/(^\s*)|(\s*$)/g,"");

			} 
			
		window.open('wx_count.htm?method=findReplyReport&&busid='+busid+'&&opname='+opName +'&&busname='+busname +'&&pageNo='+pageNo+'&&pagesize='+pagesize+'',"_self");
	}
	
	    //网讯发送统计报表模糊查询分页    
     function netSendByTimePages(netid, pageNo,pagesize)
	{   
	
			if(isNaN(pageNo))
		{
			/*请输入数字*/
			alert(getJsLocaleMessage("common","common_js_function_11"));
			$("#pages").select();
			return;
		}
			var re = /^[1-9]\d*$/;
		  if (!re.test(pageNo))
            {
            /*必须为正整数!*/
            alert(getJsLocaleMessage("common","common_js_function_12"));
            $("#pages").select();
         return ;
     }
		if(pageNo>$("#totalpages").html()){
		 pageNo=$("#totalpages").html();
		}
		if(pageNo<=1){
		pageNo=1;
		} 	
    	
		window.open('wx_count.htm?method=getSendCount&&netId='+netid+'&&pageNo='+pageNo+'&&pagesize='+pagesize+'',"_self");
	}
     
     //系统日志分页
      function SysLogPages(pageNo,pagesize)
	{   
		if(isNaN(pageNo))
		{
			/*请输入数字*/
			alert(getJsLocaleMessage("common","common_js_function_11"));
			$("#pages").select();
			return;
		}
			var re = /^[1-9]\d*$/;
		  if (!re.test(pageNo))
            {
            /*必须为正整数!*/
            alert(getJsLocaleMessage("common","common_js_function_12"));
            $("#pages").select();
         return ;
     }
		if(pageNo>$("#totalpages").html()){
		 pageNo=$("#totalpages").html();
		}
		if(pageNo<=1){
		pageNo=1;
		} 	

		var uname=$("#opname").val();
		var opType=$("#OPType").val();
		window.open('sysuser.htm?method=findSYSLog&&OPType='+opType +'&&opname='+uname+'&&pageNo='+pageNo+'&&pagesize='+pagesize+'',"_self");
	}
     
      //网讯充值报表模糊查询分页    
     function Pages(pageNo,pagesize)
	{   
		if(isNaN(pageNo))
		{
			/*请输入数字*/
			alert(getJsLocaleMessage("common","common_js_function_11"));
			$("#pages").select();
			return;
		}
		
			var re = /^[1-9]\d*$/;
		  if (!re.test(pageNo))
            {
            /*必须为正整数!*/
            alert(getJsLocaleMessage("common","common_js_function_12"));
            $("#pages").select();
         return ;
     }
		
		if(pageNo>$("#totalpages").html()){
		 pageNo=$("#totalpages").html();
		}
		if(pageNo<=1){
		pageNo=1;
		} 	
		var zzType=$("#zzType").val();
	    var uname=$("#username").val();

		//  window.location.href="<%=path%>/sysuser.htm?method=netZZReport&zzType="+zzType +"&userName="+uname;
       
		window.open('sysuser.htm?method=netZZReport&zzType='+zzType +'&userName='+uname +'&&pageNo='+pageNo+'&&pagesize='+pagesize+'',"_self");
	}
     
     //网讯管理模糊查询分页
  
	/*预览网讯信息*/
	function showDlgDiv()
    {
        //禁用select
        hideOrShowSelect();
        //改变样式
        document.getElementById('divBox').className='showNetbox';
        $("#divBox").show();
        //调整位置至居中
      //  adjustLocation();
        ShowLocation();
         
       //   Location();
    }
    
    function cancel()
    {
        $("#nm_preview_common").attr("src","");
        $("#overlay1").toggle();
        document.getElementById('divBox').className='hideDlg';
        hideOrShowSelect(false);
    }
    
    function hideOrShowSelect(v)
    {
        var allselect = document.getElementsByTagName("select");
        for (var i=0; i<allselect.length; i++)
        {
            allselect[i].disabled =(v==true)?"disabled":"";
        }
    }
    
    //20111215调整居中
     function ShowLocation()
    {

        var obox=document.getElementById('divBox');
        var imgs = document.getElementById('img');
         if(isFirefox=navigator.userAgent.indexOf("Firefox")>0){
			if (obox !=null && obox.style.display !="none")
	        {
	            var w=239;
	            var h=450;
	            var oLeft,oTop;
	         
            if (window.innerWidth)
            {
                oLeft=window.pageXOffset+(window.innerWidth-w)/2.5 +"px";
                oTop=window.pageYOffset+(window.innerHeight-h)/2.5 +"px";
            }
            else
            {
                var dde=document.documentElement;
                oLeft=dde.scrollLeft+(dde.offsetWidth-w)/2.5 +"px";
                oTop=dde.scrollTop+(dde.offsetHeight-h)/2.5 +"px";
            }
            
              //  imgs.style.left="0px";
	         //   imgs.style.top="0px"; 
	            obox.style.left=oLeft;
	            obox.style.top=oTop;
	        }
	        
		}else{
	        if (obox !=null && obox.style.display !="none")
	        {
	 
	            var w=239;
	            var h=450;
	            var oLeft,oTop;
	      
	            if (window.innerWidth)
	            {
	                oLeft=window.pageXOffset+(window.innerWidth-w)/2.5 +"px";
	                oTop=window.pageYOffset+(window.innerHeight-h)/2.5 +"px";
	                
	        
	            }
	            else
	            {
	       
	                var dde=document.documentElement;
	                oLeft=dde.scrollLeft+(dde.offsetWidth-w)/2.5 +"px";
	                oTop=dde.scrollTop+(dde.offsetHeight-h)/2.5 +"px";
	                
	                
	           
	            }
	            
	        
	            obox.style.left=oLeft;
	            obox.style.top=oTop;
	            
	         
	        }
	    }
    }
    
    
    function showNetlocation()
    {
        var obox=document.getElementById('divBox');
        if (obox !=null && obox.style.display !="none")
        {
            var w=239;
            var h=450;
            var oLeft,oTop;
            
            if (window.innerWidth)
            {
                oLeft=window.pageXOffset+(window.innerWidth-w)/4 +"px";
                oTop=window.pageYOffset+(window.innerHeight-h)/4 +"px";
            }
            else
            {
                var dde=document.documentElement;
                oLeft=dde.scrollLeft+(dde.offsetWidth-w)/4 +"px";
                oTop=dde.scrollTop+(dde.offsetHeight-h)/4 +"px";
            }
            
            obox.style.left="400px";
            obox.style.top="400px";
        }
    }
     var  listpage=null;
     
     
    
   

	  //查询条件
   var rowchange=function(){
   //行之间样式的变化
                $('#content tbody tr').hover(function() {
					$(this).css('background-color', '#c1ebff');
				}, function() {
					$(this).css('background-color', '#FFFFFF');
				});
				//条件查询的展开与收缩
				$("#lookupIcon").click(function(){
		      $("#condition").toggle("slow");  
		});
    }
    
    function fclick(obj){
	with(obj){
	style.posTop=event.y-offsetHeight/2
	style.posLeft=event.x-offsetWidth/2
	}
}


/*文件上传*/
function uploadCheck(){
	var filepath = document.getElementById("fileField").value;
	if(filepath == ''){
        /*请选择上传文件*/
        alert(getJsLocaleMessage("common","common_js_function_16"));
		return false;
	}
}
/*附件管理模糊查询分页*/
	function FirstPage(pageNo)
	{
		if(isNaN(pageNo))
		{
			/*请输入数字*/
			alert(getJsLocaleMessage("common","common_js_function_11"));
			$("#pages").select();
			return;
		}
			var re = /^[1-9]\d*$/;
		  if (!re.test(pageNo))
            {
            /*必须为正整数!*/
            alert(getJsLocaleMessage("common","common_js_function_12"));
            $("#pages").select();
         return ;
     }
		if(pageNo>$("#totalpages").html()){
		 pageNo=$("#totalpages").html();
		}
		if(pageNo<=1){
		pageNo=1;
		} 	

		 //var fileId=$("#fileid").val();
	  	  var fileName=$("#filename").val();
	  	  //var fileBanb=$("#filebanb").val();
	  	  var scdate=$("#scdate").val();
	  	  var enddate=$("#enddate").val();
	  	  //var status=$("#status").val();
	  	 var number=$("#pagesize").val();
	  	 var type =$("#type").val();
	  	 var query = $("#querySucai").val();
	  	 var returnFication=$("#fication1").val();
	  	
	  	 if(type=="undefined"||type=="100"){
	  	  			var time=new Date();
	  	  			if(query=='OK'){
						$('#bookInfo').load('wx_attached.htm?method=LookUp',{
						fileName:fileName,
						scdate:scdate,
						enddate:enddate,
						pagesize:number,
						querySucai:query,
						type:type,
						returnFication:returnFication,
						pageNo:pageNo
				});		
	  	 			}
	  	 			else {
	  	 				$('#bookInfo').load('wx_attached.htm?method=LookUp&&pageNo='+pageNo+'&&pagesize='+number+'&&returnFication='+returnFication,{time:time});
	  	 			}
	  	 	// window.open('wx_attached.htm?method=LookUp&&fileId='+fileId+'&&fileName='+fileName+'&&fileBanb='+fileBanb+'&&scdate='+scdate+'&&enddate='+enddate+'&&status='+status+'&&pageNo='+pageNo+'&&pagesize='+number+'',"_self");
	}
		else{
			var time=new Date();
			if(query=='OK'){
			$('#bookInfo').load('wx_attached.htm?method=LookUp',{
						fileName:fileName,
						scdate:scdate,
						enddate:enddate,
						pagesize:number,
						querySucai:query,
						type:type,
						returnFication:returnFication,
						pageNo:pageNo
					});	
	  	 			}
			else {
				$('#bookInfo').load('wx_attached.htm?method=LookUp',{
						pagesize:number,
						querySucai:query,
						type:type,
						returnFication:returnFication,
						pageNo:pageNo
					});	
			}
		// window.open('wx_attached.htm?method=LookUp&&type='+type+'&&fileId='+fileId+'&&fileName='+fileName+'&&fileBanb='+fileBanb+'&&scdate='+scdate+'&&enddate='+enddate+'&&status='+status+'&&pageNo='+pageNo+'&&pagesize='+number+'',"_self");
	}
	}
	
	/*重命名文件名称，版本，描述*/
	var update=function()
	{
	
	}
	/*下拉框每页显示条数*/
	
	/*分页跳转*/
	function swipe(type)
	{
		 var fileName=$("#filename").val();
	  	  var scdate=$("#scdate").val();
	  	  var enddate=$("#enddate").val();
		var number=$("#pagesize").val();
		var pageNo=$("#pages").val();
		var tt=$("#type").val();
		var returnFication =$("#fication1 ").val();
		if(isNaN(pageNo))
		{
			/*请输入数字*/
			alert(getJsLocaleMessage("common","common_js_function_11"));
			$("#pages").select();
			return;
		}
			var re = /^[1-9]\d*$/;
		  if (!re.test(pageNo))
            {
            /*必须为正整数!*/
            alert(getJsLocaleMessage("common","common_js_function_12"));
            $("#pages").select();
         return ;
     }
		
	 var query = $("#querySucai").val();
		if(pageNo>parseInt($("#totalpages").html())){
		   pageNo=$("#totalpages").html();
		}
		if(pageNo<=1){
		pageNo=1;
		} 
			//网讯管理
			if(type==0)
			{
				window.open("wx_manger.htm?method=find&&pageno="+pageNo+"&&pagesize="+number+"","_self");
			}
			//附件管理
			if(type==1)
			{
				var time=new Date();
				if(tt=="undefined"){
				if(query=="OK"){
					$('#bookInfo').load('wx_attached.htm?method=LookUp',{
							fileName:fileName,
							scdate:scdate,
							enddate:enddate,
							pagesize:number,
							pageno:pageNo,
							returnFication:returnFication,
							querySucai:query
						});
				
				}
				else {
					$('#bookInfo').load('wx_attached.htm?method=LookUp&&pageno='+pageNo+'&&pagesize='+number+'&&returnFication='+returnFication,{time:time});
					}
				}
				else
					if(query=="OK"){
					$('#bookInfo').load('wx_attached.htm?method=LookUp',{
							fileName:fileName,
							scdate:scdate,
							enddate:enddate,
							pagesize:number,
							pageno:pageNo,
							querySucai:query,
							returnFication:returnFication,
							type:tt
						});
				
					}
					else
						$('#bookInfo').load('wx_attached.htm?method=LookUp&&type='+tt+'&&pageno='+pageNo+'&&pagesize='+number+'&&returnFication='+returnFication,{time:time});
			}
	}

	/*网讯统计开始*/
	function seeVisit(obj){
                window.open("wx_count.htm?method=getVisitBytime&pageno=1&time="+obj+"","_self");
       }
       
        function seeNetByTime(obj){
                window.open("wx_count.htm?method=seeNetByTime&pageno=1&time="+obj+"","_self");
       }
       
         function seeVisitByphone(obj){
                window.open("wx_count.htm?method=getVisitBytimeWithPhone&pageno=1&time="+obj+"","_self");
       }
    
         /*ajax方法    返回数据    */
            var adddata=function(pageno,pagesize,wxid,wxname){
            $.post("wx_count.htm?method=findAjax",{pageno:pageno,pagesize:pagesize,wxid:wxid,wxname:wxname},function(data){
                             var obj=eval("("+data+")");
                             var list=obj.list;
                            var tbody=document.getElementById("tbody1");
                            while(tbody.firstChild)
                               tbody.removeChild(tbody.firstChild);
                              $("#pageno").html(obj.pageno);
                             $("#totalpages").html(obj.totalpages);
                             $("#totalsize").html(obj.totalsize);
                              for(var i=0;i<list.length;i++){
                                    var tr=document.createElement("tr");
				                          var td1=document.createElement("td");
				                          td1.innerHTML=list[i].netid;
				                          tr.appendChild(td1);
				                          
				                           var td2=document.createElement("td");
				                          td2.innerHTML=list[i].name;
				                          tr.appendChild(td2);
				                          var td4=document.createElement("td");
				                          td4.innerHTML=list[i].sendcount;
				                          tr.appendChild(td4);
				                           var td5=document.createElement("td");
				                          td5.innerHTML=list[i].successcount;
				                          tr.appendChild(td5);
				                          
				                          var td6=document.createElement("td");
				                          td6.innerHTML=list[i].falsecount;
				                          tr.appendChild(td6);
				                           var td7=document.createElement("td");
				                            if(list[i].visitpeople==0){
				                            td7.innerHTML=list[i].visitpeople;
				                           }
				                           else{
				                               td7.innerHTML="<a href='#' onclick='seeVisitByphone("+list[i].netid+")'>"+list[i].visitpeople+"</a>";
				                           }
				                      
				                          tr.appendChild(td7);
				                           var td8=document.createElement("td");
				                           if(list[i].visitcount==0){
				                            td8.innerHTML=list[i].visitcount;
				                           }
				                           else{
				                               td8.innerHTML="<a href='#' onclick='seeVisit("+list[i].netid+")'>"+list[i].visitcount+"</a>";
				                           }
				                         tr.appendChild(td8);
				                          tbody.appendChild(tr);
                             }  
                      });
          }
          
          var page=function()
          {
            /* 下一页*/
             $("#next").click(function(){
              var pagesize=$("#pagesize").val();
                   var pageno=Number($("#pageno").html())+1;
                   var totalpages=$("#totalpages").html();
                   if(pageno>totalpages){
                        return;
                   }
                   else{
              adddata(pageno,pagesize,$("#nid2").val(),$("#nname").val());
                   }      
          });
          
          /* 上一页*/
          $("#pre").click(function(){
           var pagesize=$("#pagesize").val();
                   var pageno=Number($("#pageno").html())-1;
                   var totalpages=$("#totalpages").html();
                   if(pageno<1){
                        return;
                   }
                   else{
                  adddata(pageno,pagesize,$("#nid2").val(),$("#nname").val());
                  }      
          });
          
          /* 第一页*/
           $("#one").click(function(){
             var pagesize=$("#pagesize").val();
                 adddata(1,pagesize,$("#nid2").val(),$("#nname").val());
                       
          });
          
          /* 最后一页*/
           $("#last").click(function(){
                   var pagesize=$("#pagesize").val();
                    adddata($("#totalpages").html(),pagesize,$("#nid2").val(),$("#nname").val());
          });
          
           /* 最后一页*/
           $("#pagesize").click(function(){
                   var pagesize=$(this).val();
                    adddata($("#pageno").html(),pagesize,$("#nid2").val(),$("#nname").val());
          });
          
                /* 转到第n页*/
           $("#go2").click(function(){
                  var pagesize=$("#pagesize").val();
                  var pageNo=$("#pages").val();
                   if(isNaN(pageNo)){
                       /*请输入数字*/
			alert(getJsLocaleMessage("common","common_js_function_11"));
                      $("#pages").select();
                       return;
                   }
               	var re = /^[1-9]\d*$/;
		  if (!re.test(pageNo))
            {
            /*必须为正整数!*/
            alert(getJsLocaleMessage("common","common_js_function_12"));
            $("#pages").select();
         return ;
     }
                
                	if(pageNo>$("#totalpages").html()){
		              pageNo=$("#totalpages").html();
	                        	}
	                   	if(pageNo<=1){
	                    	pageNo=1;
	                        	} 
                        adddata(pageNo,pagesize,$("#nid2").val(),$("#nname").val());
          });
          
          /* 条件模糊查询*/
          $("#finds").click(function(){
           var pageno=1;
           var pagesize=$("#pagesize").val();
        var nr1=$("#nid2").val();     
          if (nr1.indexOf(" ")>-1) 
			{  
			nr1=nr1.replace(/(^\s*)|(\s*$)/g,"");
			}   
		flg=0;  
		str="";  
		spc=""  
		arw="";  
		for (var i=0;i<nr1.length;i++)
		{  
			cmp="0123456789"  
			tst=nr1.substring(i,i+1)  
			if (cmp.indexOf(tst)<0)
			{  
				flg++;  
				str+=" "+tst;  
				spc+=tst;  
				arw+="^";  
			} else{arw+="_";}  
		}  
		if (flg!=0)
		{  
			if (spc.indexOf(" ")>-1) 
			{
                /*和空格*/
                str+=getJsLocaleMessage("common","common_js_function_17");
            }
            /*网讯编号只能为数字*/
            alert(getJsLocaleMessage("common","common_js_function_4"));
			$("#nid2").selected();
			return;  
		} 
	             var wxname=$("#nname").val();
	             	wxname=wxname.replace(/(^\s*)|(\s*$)/g,"");
	             	if(wxname=="" && nr1==""){
                        /*请输入查询条件*/
                        alert(getJsLocaleMessage("common","common_js_function_18"));
	             	  return;
	             	}
	             	else{
	             	    	adddata(pageno,pagesize,nr1,wxname); 
	             	}
          });
          }
	/*网讯统计结束*/
	
/*网讯审核开始*/
function AllCheck()
{
	var s=document.getElementsByName("checklist");
	var s2="";
	for(var i=0;i<s.length;i++)//循环遍历出所选数据的id
	{
		if(s[i].checked)
		{
			s2+=s[i].value+',';
		}
	}
	s2=s2.substr(0,s2.length-1);
	if(s2!="")
	{
        /*确定审核所选记录？*/
        if(confirm(getJsLocaleMessage("common","common_js_function_19")))
		{
			window.open('wx_Check.htm?method=UpdateStatus&&idList='+s2+'',"_self");
		}
	}
	else
	{
        /*请选择要审核的记录！*/
        alert(getJsLocaleMessage("common","common_js_function_20"));
	}
}

  /*显示用户信息*/
	
	function LookInfo(userid)
	{
		 $("#pres").toggle(); 
		 $("#overlay1").toggle();
		 $.post("wx_Check.htm?method=LookUserInfo",{userid:userid},function(data){
 		         data=eval("("+data+")");
 		         if(data.name=="null")
 		         {
 		         	$("#uname").val("");
 		         }
 		         else
 		         {
 		         	$("#uname").val(data.name);
 		         }
 		         if(data.sex==1)
 		         {
                     /*男*/
                     $("#sex").val(getJsLocaleMessage("common","common_man"));
 		         }
 		         else
 		         {
                     /*女*/
                     $("#sex").val(getJsLocaleMessage("common","common_woman"));
 		         }
 		          if(data.birthday=="null")
 		         {
 		         	$("#brith").val(" ");
 		         }
 		         else
 		         {
 		         	 $("#brith").val(data.birthday);
 		         }
 		         if(data.mobile=="null")
 		         {
 		         	$("#mobil").val(" ");
 		         }else{
 		         	$("#mobil").val(data.mobile);
 		         }
 		         if(data.oph=="null")
 		         {
 		         	$("#phone").val(" ");
 		         }else{
 		         	$("#phone").val(data.oph);
 		         }
 		         if(data.qq=="null")
 		         {
 		         	$("#qq").val(" ");
 		         }else{
 		         	$("#qq").val(data.qq);
 		         }
 		         
 		          if(data.email=="null")
 		         {
 		         	$("#e_mail").val(" ");
 		         }
 		         else
 		         {
 		         	$("#e_mail").val(data.email);
 		         }
 		          if(data.msn=="null")
 		         {
 		         	$("#msn").val(" ");
 		         }
 		         else
 		         {
 		       		 $("#msn").val(data.msn); 		       
 		         }
 		})
		tuodong();
	}
	
	/*预览审核网讯信息*/
	function showDlgDivs()
    {
        //禁用select
        hideOrShowSelects();
        //改变样式
        document.getElementById('divBoxCheck').className='showNetboxCheck';

        //调整位置至居中
         ShowLocations();
    }
    
    function cancels()
    {
        document.getElementById('divBox').className='hideDlg';
        hideOrShowSelect(false);
    }
    
    function hideOrShowSelects(v)
    {
        var allselect = document.getElementsByTagName("select");
        for (var i=0; i<allselect.length; i++)
        {
            allselect[i].disabled =(v==true)?"disabled":"";
        }
    }
    
    //20111215调整居中
     function ShowLocations()
    {
        var obox=document.getElementById('divBoxCheck');
        var imgs = document.getElementById('img');
         if(isFirefox=navigator.userAgent.indexOf("Firefox")>0){
			if (obox !=null && obox.style.display !="none")
	        {
	            var w=239;
	            var h=450;
	            var oLeft,oTop;
	            if (window.innerWidth)
	            {
	                oLeft=window.pageXOffset+(window.innerWidth-w)/2 +"px";
	                oTop=window.pageYOffset+(window.innerHeight-h)/2 +"px";
	            }
	            else
	            {
	                var dde=document.documentElement;
	                oLeft=dde.scrollLeft+(dde.offsetWidth-w)/2 +"px";
	                oTop=dde.scrollTop+(dde.offsetHeight-h)/2 +"px";
	            }
	            obox.style.left=oLeft;
	            obox.style.top=oTop;
	        }
	        
		}else{
	        if (obox !=null && obox.style.display !="none")
	        {
	            var w=239;
	            var h=450;
	            var oLeft,oTop;
	            if (window.innerWidth)
	            {
	                oLeft=window.pageXOffset+(window.innerWidth-w)/3 +"px";
	                oTop=window.pageYOffset+(window.innerHeight-h)/3 +"px";
	            }
	            else
	            {
	                var dde=document.documentElement;
	                oLeft=dde.scrollLeft+(dde.offsetWidth-w)/3 +"px";
	                oTop=dde.scrollTop+(dde.offsetHeight-h)/3 +"px";
	            }
	            
	            imgs.style.left="0.8px";
	            imgs.style.top="3px"; 
	            obox.style.left=oLeft;
	            obox.style.top=oTop;
	        }
	    }
    }
    
    
    function showNetlocations()
    {
        var obox=document.getElementById('divBoxCheck');
        if (obox !=null && obox.style.display !="none")
        {
            var w=239;
            var h=450;
            var oLeft,oTop;
            if (window.innerWidth)
            {
                oLeft=window.pageXOffset+(window.innerWidth-w)/4 +"px";
                oTop=window.pageYOffset+(window.innerHeight-h)/4 +"px";
            }
            else
            {
                var dde=document.documentElement;
                oLeft=dde.scrollLeft+(dde.offsetWidth-w)/4 +"px";
                oTop=dde.scrollTop+(dde.offsetHeight-h)/4 +"px";
            }
            
            obox.style.left="400px";
            obox.style.top="400px";
        }
    }
    
    //查看
	function LookAndCheck(netId)
	{	
	var  listpage=null;
	$.post('wx_manger.htm?method=showNetById',{netId:netId},function(data){
	       data=eval("("+data+")");
	       listpage=data;
	      $("#ym").children().remove();
	     for(var i=0;i<data.length;i++)
	     {
	         var op=$("<option>").val(data[i].id).html(data[i].name).appendTo($("#ym"));
	         $("#netInfoDiv").html(data[0].content);
	      }
	      showDlgDivs();
     });
     
     $("#ym").click(function(){
            var id=$(this).val();
            for(var i=0;i<listpage.length;i++){
                  if(id==listpage[i].id){
                      $("#netInfoDiv").html(listpage[i].content);
                  }
            }
     });
}
/*校验手机号码*/
function isPhone(phone){

	var regex = /^(13|14|15|18)\d{9}$/;
	if(regex.test(phone)){
		return true;
	}else{
		return false;
	}
	/*只是显示数字*/
	function numberControl(va)
	{
		var pat=/[`~!@#$%^&*()_+<>?:"{},.\/;'[\]]/im;
		if(pat.test(va.val()))
		{
			va.val(va.val().replace(/[^\d]/g,''));
		}
	}
	
}

//显示列表状态下，模板名字详细信息
function modify(t,i)
{
	$('#modify').dialog({
		autoOpen: false,
		resizable: false,
		width:250,
	    height:200
	});
	$("#msg").children("xmp").empty();
	$("#msg").children("xmp").text($(t).children("label").children("xmp").text());
	if(i==1)
	{
		/*网讯名称*/
		$('#modify').dialog('option','title',getJsLocaleMessage("common","ydwx_wxgl_wxshh_mingchens"));
	}
	else
	{
		/*网讯内容*/
		$('#modify').dialog('option','title',getJsLocaleMessage("common","ydwx_wxfs_jtwxfs_wxnr"));
	}
	$('#modify').dialog('open');
}