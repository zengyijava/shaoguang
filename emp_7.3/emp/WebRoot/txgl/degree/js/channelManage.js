var ID="";
var DEGREE="";
// 增加档位
function addType(id) {
	if (confirm(getJsLocaleMessage("rms","rms_dwjf_confirmadddwxx"))) {
		var addDegree = $("#addDegree").val();
		var addDegreeBegin = $("#addDegreeBegin").val();
		var addDegreeEnd = $("#addDegreeEnd").val();
		var addValidDateBegin = $("#addValidDateBegin").val();
		var addValidDateEnd = $("#addValidDateEnd").val();		
		//正整数
		var regex = /^\+?[1-9]\d*$/;
		//正整数或0
		var regex2 = /0|(^[1-9]+\d*$)/;
		
		if (addDegree == "" ) {
			alert(getJsLocaleMessage("rms","rms_dwjf_jfdwnoempty"));
			return;
		} else if(addDegreeBegin == "") {
			alert(getJsLocaleMessage("rms","rms_dwjf_initialcapnoempty"));
			return;
		} else if(addDegreeEnd == "") {
			alert(getJsLocaleMessage("rms","rms_dwjf_endcaprangenoept"));
			return;
		} else if(addValidDateBegin == "") {
			alert(getJsLocaleMessage("rms","rms_dwjf_starttimenoept"));
			return;
		} else if(addValidDateEnd == "") {
			alert(getJsLocaleMessage("rms","rms_dwjf_endtimenoept"));
			return;		
		} else if(!regex.test(addDegree)) {
			alert(getJsLocaleMessage("rms","rms_dwjf_jfdwbeinteger"));
			return;		
		} else if(!regex2.test(addDegreeBegin)) {
			alert(getJsLocaleMessage("rms","rms_dwjf_startcapbeinteger"));
			return;		
		} else if(!regex.test(addDegreeEnd)) {
			alert(getJsLocaleMessage("rms","rms_dwjf_endcapbeinteger"));
			return;		
		} else {
			$.post("tx_degreeManager.htm", {
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
				} else if(result == "TimeError"){
					alert(getJsLocaleMessage("rms","rms_dwjf_endtimebigstart"));
				} else if(result == "EndTimeError"){
					alert(getJsLocaleMessage("rms","rms_dwjf_endtimebigcurrent"));
				} else if (resultlow[0] == "lowdegree") {
					alert(getJsLocaleMessage("rms","rms_dwjf_rlfwbhl"));
				} else if(resultlow[0] == "highdegree"){
					alert(getJsLocaleMessage("rms","rms_dwjf_rlfwbhl"));
				} else if (result == "true") {
					alert(getJsLocaleMessage("rms","rms_dwjf_successcreate"));
					$("#addDiv").dialog("close");
					$("#search").click();
				} else {
					alert(getJsLocaleMessage("rms","rms_dwjf_failedcreate"));
					$('#addsubBut').attr("disabled", false);
					$('#addcancelwid').attr("disabled", false);
					return;
				}
			});
		}
	}
}
// 取消关闭
function doCancel() {
	$("#addDiv").val("");
	$("#addDiv").dialog("close");

	$("#editDiv").val("");
	$("#editDiv").dialog("close");
}

//修改档位
function update(id,degree,degreeBegin,degreeEnd,validDateBegin,validDateEnd,status) {
	ID = id;
	DEGREE = degree;
	var DEGREE_BEGIN = degreeBegin;
	var DEGREE_END = degreeEnd;
	
	var VALID_DATE_BEGIN = validDateBegin.toString().substring(0,validDateBegin.toString().length-2);
	var VALID_DATE_END = validDateEnd.toString().substring(0,validDateBegin.toString().length-2);
	var STATUS = status;
	$("#editDegree").val(DEGREE);
	$("#editDegreeBegin").val(DEGREE_BEGIN);
	$("#editDegreeEnd").val(DEGREE_END);
	$("#editValidDateBegin").val(VALID_DATE_BEGIN);
	$("#editValidDateEnd").val(VALID_DATE_END);
	$("#editStatus option[value='" + STATUS + "'] ").attr("selected", true);
	$("#editDegree").attr("disabled",true);
	$("#editDiv").dialog("open");
	
}
//修改档位
function editDegreeManager() {
	//正整数
	var regex = /^\+?[1-9]\d*$/;
	//正整数或0
	var regex2 = /0|(^[1-9]+\d*$)/;
	if (confirm(getJsLocaleMessage("rms","rms_dwjf_qdmodifyjfdw"))) {
		$("#editDiv").dialog("open");
		var degreeBegin = $("#editDegreeBegin").val();
		var degreeEnd = $("#editDegreeEnd").val();
		var validDateBegin = $("#editValidDateBegin").val();
		var validDateEnd = $("#editValidDateEnd").val();
		var status = $("#editStatus").val();
		if(degreeBegin == "") {
			alert(getJsLocaleMessage("rms","rms_dwjf_initialcapnoempty"));
			return;
		} else if(degreeEnd == "") {
			alert(getJsLocaleMessage("rms","rms_dwjf_endcaprangenoept"));
			return;
		} else if(validDateBegin == "") {
			alert(getJsLocaleMessage("rms","rms_dwjf_starttimenoept"));
			return;
		} else if(validDateEnd == "") {
			alert(getJsLocaleMessage("rms","rms_dwjf_endtimenoept"));
			return;		
		} else if(status == "") {
			alert(getJsLocaleMessage("rms","rms_report_jfdwztbnwk"));
			return;
		} else if(!regex2.test(degreeBegin)) {
			alert(getJsLocaleMessage("rms","rms_dwjf_startcapbeinteger"));
			return;
		} else if(!regex.test(degreeEnd)) {
			alert(getJsLocaleMessage("rms","rms_dwjf_endcapbeinteger"));
			return;
		} else {
			$.post("tx_degreeManager.htm", {
				method : "updateDegree",
				id : ID,
				degree : DEGREE,
				degreeBegin : degreeBegin,
				degreeEnd : degreeEnd,
				validDateBegin : validDateBegin,
				validDateEnd : validDateEnd,
				status : status,
				
			}, function(result) {
				//获取重叠上一档位
				var resultlow = result.split(":");
				
				if (result == "DegreeExists") {
					alert(getJsLocaleMessage("rms","rms_dwjf_jfdwycz"));
				} else if(result == "widthError"){
					alert(getJsLocaleMessage("rms","rms_dwjf_endcapabigstart"));
				} else if(result == "TimeError"){
					alert(getJsLocaleMessage("rms","rms_dwjf_endtimebigstart"));
				} else if(result == "EndTimeError"){
					alert(getJsLocaleMessage("rms","rms_dwjf_endtimebigcurrent"));
				} else if (resultlow[0] == "lowdegree") {
					alert(getJsLocaleMessage("rms","rms_dwjf_rlfwbhl"));
				} else if(resultlow[0] == "highdegree"){
					alert(getJsLocaleMessage("rms","rms_dwjf_rlfwbhl"));
				} else if (result == "true") {
					alert(getJsLocaleMessage("rms","rms_dwjf_successmodify"));
					$("#editDiv").dialog("close");
					$("#search").click();
				} else {
					alert(getJsLocaleMessage("rms","rms_dwjf_failedmodify"));
					$('#addsubBut').attr("disabled", false);
					$('#addcancelwid').attr("disabled", false);
					return;
				}
			});
		}
	}
}