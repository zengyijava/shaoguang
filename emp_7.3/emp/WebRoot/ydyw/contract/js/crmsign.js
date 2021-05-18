var ztree;
var lguserid;
var ztree_action = 'ydyw_crmSignQuery.htm?method=getClientDepTree';
var setting = {
	checkable : true,
    checkStyle : "checkbox",
    checkType : { "Y": "s", "N": "s" },
	async : true,
	asyncUrl:"",
	isSimpleData: true,
	rootPID : -1,
	treeNodeKey: "id",
	treeNodeParentKey: "pId",
	callback: {
		change: zTreeOnClick,
		asyncSuccess:function(event, treeId, treeNode, msg){
		if(!treeNode){	//判断是 顶级机构就展开,其余的收缩+	
			 var rootNode = ztree.getNodeByParam("level", 0);
			 ztree.expandNode(rootNode, true, false);
		}
		},
		beforeAsync:function(treeId,treeNode){
			ztree.setting.asyncUrl= ztree_action + "&lguserid="+$('#lguserid').val()+"&depId="+treeNode.id;
		}
		}
};
function zTreeOnClick(event, treeId, treeNode) {
	if (treeNode) {
		var zTreeNodes;
		zTreeNodes = ztree.getChangeCheckedNodes();
		var pops='';
		var userids = '';
		var popsArr = [];
		var idsArr = [];
		if(!!$('#depId').val()){
			popsArr = $('#cli-dep').val().split(';'); 
			idsArr = $('#depId').val().split(',');
			}
		if(treeNode.checked){//选中
			for(var i=0; i<zTreeNodes.length; i++){
				zTreeNodes[i].checkedOld = zTreeNodes[i].checked;
				popsArr.push(zTreeNodes[i].name);
				idsArr.push(zTreeNodes[i].id);
			}
			popsArr = popsArr.unique();
			idsArr = idsArr.unique();
		}else{//取消选中
			for(var i=0; i<zTreeNodes.length; i++){
				zTreeNodes[i].checkedOld = zTreeNodes[i].checked;
				popsArr = popsArr.remove(zTreeNodes[i].name);
				idsArr = idsArr.remove(zTreeNodes[i].id);
			}
		}
        /*点击选择机构*/
		$('#cli-dep').val(popsArr.join(';')||getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_34"));
		$("#depId").val(idsArr.join(','));
	}
};
function zTreeOnClickOK() {
	$('#cli-dep').click();
}
function cleanSelect() {
	var checkNodes ;
	checkNodes = ztree.getCheckedNodes();
	for ( var i = 0; i < checkNodes.length; i++) {
		checkNodes[i].checked = false;
	}
	ztree.refresh();
	/*点击选择机构*/
	$('#cli-dep').val(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_34"));
	$("#depId").val('');
}
var zNodes =[];
var ischange = false;
$(document).ready(function(){
	$('#cli-dep').click(function(){
		var tree = $(this).next('div');
		tree.toggle();
		var sel =$('#taocan').find('.sel-mut'); 
		if(tree.is(':hidden')){
			sel.css('visibility','visible');
		}else{
			sel.css('visibility','hidden');
		}
		if(!ztree){
			setting.asyncUrl = ztree_action + '&lguserid='+ $('#lguserid').val();
			ztree = tree.find("#dropdownMenu").zTree(setting, zNodes);
		}
	});
	//监控输入框是否变动 为取消二次确认提供判断
	filterChar('phone,cliname,cardNo,cliCode,address,account,accountName,feeAccount,feeAccountName',function(){
		ischange = true;
	});
	$('#cli-dep').click(function(){
		ischange = true;
	});
	//手机号码 账号 扣费账号非法字符过滤
    filterChar('phone',/[^\d\+]/g);//手机号允许数字和+
	filterChar('account,feeAccount', /\D/g);
	//姓名非法字符过滤
	filterChar('cliname,accountName,feeAccountName',/[\*\_\#\:\?\<\>\|\&]+/g);
	//账号信息变化 同步 扣费账号信息
	filterChar('account,accountName',syncAccount);
	//签约账号唯一性检测
	$('#account').blur(function(){
		var $this = $(this);
		var value = $this.val();
		if(value && value != $this.attr('old')){
			var $button = $('.btn-submit').children('input');
			$.ajax({
			type:"POST",
			url: "ydyw_crmSignQuery.htm?method=isUniqueAcct",
			data: {account:value,lgcorpcode : $('#lgcorpcode').val(),isAsync:"yes"},
			beforeSend: function(){},
			complete: function(){},
			success: function(result){
				if(isOutOfLogin(result)){return;}
				result = eval('('+result+')');
				if(result.errcode == 1){
					/*该签约账号已存在！请重新输入*/
					alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_35"));
					$this.val('');
					syncAccount();
				}
			}
			});	
		}
	});
	//客户编号唯一性检测
	$('#cliCode').blur(function(){
		var $this = $(this);
		if($this.attr('disabled')){return;}
		var value = $this.val();
		if(value){
			var $button = $('.btn-submit').children('input');
			$.ajax({
			type:"POST",
			url: "ydyw_crmSignQuery.htm?method=isUniqueClicode",
			data: {cliCode:value,lgcorpcode : $('#lgcorpcode').val(),isAsync:"yes"},
			beforeSend: function(){},
			complete: function(){},
			success: function(result){
				if(isOutOfLogin(result)){return;}
				result = eval('('+result+')');
				if(result.errcode == 1){
					/*该客户编号已存在！请重新输入*/
					alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_36"));
					$this.val('');
				}
			}
			});	
		}
	});
	//扣费类型变化 处理 同步账号信息
	$('input[name="feeType"]').change(syncAccount);
	//证件号码
	filterChar('cardNo', /[\u4E00-\u9FA5]|[\uFE30-\uFFA0]/g);
	//手机号码验证有效性
	filterChar('phone', function(){
		var self = $('#phone');
		var phone = self.val();
		var ul = $('#phone-list');
		ul.empty().hide();
		if($('#cliname').attr('disabled')){
			clearVal();
		}
		if(checkPhone(phone) && asyncCheckPhone(phone)){
			$.ajax({
				type:"POST",
				async:false,
				url: "ydyw_crmSignQuery.htm",
				data: {method:"findClientByPhone",phone:phone,lgcorpcode:$('#lgcorpcode').val(),isAsync:"yes"},
				success: function(result){
					if(isOutOfLogin(result)){return;}
					var result = eval('('+result+')');
					var ul = self.siblings('ul');
					ul.empty();
					for(var i=0;i<result.length;i++){
						var li = result[i];
						ul.append($('<li>').attr('data',JSON.stringify(li)).html(li.name));
					}
					if(result.length > 0){
						var first = result[0];
						setVal(first);
						if(result.length>1){
							ul.show();
						}
					}
				}
			});
		}
	});
	$('#phone').focus(function(){
		var ul = $('#phone-list');
		if(ul.children().size()>1){
			ul.show();
		}
	});
	$('#phone,#cliname').focus(function(){
		$(this).css('borderColor','');
	});
	$('#taocan,#cli-dep').click(function(){
		$(this).css('borderColor','');
	});
	$('#phone-list,#phone').click(function(){
		var evt = window.event || arguments.callee.caller.arguments[0];
		var target = evt.target || evt.srcElement;
		if(!target.id){
			$('#phone-list').hide();
			var obj = eval('('+$(target).attr('data')+')');
			setVal(obj);
		}
		return false;
	});
	$(document).click(function(){
		$('#phone-list').hide();
	});
	//phonelist匹配手机号码列表
	$('#phone-list li').live('mouseover',function(){
		$(this).css('background','#eee');
	}).live('mouseout',function(){
		$(this).css('background','');
	});
	$('#serach-sel').change(function(){
		$('#search').trigger('click');
	})
	$('#search').click(function(){
		var tcname = $.trim($('#tcname').val());
		var tctype = $('#serach-sel').val();
		var sels = [];
		$('#select-tc').children('option').each(function(){
			var id = $(this).attr('id');
			if(id){sels.push(id);}
		})
		sels = sels.join(',');
		var $button = $('.btn-submit').children('input');
		var $l_sel = $('.zs-left').children('select').first();
        var empLangName = $("#empLangName").val();
		$.ajax({
			type:"POST",
			url: "ydyw_crmSignQuery.htm?method=findTaocans",
			data: {type:tctype,name:tcname,ids:sels,lgcorpcode:$('#lgcorpcode').val(),isAsync:"yes",empLangName:empLangName},
			//beforeSend: function(){$button.attr("disabled",true);},
			//complete: function(){$button.attr("disabled",false);},
			success: function(result){
				if(isOutOfLogin(result)){return;}
				$l_sel.empty();
				var $div = $('#taocan .tip');
				result = eval('('+result+')');
				var len = result.length;
				if(len == 0){
					if($div.size()==0){
						$div = $('<div class="tip">'+getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_37")+'</div>');
						$('#taocan').prepend($div);
					}
					$div.show();
				}else{
					$div.hide();
				}
				for(var i=0;i<len;i++){
					var item = result[i];
					var $option = $('<option id="'+item.id+'" type="'+item.type+'" money="'+item.money+'">'+item.name+'&nbsp;&nbsp;('+item.code+'&nbsp;&nbsp;'+item.zf+')</option>');
					$option.attr('_name',item.name);
					$option.attr('code',item.code);
					$option.attr('title',$option.text());
					$l_sel.append($option);
				}
			},
			error:function(){
			}
			});
	})
	
});
//手机关联客户自动赋值
function setVal(obj){
	$('#cliname').val(obj.name).attr('disabled',true);
	$('#cliCode').val(obj.client_code).attr('disabled',true);
	$('#depId').val(obj.dep_id);
	$('#cliGuid').val(obj.guid);
	$('#cli-dep').val(obj.dep_name).attr('disabled',true);
	$('#cli-dep').removeClass('treeInput');
	$('#dropMenu').hide();
}
//清除自动赋值的值
function clearVal(){
	$('#cliname').val('').attr('disabled',false);
	$('#cliCode').val('').attr('disabled',false);
	$('#depId').val('');
	$('#cliGuid').val('');
	$('#cli-dep').val('').attr('disabled',false);
	$('#cli-dep').addClass('treeInput');
}
function syncAccount(){
	if($('input[name="feeType"]:checked').val() == 0){
		$('#feeAccount').val($('#account').val()).attr('disabled',true);
		$('#feeAccountName').val($('#accountName').val()).attr('disabled',true);
		$('#feeAccount').parents("tr").hide();
	}else{
		$('#feeAccount').attr('disabled',false);
		$('#feeAccountName').attr('disabled',false);
		$('#feeAccount').parents("tr").show();
	}
}

//确认 有效验证 提交
function doOk(){
    /*0  签约操作成功！短信通知发送至网关成功。*/
    /*1  签约操作成功！短信通知发送至网关失败。*/
    /*-1 请求参数错误！*/
    /*-2 该签约账号已签约，请在原记录中修改！*/
    /*-3 已存在该客户编号！*/
    /*-4 该手机号码已签约，请在原记录中修改！*/
    /*-99 请求异常！*/
    var msg = {
        '0':getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_38"),
        '1':getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_39"),
        '-1':getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_40"),
        '-2':getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_41"),
        '-3':getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_42"),
        '-4':getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_43"),
        '-99':getJsLocaleMessage("ydyw","ydyw_ywgl_ywtcgl_text_51")
    };
	var phone = $('#phone').val();
	var _phone = $('#phone').attr('old');
	if(phone){
		if(!checkPhone(phone)){
			/*手机号码格式有误，请重新输入！*/
			alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_44"));
			return;
		}
		if(!asyncCheckPhone(phone)){
			/*手机号码号段不存在，请重新输入！*/
			alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_45"));
			return;
		}
	}else{
		$('#phone').css('borderColor','red');
		/*请输入手机号码必填项！*/
		alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_46"));
		return;
	}
	var cliname = $('#cliname').val();
	if(!cliname){
		$('#cliname').css('borderColor','red');
		/*请输入姓名必填项！*/
		alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_47"));
		return;
	}
	var cardType = $('#cardType').val();
	var cardNo = $('#cardNo').val();
	if(!!cardType != !!cardNo){
		/*证件类型或证件号码填写不正确！*/
		alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_48"));
		return;
	}
	
	var depId = $('#depId').val();
	if(!depId){
		$('#cli-dep').css('borderColor','red');
		alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_49"));
		return;
	}
	
	if(!$('#select-tc').children().size()){
		$('#taocan').css('borderColor','red');
		/*请输入机构必填项！*/
		alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_50"));
		return;
	}
	var codes = [], moneys =[], types = [],names = [];
	$('#select-tc option').each(function(){
		var $self = $(this);
		codes.push($self.attr('code'));
		moneys.push($self.attr('money'));
		types.push($self.attr('type'));
		names.push($self.attr('_name'));
	})
	var data = {phone:phone,_phone:_phone,cliname:cliname,cardType:cardType,cardNo:cardNo,depId:depId};
	data.lgcorpcode = $('#lgcorpcode').val();
	data.id = $('#contractId').val();
	data.guid = $('#cliGuid').val();
	data._guid = $('#_cliGuid').val();
	data.cliCode = $('#cliCode').val();
	data.address = $('#address').val();
	data.account = $('#account').val();
	data._account = $('#account').attr('old');
	data.accountName = $('#accountName').val();
	data.feeType = $('#feeType').val();
	data.feeAccount = $('#feeAccount').val();
	data.feeAccountName = $('#feeAccountName').val();
	data.codes = codes.join(',');
	data.moneys = moneys.join(',');
	data.types = types.join(',');
	data.names = names.join('<>');
	data.isAsync="yes";
	var $this = $('.btn-submit').children('input');
	/*新增签约失败！*/
	var opname = getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_51");
	/*修改签约失败！*/
	if(data.id){opname = getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_55");}
	$.ajax({
	type:"POST",
	url: "ydyw_crmSignQuery.htm?method=add",
	data: data,
	beforeSend: function(){$this.attr("disabled",true);},
	complete: function(){$this.attr("disabled",false);},
	success: function(result){
		if(isOutOfLogin(result)){return;}
		result = eval('('+result+')');
		var errcode = result.errcode;
		/*签约操作失败！*/
		var res = msg[errcode]||getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_52");
		alert(res);
		if(errcode >= 0){
			window.location.href = 'ydyw_crmSignQuery.htm?method=find'; 
		}
	},
	error:function(){
		alert(opname);
	}
	});	
}

//取消返回列表界面
function doSelectEClose(){
	/*您确定不保存当前录入的信息？*/
	if(!ischange || confirm(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_54"))){
		window.location.href = 'ydyw_crmSignQuery.htm?method=find';
	}
}
