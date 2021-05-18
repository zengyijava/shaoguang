<%@ page language="java" import="java.util.List" pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
String iPath = request.getRequestURI().substring(0,
		request.getRequestURI().lastIndexOf("/"));
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
String userJson = (String)request.getAttribute("userJson");
//语言方面相关
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<!doctype html>
<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@include file="/common/common.jsp" %>
	<title><emp:message key="zxkf_chat_title_24" defVal="新建群组页面" fileName="zxkf"/></title>
	<link rel="stylesheet" href="<%=iPath %>/static/css/base.css?V=<%=StaticValue.getJspImpVersion() %>">
	<link rel="stylesheet" href="<%=iPath %>/static/css/group_info.css?V=<%=StaticValue.getJspImpVersion() %>">
	<link rel="stylesheet" href="<%=iPath %>/static/css/nanoscroller2.css?V=<%=StaticValue.getJspImpVersion() %>">
	<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
	<%}%>
</head>
<body>
	<div id="groupWrapper">
		<div id="group_info">
			<div class="group_head">
				<input type="text" maxlength="16" name="group_name" id="group_name" class="g_inp" placeholder="<emp:message key='zxkf_chat_title_65' defVal='请输入群组名称' fileName='zxkf'/>">
				&nbsp;<span style="color:#999"><emp:message key="zxkf_chat_title_25" defVal="客服名称最大长度" fileName="zxkf"/>16</span>
				<input type="hidden" name ="customeIds" id="customeIds" value=","/>
			</div>
			<div class="group_wrapper">
				<div class="select_panel pos_left" id="selectPanel">
					<div class="mod_h"><emp:message key="zxkf_chat_title_25" defVal="成员列表" fileName="zxkf"/>：<span>0</span></div>
					<div class="mod_b ">
						<div class="inner nano">
							<div class="content">
								<ul>
								</ul>
							</div>
						</div>
					</div>
				</div>
				<div class="btn_panel">
					<button id="btn_add" class="btn_g"><emp:message key="zxkf_chat_title_26" defVal="添加" fileName="zxkf"/></button><br /><br />
					<button id="btn_delete" class="btn_g"><emp:message key="zxkf_chat_title_27" defVal="删除" fileName="zxkf"/></button>
				</div>
				<div class="select_panel pos_right " id="showPanel">
					<div class="mod_h"><emp:message key="zxkf_chat_title_28" defVal="成员列表" fileName="zxkf"/>：<span>0</span></div>
					<div class="mod_b ">
						<div class="inner nano">
						   <div class="content">
							<ul>
								
							</ul>
						   </div>	
						</div>
					</div>
				</div>
		
				
			</div>
		</div>
	</div>
<input type="hidden" id="saveData" name="saveData">	
<script src="<%=iPath %>/static/js/myjquery-k.js"></script>
<script type="text/javascript" src="<%=path %>/common/widget/artDialog/artDialog.js?skin=default"></script>
<script type="text/javascript" src="<%=path %>/common/widget/artDialog/iframeTools.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=iPath %>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript" src="<%=iPath %>/common/i18n/<%=langName%>/zxkf_<%=langName%>.js"></script>
<script src="<%=iPath %>/static/js/jquery.nanoscroller.js?V=<%=StaticValue.getJspImpVersion() %>"></script>

<script>
var userJson = <%=userJson%>;
var iPath = '<%=iPath%>';
art.dialog.data('group_name', '');
art.dialog.data('group_list', '');
	$(document).ready(function(){
		scrollinit();
		var oSaveData=$('#saveData'),
			oSelectPanel=$('#selectPanel'),
			oShowPanel=$('#showPanel'),
			oShowUl=oShowPanel.find('.content ul'),
			oSelUl=oSelectPanel.find('.content ul'),
			oSelLi=oSelUl.find('li'),
			oShowLi=oShowUl.find('li');
		oSelectPanel.find('.mod_h span').html(userJson.length);
		
		for(var i=0;i<userJson.length;i++)
		{
			var user = userJson[i];
			var userHtml = '<li><dl><dt><input type="checkbox" name="" id="'+user.customeId+'"></dt>'+
				'<dd><img src="'+iPath+'/static/images/icon-group.gif" alt=""></dd>'+
				'<dd>'+user.name+'</dd></dl></li>';
			oSelUl.append(userHtml);
		}
		
		$('.mod_b').delegate('li',{
			mouseenter:function(){
					$(this).addClass('hover');
			},
			mouseleave:function(){
				
					$(this).removeClass('hover');
			},
			click:function(){
				var oCheckbox=$(this).find('input[type=checkbox]');
				oCheckbox.trigger('click');
			}		
		})
		$('.mod_b').delegate('input[type="checkbox"]','click',function(e){
			e.stopPropagation();
		})
		$('#btn_add').click(function(){
			var aArray=[],data=[];
			//oShowUl.html('');
			var mcount = 0;
			oSelUl.find('li').each(function(){
				var oCheckbox=$(this).find('dt>input[type=checkbox]');
				if(oCheckbox.is(':checked')){
					mcount = mcount + 1;
					if(oShowUl.find('dt>input[id='+oCheckbox.attr('id')+']').length == 0)
					{
						oShowUl.append($(this).clone());
						oShowUl.find('dt>input[type=checkbox]').prop('checked',false);
						data.push(oCheckbox.attr('id'));
					}
				}
			})
			if(mcount == 0){
				alert(getJsLocaleMessage('zxkf','zxkf_chat_title_29'));
			}else{
				oSaveData.val(data);
				oShowPanel.find('.mod_h span').html(oShowUl.find('li').size());
				scrollinit();
				art.dialog.data('group_list', document.getElementById('saveData').value);
			}
		})
		$('#btn_delete').click(function(){
			var data=[],
				oSaveData=$('#saveData');
			if($('#showPanel .content li dt>input[type=checkbox]:checked').length==0)
			{
				alert(getJsLocaleMessage('zxkf','zxkf_chat_title_30'));
				return;
			}
			data=[];
			$('#showPanel .content li').each(function(){
				var oCheckbox=$(this).find('dt>input[type=checkbox]');
				if(oCheckbox.is(':checked')){
					$(this).remove();
				}else{
					data.push(oCheckbox.attr('id'));
				}
				
			})
			oShowPanel.find('.mod_h span').html(oShowUl.find('li').size());
			oSaveData.val(data);
			art.dialog.data('group_list', document.getElementById('saveData').value);
			scrollinit();
		})
		$('#group_name').keyup(function(){
			var group_name = document.getElementById('group_name').value;
			art.dialog.data('group_name', group_name);
		})
		function scrollinit(){
			$(".nano").nanoScroller({
				sliderMaxHeight:60
			});
		}
	})
</script>

</body>
</html>