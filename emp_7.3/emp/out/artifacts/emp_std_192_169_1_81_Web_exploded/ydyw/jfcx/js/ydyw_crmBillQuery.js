		//隐藏机构树形控件
		function showMenu() {
			hideMenu();
			var sortSel = $("#depNam");
			var sortOffset = $("#depNam").offset();
			$("#dropMenu").toggle();
		}
		
		//隐藏机构树形控件
		function hideMenu() {
			$("#dropMenu").hide();
		}
		
		//选中的机构显示文本框
		function zTreeOnClick3(event, treeId, treeNode) {
			if (treeNode) {
				$("#depNam").attr("value", treeNode.name); //设置机构属性
				$("#deptString").attr("value", treeNode.id); //设置机构代码	
				
			}
			
		}		
		
		//选中的机构显示文本框
		function zTreeOnClickOK3() {
				hideMenu();
		}

   		
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

		function cleanSelect_dep()
		{

			$('#depNam').attr('value', '');
			$('#depNam').attr('value', getJsLocaleMessage("common","common_pleaseSelect"));
			$('#deptString').attr('value', '');
		}	  