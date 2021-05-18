<%@ page language="java" import="java.util.List" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
String path = request.getContextPath();
String iPath = request.getRequestURI().substring(0,
	request.getRequestURI().lastIndexOf("/"));
String userInfos = (String)request.getAttribute("userInfos");
String unUseInfos = (String)request.getAttribute("unUseInfos");
String groups = (String)request.getAttribute("groups");
String gpId = request.getParameter("gpId");
String aId = request.getParameter("aId");
String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
Map<String, String> titleMap = (Map<String, String>)session.getAttribute("titleMap");
String menuCode = titleMap.get(request.getAttribute("rTitle"));
String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String)session.getAttribute("stlyeSkin");

String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>Document</title>
		<%@include file="/common/common.jsp" %>
		<link rel="stylesheet" href="<%=path %>/wxgl/group/css/base.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" href="<%=path %>/wxgl/group/css/group_info.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" href="<%=path %>/wxgl/group/css/nanoscroller2.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>">
	<script type="text/javascript" src="<%=commonPath%>/wxcommon/js/myjquery-c.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript">
			function fig(){
					document.getElementById("btn_add").click();
				}
	</script>
<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
</head>
<body onload="javascript:fig()">
	<div id="groupWrapper">
		<div id="group_info">
			<div class="group_head">
				<select id="selGroup"></select>
			</div>
			<div class="group_wrapper">
				<div class="select_panel pos_left" id="selectPanel">
					<div class="mod_h"><emp:message key="wxgl_gzhgl_title_123" defVal="成员列表：" fileName="wxgl"/><span></span></div>
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
					<button id="btn_add" class=btnClass1><emp:message key="wxgl_button_1" defVal="添加" fileName="wxgl"/></button><br /><br />
					<button id="btn_delete" class="btnClass1"><emp:message key="wxgl_button_10" defVal="删除" fileName="wxgl"/></button>
				</div>
				<div class="select_panel pos_right " id="showPanel">
					<div class="mod_h"><emp:message key="wxgl_gzhgl_title_123" defVal="成员列表：" fileName="wxgl"/><span>0</span></div>
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
<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/wxgl_<%=langName%>.js"></script>
<script src="<%=path %>/wxcommon/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=path %>/wxcommon/widget/artDialog/artDialog.js?skin=default"></script>
<script type="text/javascript" src="<%=path %>/wxcommon/widget/artDialog/iframeTools.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script src="<%=path %>/wxgl/group/js/jquery.nanoscroller.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=path%>/wxcommon/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script>
var unUseInfos = <%=unUseInfos%>;
var userInfos = <%=userInfos%>;
var groups = <%=groups%>;
var iPath = '<%=iPath%>';
var path = '<%=path%>';
var uids = ',';//原先拥有的成员id
var addUids = ',';//增加的成员id
var delUids = ',';//删除的成员id
var gpids = ',';//需要重新统计人数的群组id
var curGid = <%=gpId%>;
	$(document).ready(function(){
		scrollinit();
		var oSaveData=$('#saveData'),
			oSelectPanel=$('#selectPanel'),
			oShowPanel=$('#showPanel'),
			oShowUl=oShowPanel.find('.content ul'),
			oSelUl=oSelectPanel.find('.content ul'),
			oSelLi=oSelUl.find('li'),
			oShowLi=oShowUl.find('li'),
			selGroup=$('#selGroup');

		if(groups != null)
		{
			for(var i=0;i<groups.length;i++)
			{
				var group = groups[i];
			    selGroup.append('<option value="'+group.gpid+'" wgid="'+group.wgid+'">'+group.name+'</option>');
			}
		}
		selGroup.isSearchSelect({'width':'203','height':'120','isInput':false,'zindex':0},function(){
			var wgid = selGroup.find('> option:selected').attr('wgid');
			var gpid = selGroup.val();
			$.post(path+'/user_groupManager.htm?method=getGroupUsers&'+new Date().getTime(),{
				gpId : gpid,
				addUids : addUids,
				delUids : delUids,
				wgId	: wgid
			},function(result){
			    var gpUserInfos = eval('('+result+')');
			    loadUsers(oSelectPanel,oSelUl,gpUserInfos);
			});
		});
		loadUsers(oSelectPanel,oSelUl,unUseInfos)
		oShowPanel.find('.mod_h span').text(userInfos.length);
		if(userInfos != null)
		{
			for(var i=0;i<userInfos.length;i++)
			{
				var user = userInfos[i];
				var userHtml = '<li><dl><dt><input type="checkbox" name="" gpid="'+user.gpid+'" id="'+user.uid+'"></dt>'+
					'<dd><img src="'+path+'/wxcommon/img/icon-group.gif" alt=""></dd>'+
					'<dd>'+user.name+'</dd></dl></li>';
				oShowUl.append(userHtml);
				uids = uids + user.uid +',';
			}
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
		//“添加”按钮点击事件
		$('#btn_add').click(function(){
			var i = 0;
			oSelUl.find('li dt>input[type=checkbox]:checked').each(function(){
				var $li = $(this).parent().parent().parent();
				oShowUl.append($li);
				var uid = $(this).attr('id')+',';
				// 如果这个id是原先已拥有的id
				if(uids.indexOf(','+uid) > -1)
				{
				    delUids=delUids.replace(','+uid,',');
				}
				else// 否则为新增加的id
				{
				    addUids = addUids + uid;
				    var gpid = $(this).attr('gpid')+ ',';
					if( gpid != '' && gpids.indexOf(','+gpid) == -1)
					{
						gpids = gpids + gpid ;
					}
				}
				i = i + 1;
			});
			var showCount = parseInt(oShowPanel.find('.mod_h span').text());
			var selCount = parseInt(oSelectPanel.find('.mod_h span').text());
			oShowPanel.find('.mod_h span').text(showCount+i);
			oSelectPanel.find('.mod_h span').text(selCount-i);
			art.dialog.data('addUids',addUids);
			art.dialog.data('delUids',delUids);
			art.dialog.data('gpids',gpids);
			scrollinit();
			//art.dialog.data('group_list', document.getElementById('saveData').value);
		})
		//“删除”按钮点击事件
		$('#btn_delete').click(function(){
		    var selGpid = selGroup.val();
		    var wgid = selGroup.find('> option:selected').attr('wgid');
			var oSaveData=$('#saveData');
			var i=0,j=0;
			$('#showPanel .content li dt>input[type=checkbox]:checked').each(function(){
			    var gpid = $(this).attr('gpid');
			    if(selGpid == gpid || (curGid == gpid && wgid == 0))
			    {
			    	oSelUl.append($(this).parent().parent().parent());
			    	i = i+1;
			    }else
			    {
			        $(this).parent().parent().parent().remove();
			    }
			    var uid = $(this).attr('id')+',';
				// 如果这个id是原先已拥有的id
				if(uids.indexOf(','+uid) > -1)
				{
				    delUids=delUids+uid;
				}
				else// 否则为新增加的id
				{
				    addUids=addUids.replace(','+uid,',');
					if( gpid != '' && gpids.indexOf(','+gpid+ ',') > -1)
					{
					    gpids.replace(','+gpid+ ',',',');
					}
				}
				j = j+1;
			});
			var showCount = parseInt(oShowPanel.find('.mod_h span').text());
			var selCount = parseInt(oSelectPanel.find('.mod_h span').text());
			oShowPanel.find('.mod_h span').text(showCount-j);
			oSelectPanel.find('.mod_h span').text(selCount+i);
			///oShowPanel.find('.mod_h span').html(oShowUl.find('li').size());
			//oSaveData.val(data);
			art.dialog.data('addUids',addUids);
			art.dialog.data('delUids',delUids);
			art.dialog.data('gpids',gpids);
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
	});

	function loadUsers(oSelectPanel,oSelUl,unUseInfos)
	{
	    oSelectPanel.find('.mod_h span').text(unUseInfos.length);
		if(unUseInfos != null)
		{
			oSelUl.html('');
			for(var i=0;i<unUseInfos.length;i++)
			{
				var user = unUseInfos[i];
				var userHtml = '<li><dl><dt><input type="checkbox" name="" gpid="'+user.gpid+'" id="'+user.uid+'"></dt>'+
					'<dd><img src="'+path+'/wxcommon/img/icon-group.gif" alt=""></dd>'+
					'<dd>'+user.name+'</dd></dl></li>';
				oSelUl.append(userHtml);
			}
		}
	}
</script>

</body>
</html>