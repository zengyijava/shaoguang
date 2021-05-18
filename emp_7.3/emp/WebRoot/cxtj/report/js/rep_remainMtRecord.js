function modify(t)
		{
		$('#modify').dialog({
			autoOpen: false,
			width:250,
		    height:200
		});
		$("#msg").empty();
		$("#msg").html($(t).children("label").children("xmp").html());
		$('#modify').dialog('open');
		}

		function numberControl(va)
		{
			var pat=/^\d*$/;
			if(!pat.test(va.val()))
			{
				va.val(va.val().replace(/[^\d]/g,''));
			}
		}
		//checkbox全选
		function checkAlls(e,str)    
		{  
			var a = document.getElementsByName(str);    
			var n = a.length;    
			for (var i=0; i<n; i=i+1)    
				a[i].checked =e.checked;    
		}
		//发送起止时间控制
		function rtime(){
		    var max = "2099-12-31 23:59:59";
		    var v = $("#sendtime").attr("value");
			if(v.length != 0)
			{
			    var year = v.substring(0,4);
				var mon = v.substring(5,7);
				var day = 31;
				if (mon != "12")
				{
				    mon = String(parseInt(mon,10)+1);
				    if (mon.length == 1)
				    {
				        mon = "0"+mon;
				    }
				    switch(mon){
				    case "01":day = 31;break;
				    case "02":day = 28;break;
				    case "03":day = 31;break;
				    case "04":day = 30;break;
				    case "05":day = 31;break;
				    case "06":day = 30;break;
				    case "07":day = 31;break;
				    case "08":day = 31;break;
				    case "09":day = 30;break;
				    case "10":day = 31;break;
				    case "11":day = 30;break;
				    }
				}
				else
				{
				    year = String((parseInt(year,10)+1));
				    mon = "01";
				}
				max = year+"-"+mon+"-"+day+" 23:59:59"
			}
			WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:v,maxDate:max});

		};

		function stime(){
		    var max = "2099-12-31 23:59:59";
		    var v = $("#recvtime").attr("value");
		    var min = "1900-01-01 00:00:00";
			if(v.length != 0)
			{
			    max = v;
			    var year = v.substring(0,4);
				var mon = v.substring(5,7);
				if (mon != "01")
				{
				    mon = String(parseInt(mon,10)-1);
				    if (mon.length == 1)
				    {
				        mon = "0"+mon;
				    }
				}
				else
				{
				    year = String((parseInt(year,10)-1));
				    mon = "12";
				}
				min = year+"-"+mon+"-01 00:00:00"
			}
			WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:max});

		};
		function doSel(doType)
		{
			doSel(doType,null);
		}
		