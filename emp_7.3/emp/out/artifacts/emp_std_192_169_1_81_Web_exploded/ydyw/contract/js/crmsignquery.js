var ztree_action = 'ydyw_crmSignQuery.htm?method=getOptDepTree';
var setting = {
			async : true,
			asyncUrl :ztree_action+"&lguserid="+$("#lguserid").val(), //获取节点数据的URL地址
			isSimpleData: true,
			rootPID : -1,
			treeNodeKey: "id",
			treeNodeParentKey: "pId",
			asyncParam: ["depId"],
			callback: {
				click: zTreeOnClick,
				asyncSuccess:function(event, treeId, treeNode, msg){
					if(!treeNode){
					   var rootNode = ztree.getNodeByParam("level", 0);
					   ztree.expandNode(rootNode, true, false);
					}
				}
			}
	};
var ztree,zNodes = [];
function selVal(treeNode){
	if (treeNode) {
		$('#opt-dep').val(treeNode.name);
		$("#depName").val(treeNode.id);
	}else{
		$('#opt-dep').val(getJsLocaleMessage("common","common_pleaseSelect"));
		$("#depName").val('');
	}
}
function zTreeOnSure(){
	$('#dropMenu').hide();
}
function zTreeOnCancel(){
	$('#opt-dep').val(getJsLocaleMessage("common","common_pleaseSelect"));
	$("#depName").val('');
}
function zTreeOnClick(event, treeId, treeNode) {
	selVal(treeNode);
};
$(document).ready(function(){
	$('#opt-dep').click(function(){
		var tree = $(this).next('div');
		tree.toggle();
		if(!ztree){
			setting.asyncUrl = ztree_action + '&lguserid='+ $('#lguserid').val();
			ztree = tree.find("#dropdownMenu").zTree(setting, zNodes);
		}
	});
	$("#more-info").dialog({
		autoOpen: false,
		height:470,
		width: 600,
		modal: true,
		open:function(){
		},
		close:function(){
		}
	});
	$("#more-info input").click(function(){
		$("#more-info").dialog('close');
	});
	//更多详情
	$('.more').live('click',function(){
		var $this = $(this);
		var $tr = $this.closest('tr');
		$('#more-info .data').each(function(){
			var data_id = $(this).attr('data-id');
			var text;
			var $font = $(this).children('font');
			$font.text(text||'-');
			if(data_id == 'taocan'){
				text = $tr.find('[data-id="'+data_id+'"]').html();
				$font.html(text||'-');
			}else{
				text = $tr.find('[data-id="'+data_id+'"]').text();
				$font.text(text||'-');
			};
			if(data_id == 'acct' || data_id == 'feeAcct'){
				$font.attr('title',$font.text());
			}
		});
		$("#more-info").dialog('open');
		var $cliDep = $('#more-info .data[data-id="cliDep"]').children('font');
		var guid = $this.attr('data-id');
		$.ajax({
			type:"POST",
			url: "ydyw_crmSignQuery.htm?method=findClientDepsByGuid",
			data: {guid:guid,lgcorpcode : $('#lgcorpcode').val(),time:new Date(),isAsync:"yes"},
			beforeSend: function(){$cliDep.text(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_29"));},
			complete: function(){},
			success: function(result){
				if(isOutOfLogin(result)){return;}
				if(result){
					result = eval('('+result+')');
					var depname = [];
					for(var i=0;i<result.length;i++){
						depname.push(result[i].dep_name);
					}
					$cliDep.text(depname.join(",")).css('color','');
					$cliDep.attr('title',$cliDep.text());
				}else{
					$cliDep.text(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_30")).css('color','red');
				}
			},
			error:function(){
				$cliDep.text(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_28")).css('color','red');
			}
			});
	});
	//编辑
	$('.edit').live('click',function(){
		var $this = $(this);
		var id = $this.parent('td').attr('data-id');
		window.location.href = "ydyw_crmSignQuery.htm?method=toEdit&contractId="+id;
	});
	//改变签约状态
	$('.state').live('click',function(){
		var $this = $(this);
		var id = $this.parent('td').attr('data-id');
		var state = $this.attr('state');
		if(!confirm(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_31")+$this.text().toLowerCase()+'？')){
			return;
		}
		$.ajax({
			type:"POST",
			url: "ydyw_crmSignQuery.htm?method=updateState",
			data: {id:id,state:state,time:new Date(),isAsync:"yes"},
			beforeSend: function(){$this.attr("disabled",true);},
			complete: function(){$this.attr("disabled",false);},
			success: function(result){
				if(isOutOfLogin(result)){return;}
				var json = eval('('+result+')');
				if(json.errcode == -1){
					alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_32"));
				}else if(json.errcode == 0){
					alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_33"));
					submitForm();
				}
			},
			error:function(){
				alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_32"));
			}
			});	
	});
})