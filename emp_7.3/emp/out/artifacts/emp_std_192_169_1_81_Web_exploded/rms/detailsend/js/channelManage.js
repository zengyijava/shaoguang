
// 增加档位
function search() {
//		alert(1);
		var addDegree = $("#addDegree").val();
		var addDegreeBegin = $("#addDegreeBegin").val();
		var addDegreeEnd = $("#addDegreeEnd").val();
		var addValidDateBegin = $("#addValidDateBegin").val();
		var addValidDateEnd = $("#addValidDateEnd").val();

		var regex = /^\d+\.\d+$/;
		
		if (addDegree == "" ) {
			alert(getJsLocaleMessage("rms","rms_dwjf_jfdwnoempty"));
			return;
		}
		else if(addDegreeBegin == "") {
			alert(getJsLocaleMessage("rms","rms_dwjf_initialcapnoempty"));
			return;
		}
		else if(addDegreeEnd == "") {
			alert(getJsLocaleMessage("rms","rms_dwjf_endcaprangenoept"));
			return;
		}
		else if(addValidDateBegin == "") {
			alert(getJsLocaleMessage("rms","rms_dwjf_starttimenoept"));
			return;
		}
		else if(addValidDateEnd == "") {
			alert(getJsLocaleMessage("rms","rms_dwjf_endtimenoept"));
			return;		
		}
		else if(regex.test(addDegree)) {
			alert(getJsLocaleMessage("rms","rms_dwjf_jfdwbeinteger"));
			return;		
		}
		else if(regex.test(addDegreeBegin)) {
			alert(getJsLocaleMessage("rms","rms_dwjf_startcapbeinteger"));
			return;		
		}
		else if(regex.test(addDegreeEnd)) {
			alert(getJsLocaleMessage("rms","rms_dwjf_endcapbeinteger"));
			return;		
		}else if(addValidDateEnd<addValidDateBegin){
			alert(getJsLocaleMessage("rms","rms_dwjf_endtimebigstart"));
			return;
		}else {
			$.post("cha_degreelManage.htm", {
				method : "addDegree",
				addDegree : addDegree,
				addDegreeBegin : addDegreeBegin,
				addDegreeEnd : addDegreeEnd,
				addValidDateBegin : addValidDateBegin,
				addValidDateEnd : addValidDateEnd,
				
			}, function(result) {
				
				//获取重叠上一档位
				var resultlow = result.split(":");
				
				if (result == "DegreeExists") {
					alert(getJsLocaleMessage("rms","rms_dwjf_jfdwycz"));
				} else if(result == "widthError"){
					alert(getJsLocaleMessage("rms","rms_dwjf_endcapabigstart"));
				} else if (resultlow[0] == "lowdegree") {
					alert(getJsLocaleMessage("rms","rms_report_capacityrange")+resultlow[1]+getJsLocaleMessage("rms","rms_report_jfdwcdreinput"));
				} else if(resultlow[0] == "highdegree"){
					alert(getJsLocaleMessage("rms","rms_report_capacityrange")+resultlow[1]+getJsLocaleMessage("rms","rms_report_jfdwcdreinput"));
				} else if (result == "true") {
					alert(getJsLocaleMessage("rms","rms_dwjf_successcreate"));
					$("#addDiv").dialog("close");
				} else {
					alert(getJsLocaleMessage("rms","rms_dwjf_failedcreate"));
					$('#addsubBut').attr("disabled", false);
					$('#addcancelwid').attr("disabled", false);
					return;
				}
			});

		}
	//}
}
