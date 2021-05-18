		//开始时间
		function setime(){
		    var max = "2099-12-31";
		    var v = $("#recvtime").attr("value");
		    var min = "1900-01-01";
			WdatePicker({dateFmt:'yyyy-MM-dd',minDate:min,maxDate:v});

		}
		//发送起止时间控制
		function retime(){
		    var max = "2099-12-31";
		    var v = $("#sendtime").attr("value");
			
			WdatePicker({dateFmt:'yyyy-MM-dd',minDate:v,maxDate:max});

		}
		function numberControl(va)
		{
			var pat=/^\d*$/;
			if(!pat.test(va.val()))
			{
				va.val(va.val().replace(/[^\d]/g,''));
			}
		}