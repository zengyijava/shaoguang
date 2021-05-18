
//新建审核流程管理
function addAuditPro() {
	var lguserid = $("#lguserid").val();
	var pathUrl = $("#pathUrl").val();
	window.location.href = pathUrl
			+ "/aud_auditpro.htm?method=toAddAuditPro&pathtype=1&lguserid="
			+ lguserid;
}

// 删除审核流程管理
function deleteAuditPro(flowid) {
	var lguserid = $("#lguserid").val();
	var pathUrl = $("#pathUrl").val();
	$.post(pathUrl + "/aud_auditpro.htm", {
		method : "operationAuditCon",
		operType : "delete",
		flowid : flowid
	}, function(msg) {
		if (msg.indexOf("html") > 0) {
			alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_1"));
			return;
		} else if (msg == "haveaudobj" || msg == "haverecord") {
			alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_2"));
			return;
		} else if (msg == "") {
			alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_3"));
			return;
		} else {
			if (confirm(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_4"))) {
				$.post(pathUrl + "/aud_auditpro.htm", {
					method : "deleteAudit",
					lguserid : lguserid,
					flowid : flowid
				}, function(returnmsg) {
					if (returnmsg.indexOf("html") > 0) {
						alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_1"));
						return;
					} else if (returnmsg == "success") {
						alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_5"));
						window.location.href = pathUrl
								+ "/aud_auditpro.htm?method=find&lguserid="
								+ lguserid+"&isOperateReturn=true";
						return;
					} else if (returnmsg == "fail") {
						alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_6"));
						return;
					} else {
						alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_7"));
						return;
					}
				});
			}
		}
	});
}

// 修改审核流程管理
function updateAuditPro(flowid) {
	var lguserid = $("#lguserid").val();
	var pathUrl = $("#pathUrl").val();
	var pathtype = "2";
	$
			.post(
					pathUrl + "/aud_auditpro.htm",
					{
						method : "operationAuditCon",
						operType : "update",
						flowid : flowid
					},
					function(msg) {
						if (msg.indexOf("html") > 0) {
							alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_1"));
							return;
						} else if (msg.indexOf("haverecord") != -1) {
                            var index = msg.indexOf("#");
                            if(index == -1){
                                alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_8"));
                                return;
                            }
                            $("#updateTip .content-tip").html(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_8"));
                            var infoTypes = msg.substring(index+1,msg.length-1);
                            var types = infoTypes.split("#").sort();
                            var infos = ['',getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_9"),getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_10"),getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_11"),getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_12"),getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_13"),getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_14")];
                            var eles = [];
                            for(var i = 0; i<types.length;i++){
                                var type = types[i];
                                eles.push('<a data-id="'+flowid+'" info-type="'+type+'">'+infos[type]+'</a>');
                            }
                            $("#updateTip .audit-list").html(eles.join(''));
                            var oheight = $("#updateTip").dialog('option','oheight');
                            if(types.length>3){
                                $("#updateTip").dialog({height:oheight+24});
                            }else{
                                $("#updateTip").dialog({height:oheight});
                            }
                            $("#updateTip").dialog("open");
							return;
						} else if (msg == "") {
							alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_7"));
							return;
						} else {
							window.location.href = pathUrl
									+ "/aud_auditpro.htm?method=toAddAuditPro&pathtype=2&lguserid="
									+ lguserid + "&flowid=" + flowid;
						}
					});
}
// 设置审核对象
function installAuditObj(title, flowid) {
	var lguserid = $("#lguserid").val();
	var lgcorpcode = $("#lgcorpcode").val();
	var iPath = $("#iPath").val();
	if(title == null ||  title == ""  || title == "null"){
		title = "-";
	}
	$("#auditObj").dialog("option", "title",getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_15") + title + " &nbsp; (" + flowid + ")");
	var url = iPath+"/aud_installSelObj.jsp?pathtype=3&lguserid="+ lguserid + "&lgcorpcode=" + lgcorpcode + "&flowid=" + flowid;
	$("#flowFrame").attr("src", url);
	// 将审核流程ID设置在这里
	$("#flowFrame").attr("attrid", flowid);
	$("#auditObj").dialog("open");
}
// 修改审核对象
function updateinstallObj() {
	var flowid = $("#flowFrame").attr("attrid");
	var optionSize = $(window.frames['flowFrame'].document).find(
			"#right option").size();
	// if(optionSize == 0){
	// alert("请设置审核对象！");
	// return;
	// }
	// 设置的机构IDS
	var depidstr = "";
    //机构包含状态
    var depcontainstr = "";
	// 设置的操作员IDS
	var useridstr = "";
	$(window.frames['flowFrame'].document).find("#right option").each(
			function() {
				var id = $(this).val();
				// 1是机构 2是操作员
				var type = $(this).attr("isdeporuser");
				if (type == "1") {
					useridstr = useridstr + id + ",";
				} else if (type == "2") {
					depidstr = depidstr + id + ",";
                    depcontainstr = depcontainstr + $(this).attr("iscontain")+',';
				}
			});
	var lguserid = $("#lguserid").val();
	var pathUrl = $("#pathUrl").val();
	var pathtype = "2";
	$("#updateinstallbtn").attr("disabled", "disabled");
	$.post(pathUrl + "/aud_auditpro.htm", {
		method : "installaudobj",
		depidstr : depidstr,
        depcontainstr : depcontainstr,
		useridstr : useridstr,
		lguserid : lguserid,
		flowid : flowid,
		pathtype : pathtype
	}, function(returnmsg) {
		$("#updateinstallbtn").attr("disabled", false);
		if (returnmsg.indexOf("html") > 0) {
			alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_1"));
		} else if (returnmsg == "success") {
			alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_16"));
			$("#auditObj").dialog("close");
		} else if (returnmsg == "fail") {
			alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_17"));
		} else {
			alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_7"));
		}
	});
}
// 关闭审核对象窗口
function closeInstall() {
	$("#flowFrame").attr("src", "");
	$("#flowFrame").attr("attrid", "");
	$(window.frames['flowFrame'].document).find("#right").empty();
	$("#auditObj").dialog("close");
}
// 获取审核流程信息
function getAuditProMsg(flowid) {
	var pathUrl = $("#pathUrl").val();
	var lguserid = $("#lguserid").val()||GlobalVars.lguserid;
	$.get(pathUrl + "/aud_auditpro.htm?method=getAudit",
					{
						flowid : flowid,
						lguserid : lguserid,
                        rk:Math.random()
					},
					function(jsonObject) {
						var json = eval("(" + jsonObject + ")");
						if(json.flowtask != null && json.flowtask != ""  && json.flowtask != "null"){
							$("#aud_flowtask").text(json.flowtask.replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
						}else{
							$("#aud_flowtask").text("-");
						}
						if(json.flowuser != null && json.flowuser != ""){
							$("#aud_flowuser").text(json.flowuser.replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
						}else{
							$("#aud_flowuser").text("-");
						}
						$("#aud_flowlevel").text(json.flowlevel);
						// 审批记录
					var recordList = json.members;
					if (recordList.length > 0) {
						var msg = "<tr class='div_bd title_bg'  height='29px'  style='font-size:13px;'><td class='div_bd'>"+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_18")+"</td><td class='div_bd'>"+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_19")+"</td><td  class='div_bd'>"+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_20")+"</td><td class='div_bd'>"+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_21")+"</td></tr>";
						for ( var i = 0; i < recordList.length; i++) {
							var type = recordList[i].objtype;
							var message = recordList[i].objmsg;
							var condition = recordList[i].objcondition;
							var conditionmsg = "";
							if (condition == 1) {
								conditionmsg = getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_22");
							} else if (condition == 2) {
								conditionmsg = getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_23");
							} else {
								conditionmsg = "-";
							}
							var typemsg = "";
							// 对象
							if (type == 1) {
								typemsg = getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_24");
							} else if (type == 4) {
								typemsg = getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_25");
							} else if (type == 5) {
								typemsg = getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_26");
							} else {
								typemsg = "-";
							}

							msg = msg
									+ " <tr class='div_bd' height='24px' > <td  align='center' width='15%' class='div_bd'>"+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_27")
									+ parseInt(i + 1)
									+ getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_28")+"</td> "
									+ "  <td align='center'  width='15%'  class='div_bd'  >"
									+ typemsg
									+ "</td>"
									+ "  <td align='center'  width='45%'  style='word-break: break-all;white-space:normal;'  class='div_bd'>"
									+ message.replaceAll("<", "&lt;")
											.replaceAll(">", "&gt;")
									+ "</td>"
									+ "  <td align='center'  width='25%' class='div_bd'>"
									+ conditionmsg + "</td> " + "  </tr>";
						}
						$("#recordTable").html(msg);
					}
					$("#auditproFlow").dialog("open");
				});
}

// 备注弹出框
function showcommentdialog(t) {
	$("#msg").empty();
	$("#msg").append($(t).children("label").text().replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
	$("#singlecomment").dialog("option", "title", getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_29"));
	$("#singlecomment").dialog("open");
}

$(document).ready(function() {
	$("#left").click(function() {
		treeLoseFocus();
	});
	$("#left").dblclick(function() {
		moveIn();
		treeLoseFocus();
	});
	$("#right").dblclick(function() {
		moveOut();
	});
});

function moveIn() {
	if ($("#left option:selected").size() == 0) {
		return;
	}
	var ids = "";
	$("#left option:selected").each(function() {
		ids = ids + $(this).val() + ",";
	});
	if (ids == "" || ids.length == 0) {
		return;
	}
	var pathUrl = $("#pathUrl").val();
	var lgcorpcode = $("#lgcorpcode").val();
	var flowid = $("#flowid").val();
	$.post(pathUrl + "/aud_auditpro.htm", {
		method : "getUserAuditMsg",
		flowid : flowid,
		lgcorpcode : lgcorpcode,
		userids : ids
	}, function(msg) {
		if (msg.indexOf("html") > 0) {
			alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_1"));
			return;
		} else if (msg.indexOf("MWHS]#") > -1) {
			var arr = msg.split("MWHS]#");
			if (arr != null && arr.length > 1) {
				var names = arr[0];
				alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_24") + names + getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_30") + arr[1]);
				return;
			} else {
				alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_31"));
				return;
			}
		} else if (msg == "fail") {
			alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_7"));
			return;
		} else if (msg == "ok") {
			addUser();
		} else if (msg == "isexist") {
			addUser();
		} else {
			alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_7"));
			return;
		}
	});
}

// 选择操作员移到右边
function addUser() {
	var tsmsgFlag = "1";
	$("#left option:selected").each(function() {
		var lguserid = parseInt($(this).val());
		// 这里判断类型 1 是机构 2是操作员
			var isdeporuser = $(this).attr("isdeporuser");
			// 成员名称
			var name = $(this).text();
			// 循环判断是否存在操作员获取机构
			var isflag = "1";
			var iscontinue = "1";
			$("#right option").each(function() {
				if (iscontinue == "2") {
					return;
				}
				var rightobjtype = $(this).attr("isdeporuser");
				var rightobjvalue = parseInt($(this).val());
				if (rightobjtype == "1" && (rightobjvalue == lguserid)) {
					isflag = "2";
					iscontinue = "2";
					tsmsgFlag = "2";
				}
			});
			if (isflag == "1") {
				$("#right").append("<option value=\'" + lguserid+ "\' isdeporuser='1'>["+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_24")+"]" + name+ "</option>");
			}
		});
	if (tsmsgFlag == "2") {
		alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_32"));
	}
}

// 移出右边的单个或者多个选择的对象
function moveOut() {
	if ($("#right option:selected").size() == 0) {
		return;
	}
	$("#right option:selected").each(function() {
		// var id = $(this).val();
			// var isdep = $(this).attr("isdeporuser");
			// var name = $(this).text();
			$(this).remove();
		});
}

// 选择机构移到右边
function addauditdep() {
	var auditdepid = $("#auditdepid").val();
	if (auditdepid == null || auditdepid == "") {
		return;
	}
	var auditdepname = $("#auditdepname").val();
	// isdeporuser 1操作员 2机构 bindattr 绑定流程1 没有 2 isexistAud 1存在该流程 2不存在该流程
	// var bindattr = $("#auditbindattr").val();
	var isexistAud = $("#auditisexist").val();
	// 成员名称
	var pathUrl = $("#pathUrl").val();
	var lgcorpcode = $("#lgcorpcode").val();
	var flowid = $("#flowid").val();
	$.post(pathUrl + "/aud_auditpro.htm", {
		method : "getDepAuditMsg",
		flowid : flowid,
		lgcorpcode : lgcorpcode,
		depid : auditdepid
	}, function(msg) {
		if (msg.indexOf("html") > 0) {
			alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_1"));
			return;
		} else if (msg.indexOf("MWHS]#") != -1) {
			msg = msg.substr(6);
			alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_25") + auditdepname + getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_33") + msg + "");
			return;
		} else if (msg == "fail") {
			alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_7"));
			return;
		} else if (msg == "ok") {
			addDep(auditdepid, auditdepname);
		} else if (msg == "isexist") {
			addDep(auditdepid, auditdepname);
		}
	});
}

/**
 * 选取机构之前先判断 是否提示 包含子机构
 * @param auditdepid
 * @param auditdepname
 */
function addDep(auditdepid, auditdepname) {
	// 是否填加
	var isflag = "1";
    //若已经存在相同的机构 则提示记录重复
	$("#right option").each(function() {
		var rightobjtype = $(this).attr("isdeporuser");
		var rightobjvalue = parseInt($(this).val());
		if (rightobjtype == "2" && (rightobjvalue == parseInt(auditdepid))) {
			isflag = "2";
		}
	});
	if (isflag == "1") {
        var isAllow = true;
        if(isAllow && confirm(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_34"))){
            $("#right").append("<option value=\'" + auditdepid + "\' isdeporuser='2' iscontain='1'>["+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_25")+"]"+ auditdepname + getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_35")+"</option>");
        }else{
            $("#right").append("<option value=\'" + auditdepid + "\' isdeporuser='2' iscontain='0'>["+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_25")+"]"+ auditdepname + "</option>");
        }

	}else{
        alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_32"));
    }
}

function treeLoseFocus()
{
	$("#sonFrame").contents().find(".curSelectedNode").removeClass("curSelectedNode");
	$("#auditdepid").val("");
	$("#auditdepname").val("");
}
function router()
{
	if($("#left option:selected").size() == 0){
		addauditdep();
 	}else{
 		moveIn();
	}
}
//查询
function searchbyname(){
	//企业 编码
	var lgcorpcode = $("#lgcorpcode").val();
	//查询的名称
	var searchname = $("#searchname").val();
	//选择的机构ID
	//var auditdepid = $("#auditdepid").val();
	var auditdepid = "";
	treeLoseFocus();
	var flowid = $("#flowid").val();
	var pathUrl = $("#pathUrl").val();
	$.post(pathUrl+"/aud_auditpro.htm", {method:"getSysuserByDepId", depId:auditdepid,searchname:searchname,lgcorpcode:lgcorpcode,flowid:flowid}, function(returnmsg){
		if(returnmsg != "" && returnmsg.indexOf("suceess#") != -1){
			var arr = returnmsg.split("#");
			$("#left").empty();
			$("#left").html(arr[1]);
		}
	});
}





// 修改审批流状态
function changestate(i) {
	var flowState = $.trim($("#bkwState" + i).attr("value"));
	var lgcorpcode = $("#lgcorpcode").val();
	var pathUrl = $("#pathUrl").val();
	$.post("aud_auditpro.htm?method=changeState",
					{
						flowid : i,
						flowState : flowState,
						lgcorpcode : lgcorpcode,
						lguserid : $("#lguserid").val(),
						isAsync : "yes"
					},
					function(result) {
						if (result == "outOfLogin") {
							$("#logoutalert").val(1);
							location.href = pathUrl+"/common/logoutEmp.html";
							return;
						}
						if (result == "true") {
							$("#bkwState" + i).empty();
							if (flowState == 1) {
								$("#bkwState" + i).append("<option value='1' selected='selected'>"+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_36")+"</option>");
								$("#bkwState" + i).append("<option value='0' >"+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_37")+"</option>");
							} else {
								$("#bkwState" + i).append("<option value='0' selected='selected'>"+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_38")+"</option>");
								$("#bkwState" + i).append("<option value='1' >"+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_39")+"</option>");
							}
							changeEmpSelect($("#bkwState" + i), 80);
							var $select = $("#bkwState" + i);
							$select.next().hide();
							$select.prev().html($select.find('option:selected').text());
							alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_40"));
							// black();
						} else if(result == "auditing") {
                            alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_71"));
                        } else {
							alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_41"));
						}
					});
}

$(document).ready(function(){
    if($("#updateTip").size()>0){
        $("#updateTip .audit-list").delegate('a','click',function(){
            var flowId = $(this).attr('data-id');
            var type = $(this).attr('info-type');
            var pathUrl = $("#pathUrl").val();
            var moudle,url;
            if(type == 1){
                moudle = "1050-1200";
                url = pathUrl+"/smt_smsSendedBox.htm?flowId="+flowId+"&taskState=1&pageIndex=1&pageSize=15&lguserid="+GlobalVars.lguserid+"&lgcorpcode="+GlobalVars.lgcorpcode;
            }else if(type == 2){
                moudle = "1100-1800";
                url = pathUrl+"/mmt_mmsTask.htm?flowId="+flowId+"&state=-1&pageIndex=1&pageSize=15&lguserid="+GlobalVars.lguserid+"&lgcorpcode="+GlobalVars.lgcorpcode+"&lgguid="+GlobalVars.lgguid;
            }else if(type == 3){
                moudle = "1400-0900";
                url = pathUrl+"/tem_smsTemplate.htm?flowId="+flowId+"&tmState=-1&pageIndex=1&pageSize=15&lguserid="+GlobalVars.lguserid+"&lgcorpcode="+GlobalVars.lgcorpcode+"&lgguid="+GlobalVars.lgguid;
            }else if(type == 4){
                moudle = '1100-1500';
                url = pathUrl+"/tem_mmsTemplate.htm?flowId="+flowId+"&rState=-1&pageIndex=1&pageSize=15&lguserid="+GlobalVars.lguserid+"&lgcorpcode="+GlobalVars.lgcorpcode+"&lgguid="+GlobalVars.lgguid;
            }else if(type == 5){
                moudle = '2700-2100';
                url = pathUrl+"/wx_taskreport.htm?flowId="+flowId+"&conrstate=1&pageIndex=1&pageSize=15&lguserid="+GlobalVars.lguserid+"&lgcorpcode="+GlobalVars.lgcorpcode+"&lgguid="+GlobalVars.lgguid;
            }else if(type == 6){
                moudle = "2600-1010";
                url = pathUrl+"/wx_manger.htm?flowId="+flowId+"&rState=1&pageIndex=1&pageSize=15&lguserid="+GlobalVars.lguserid+"&lgcorpcode="+GlobalVars.lgcorpcode+"&lgguid="+GlobalVars.lgguid;
            }
            topWindow().frames['mainFrame'].openNewTab(moudle,url);
        })
    }

})
