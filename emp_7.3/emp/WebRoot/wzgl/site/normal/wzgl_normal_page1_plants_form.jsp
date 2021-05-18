<%@ page language="java" import="java.util.List" pageEncoding="UTF-8"%>
<%@page import="java.util.HashMap"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="com.montnets.emp.ottbase.util.GlobalMethods"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp"
	uri="http://www.montnets.com/emp/i18n/tags/simple"%>
	
<%
/**
备注：
1.控件表单编辑页面
2.wzgl_normal_page1(模块名+微站风格类型+当前页面类型)
3.异步请求getPageInfo返回该页面
5.改页面需要的数据
	resultMap的格式为:
    	resultMap = {"控件类型":"控件值"，["控件类型":"控件的值"],...}
	例如图片滚动类型:
		控件类型： normal_head
		控件值: {"plantId":"620","plantType":"normal_head","count":2,"items":[{"head_link":"http:\/\/www.baidu.com","head_imgurl":"http:\/\/www.baidu.com","head_title":"我的世界"},{"head_link":"http:\/\/www.baidu.com","head_imgurl":"http:\/\/www.baidu.com","head_title":"你的世界1"}]}
**/
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
//使用集群，文件服务器的地址
String filePath = GlobalMethods.getWeixFilePath();

//Jsp页面中获取session中的语言设置
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);	

//当前页面的plants
HashMap<String,JSONObject> resultMap = (HashMap<String,JSONObject>)request.getAttribute("resultMap");
%>
<link rel="stylesheet" type="text/css" href="<%=path%>/common/css/file.css?V=<%=StaticValue.getJspImpVersion() %>" />
<form id="plant_form_normal_head" plantType="normal_head" class="page_item_plant_view" style="display:none;">
	<div class="buttons" style="margin:10px 0 0 10px">
	     <input name="allowcount" value="3" type="hidden"/>
		 <a href="javascript:;" class="addNoti" id="add_normal_banner_img" ><emp:message key="wzgl_qywx_site_text_33" defVal="新增一行"
											fileName="wzgl" /></a>
    </div>
</form>
<form id="plant_form_normal_link" plantType="normal_link" class="page_item_plant_view" style="display:none;">
    <ul class="item">
		<li>
			<span style="float:left;"><emp:message key="wzgl_qywx_site_text_34" defVal="备注："
											fileName="wzgl" /></span>
			<div class="input_item">
				<label>
					<input type="text" maxlength="10" required class="bd_none" name="link_note">
				</label>
			</div>
		</li>
		<li>
			<span style="float:left;"><emp:message key="wzgl_qywx_site_text_35" defVal="一键拨号："
											fileName="wzgl" /></span>
			<div class="input_item"> 
				<label>
					<input type="text" name="link_phone" maxlength="11" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">
				</label>
			</div>
		</li>
		<li>
			<span style="float:left;"><emp:message key="wzgl_qywx_site_text_36" defVal="背景色："
											fileName="wzgl" /></span>
			<div class="input_item"> 
				<label>
					<input name="link_bgcolor" maxlength="6" size="6" maxlength="8" class="colorpickerField">
				</label>
			</div>
		</li>
	</ul>
</form>

<form id="plant_form_normal_list" plantType="normal_list" class="page_item_plant_view" style="display:none;">
	<div class="buttons" style="margin:10px 0 0 10px">
	 	 <input name="allowcount" value="6" type="hidden"/>
		 <a href="javascript:;" class="addNoti" id="add_normal_banner_list" style="background-position: 0px 0px; line-height: 2.2em;">
			<emp:message key="wzgl_qywx_site_text_33" defVal="新增一行"
											fileName="wzgl" /></a>
    </div>
</form>

<form id="plant_form_normal_menu" plantType="normal_menu" class="page_item_plant_view" style="display:none;">
	<div class="buttons" style="margin:10px 0 0 10px">
	 	 <input name="allowcount" value="4" type="hidden"/>
		 <a href="javascript:;" class="addNoti" id="add_normal_banner_menu" style="background-position: 0px 0px; line-height: 2.2em;">
		 	<emp:message key="wzgl_qywx_site_text_33" defVal="新增一行"
											fileName="wzgl" /></a>
    </div>
</form>

<div id="normal_plant_form_hidden" style="display:none">
	<%-- normal_head 表单 start --%>
	<ul class="item normal_head">
	    <li class="lv1"><span class="st"><emp:message key="wzgl_qywx_site_text_37" defVal="收起"
											fileName="wzgl" /></span><span class="delNoti">X</span></li>
	    <li>
		    <ul>
				<li>
					<span style="float:left;"><emp:message key="wzgl_qywx_site_text_38" defVal="标题："
											fileName="wzgl" /></span>
					<div class="input_item">
						<label>
							<input type="text" maxlength="15" class="bd_none" value="" name="head_title">
						</label>
					</div>
				</li>
				<li>
					<span style="float:left;"><emp:message key="wzgl_qywx_site_text_39" defVal="图片："
											fileName="wzgl" /></span>
					<div class="input_item" > 
							<img src="wzgl/site/img/wzpic_b2_zh_HK.jpg"  class="head_img" name="head_img" id="head_img"/>
							<input type="hidden" name="head_imgurl" value="wzgl/site/img/wzpic_b2.jpg">
							<%-- input type="file" value="" name="head_imgField" --%>
							<form id="uploadForm" name="uploadForm" action="" method="post" enctype="multipart/form-data">
								  <div>
                                    <a href="javascript:;" class="file"><emp:message key='wxgl_button_14' defVal='浏览' fileName='wxgl'/>
								    	<input type="file" name="uploadFile" id="uploadFile" class="uploadFile">
									</a>
                                  </div>  
							</form>
						
					</div>
				</li>
				<li>
					<span style="float:left;"><emp:message key="wzgl_qywx_site_text_40" defVal="类型："
											fileName="wzgl" /></span>
					<div class="input_item"> 
						<label>
							<select class="changeTp" name="head_tp">
								<option value="0"><emp:message key="wzgl_qywx_site_text_41" defVal="外链"
											fileName="wzgl" /></option>
								<option value="1"><emp:message key="wzgl_qywx_site_text_42" defVal="微页面"
											fileName="wzgl" /></option>
								<%-- <option value="2">表单</option> --%>
							</select>
						</label>
						<div class="tp_linked" style="display:block">
							<a href="#1" class="tpLinkTilte">URL</a>
							<input class="tpLinkValue" type="text" name="tp0_value">
							<input class="tpLinkNote" type="hidden" name="tp0_note">
						</div>
						<div class="tp_linked" style="display:none">
							<a href="#1" class="tpLinkTilte"><emp:message key="wzgl_qywx_site_text_43" defVal="选择微站页面"
											fileName="wzgl" /></a>
							<input class="tpLinkValue" type="hidden" name="tp1_value">
							<input class="tpLinkNote" type="hidden" name="tp1_note">
						</div>
						<div class="tp_linked" style="display:none">
							<a href="#1" class="tpLinkTilte"><emp:message key="wzgl_qywx_site_text_44" defVal="选择表单"
											fileName="wzgl" /></a>
							<input class="tpLinkValue" type="hidden" name="tp2_value">
							<input class="tpLinkNote" type="hidden" name="tp2_note">
						</div>
					</div>
				</li>
		    </ul>
	    </li>
	</ul>
	<%-- normal_head 表单 end --%>
	
	<%-- normal_list 表单 start --%>
	<ul class="item normal_list">
	    <li class="lv1"><span class="st"><emp:message key="wzgl_qywx_site_text_37" defVal="收起"
											fileName="wzgl" /></span><span class="delNoti">X</span></li>
	    <li>
		    <ul>
				<li>
					<span style="float:left;"><emp:message key="wzgl_qywx_site_text_38" defVal="标题："
											fileName="wzgl" /></span>
					<div class="input_item">
						<label>
							<input type="text" maxlength="10" class="bd_none" name="list_title">
						</label>
					</div>
				</li>
				<li>
					<span style="float:left;"><emp:message key="wzgl_qywx_site_text_39" defVal="图片："
											fileName="wzgl" /></span>
					<div class="input_item" style="height:50px;"> 
						<label>
							<img src="wzgl/site/img/normal_list3.jpg"  class="tab_img" name="list_img" id="list_img"/>
							<input type="hidden" name="list_imgurl" value="wzgl/site/img/normal_list3.jpg">
							<form id="uploadForm" name="uploadForm" action="" method="post" enctype="multipart/form-data">
								<div>
                                    <a href="javascript:;" class="file"><emp:message key='wxgl_button_14' defVal='浏览' fileName='wxgl'/>
								    	<input type="file" name="uploadFile" id="uploadFile" class="uploadFile">
									</a>
                                  </div>  
							</form>
						</label>
					</div>
				</li>
				<li>
					<span style="float:left;"><emp:message key="wzgl_qywx_site_text_45" defVal="简介："
											fileName="wzgl" /></span>
					<div class="input_item"> 
						<label>
							<input type="text" name="list_note" maxlength="20">
						</label>
					</div>
				</li>
				<li>
					<span style="float:left;"><emp:message key="wzgl_qywx_site_text_46" defVal="类型："
											fileName="wzgl" /></span>
					<div class="input_item"> 
						<label>
							<select class="changeTp" name="list_tp">
								<option value="0"><emp:message key="wzgl_qywx_site_text_41" defVal="外链"
											fileName="wzgl" /></option>
								<option value="1"><emp:message key="wzgl_qywx_site_text_42" defVal="微页面"
											fileName="wzgl" /></option>
								<%-- <option value="2">表单</option> --%>
							</select>
						</label>
						<div class="tp_linked" style="display:block">
							<a href="#1" class="tpLinkTilte">URL</a>
							<input class="tpLinkValue" type="text" name="tp0_value">
							<input class="tpLinkNote" type="hidden" name="tp0_note">
						</div>
						<div class="tp_linked" style="display:none">
							<a href="#1" class="tpLinkTilte"><emp:message key="wzgl_qywx_site_text_43" defVal="选择微站页面"
											fileName="wzgl" /></a>
							<input class="tpLinkValue" type="hidden" name="tp1_value">
							<input class="tpLinkNote" type="hidden" name="tp1_note">
						</div>
						<div class="tp_linked" style="display:none">
							<a href="#1" class="tpLinkTilte"><emp:message key="wzgl_qywx_site_text_44" defVal="选择表单"
											fileName="wzgl" /></a>
							<input class="tpLinkValue" type="hidden" name="tp2_value">
							<input class="tpLinkNote" type="hidden" name="tp2_note">
						</div>
					</div>
				</li>
			</ul>
		</li>
	</ul>
	<%-- normal_list 表单 end --%>
	
	<%-- normal_menu 底部菜单  start --%>
	<ul class="item normal_menu">
	    <li class="lv1"><span class="st"><emp:message key="wzgl_qywx_site_text_37" defVal="收起"
											fileName="wzgl" /></span><span class="delNoti">X</span></li>
	    <li>
		    <ul>
				<li>
					<span style="float:left;"><emp:message key="wzgl_qywx_site_text_47" defVal="名称："
											fileName="wzgl" /></span>
					<div class="input_item">
						<label>
							<input type="text" maxlength="4" class="bd_none" name="menu_title">
						</label>
					</div>
				</li>
				<li>
					<span style="float:left;"><emp:message key="wzgl_qywx_site_text_48" defVal="文字颜色："
											fileName="wzgl" /></span>
					<div class="input_item">
						<label>
							<input type="text" maxlength="20" class="bd_none colorpickerField" value="ffffff" name="menu_fontcolor">
						</label>
					</div>
				</li>
				<li>
					<span style="float:left;"><emp:message key="wzgl_qywx_site_text_49" defVal="背景颜色："
											fileName="wzgl" /></span>
					<div class="input_item">
						<label>
							<input type="text" maxlength="20" class="bd_none colorpickerField" value="4791d1" name="menu_bgcolor">
						</label>
					</div>
				</li>
				<li>
					<span style="float:left;"><emp:message key="wzgl_qywx_site_text_46" defVal="类型："
											fileName="wzgl" /></span>
					<div class="input_item"> 
						<label>
							<select class="changeTp"  name="menu_tp">
								<option value="0"><emp:message key="wzgl_qywx_site_text_41" defVal="外链"
											fileName="wzgl" /></option>
								<option value="1"><emp:message key="wzgl_qywx_site_text_42" defVal="微页面"
											fileName="wzgl" /></option>
								<%--  <option value="2">表单</option> --%>
							</select>
						</label>
						<div class="tp_linked" style="display:block">
							<a href="#1" class="tpLinkTilte">URL</a>
							<input class="tpLinkValue" type="text" name="tp0_value">
							<input class="tpLinkNote" type="hidden" name="tp0_note">
						</div>
						<div class="tp_linked" style="display:none">
							<a href="#1" class="tpLinkTilte"><emp:message key="wzgl_qywx_site_text_43" defVal="选择微站页面"
											fileName="wzgl" /></a>
							<input class="tpLinkValue" type="hidden" name="tp1_value">
							<input class="tpLinkNote" type="hidden" name="tp1_note">
						</div>
						<div class="tp_linked" style="display:none">
							<a href="#1" class="tpLinkTilte"><emp:message key="wzgl_qywx_site_text_44" defVal="选择表单"
											fileName="wzgl" /></a>
							<input class="tpLinkValue" type="hidden" name="tp2_value">
							<input class="tpLinkNote" type="hidden" name="tp2_note">
						</div>
					</div>
				</li>
			
			</ul>
		</li>
	</ul>
	<%-- normal_menu 底部菜单  end --%>
	<div>
	</div>
</div>

<script type="text/javascript">
	/**加载控件数据开始 -start**/
	//该页面的编辑控件类型有四个，分别是normal_head，normal_link，normal_list，normal_menu
	var normal_head_field_values = $.parseJSON('<%=resultMap.get("normal_head")%>');
	var normal_link_filed_values = $.parseJSON('<%=resultMap.get("normal_link")%>');
	var normal_list_filed_values = $.parseJSON('<%=resultMap.get("normal_list")%>');
	var normal_menu_filed_values = $.parseJSON('<%=resultMap.get("normal_menu")%>');
	window.console&&window.console.log(normal_head_field_values);
	window.console&&window.console.log(normal_link_filed_values);
	window.console&&window.console.log(normal_list_filed_values);
	window.console&&window.console.log(normal_menu_filed_values);
	function plant_form_normal_head(){
		var items = normal_head_field_values.items;
		for(var i=0;i<items.length;i++){
			var imgurl = items[i].head_imgurl.match("wzgl/site/img/wzpic_b\\d.[a-zA-Z0-9]{3}")?
					items[i].head_imgurl.replace(".","_" + getJsLocaleMessage("wzgl","wzgl_site_lang") + "."):items[i].head_imgurl
			var item =  $("#normal_plant_form_hidden ul.normal_head").clone();
			$(item).find("input[name='head_title']").val(items[i].head_title);
			$(item).find("input[name='head_imgurl']").val(imgurl);
			$(item).find("img[name='head_img']").attr("src",imgurl);
			//类型
			$(item).find("select.changeTp").val(items[i].head_tp);
			$(item).find("input[name='tp"+items[i].head_tp+"_value']").val(items[i].head_tpvalue).parent().siblings(".tp_linked").hide().end().show();

			//类型值的备注
			var tp_note = $.trim(items[i].head_tpnote).substr(0,10);
			var $tp_note = $(item).find("input[name='tp"+items[i].head_tp+"_note']");
			$(item).find("input[name='tp"+items[i].head_tp+"_note']").val(tp_note);
			if("1"==items[i].head_tp){
				$tp_note.after(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_37") + "<a class='tpLinkNote' href='#'>"+ tp_note +"</a>");
			}else if("2"==items[i].head_tp){
				$tp_note.after(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_38") + "<a class='tpLinkNote' href='#'>"+ tp_note +"</a>");
			}
			
			$("#plant_form_normal_head .buttons").before(item);
		}
		resetName($("#plant_form_normal_head .buttons"));
		resetListStatus($("#plant_form_normal_head .addNoti"));
	}
	
	function plant_form_normal_link(){
		var items = normal_link_filed_values.items;
		for(var i=0;i<items.length;i++){
			var item =  $("#plant_form_normal_link");
			item.find("input[name='link_note']").val(items[i].link_note);
			item.find("input[name='link_bgcolor']").val(items[i].link_bgcolor);
			item.find("input[name='link_phone']").val(items[i].link_phone);
		}
	}
	
	function plant_form_normal_list(){
		var items = normal_list_filed_values.items;
		
		for(var i=0;i<items.length;i++){
			var item =  $("#normal_plant_form_hidden ul.normal_list").clone();
			if(item!=null&&item!=undefined){
				var imgurl = items[i].list_imgurl.match("wzgl/site/img/wzpic_b\\d.[a-zA-Z0-9]{3}")?
						items[i].list_imgurl.replace(".","_" + getJsLocaleMessage("wzgl","wzgl_site_lang") + "."):items[i].list_imgurl;
				
				$(item).find("input[name='list_title']").val(items[i].list_title);
				$(item).find("input[name='list_imgurl']").val(imgurl);
				$(item).find("img[name='list_img']").attr("src",imgurl);
				$(item).find("input[name='list_note']").val(items[i].list_note);
				$(item).find("input[name='list_tp']").val(items[i].list_tp);
				//类型
				$(item).find("select.changeTp").val(items[i].list_tp);
				$(item).find("input[name='tp"+items[i].list_tp+"_value']").val(items[i].list_tpvalue).parent().siblings(".tp_linked").hide().end().show();

				//类型值的备注
				var tp_note = $.trim(items[i].list_tpnote).substr(0,10);
				var $tp_note = $(item).find("input[name='tp"+items[i].list_tp+"_note']");
				$(item).find("input[name='tp"+items[i].list_tp+"_note']").val(tp_note);
				if("1"==items[i].list_tp){
					$tp_note.after(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_37") + "<a class='tpLinkNote' href='#'>"+ tp_note +"</a>");
				}else if("2"==items[i].list_tp){
					$tp_note.after(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_38") + "<a class='tpLinkNote' href='#'>"+ tp_note +"</a>");
				}
				
				$("#plant_form_normal_list .buttons").before(item);
			}
		}
		resetName($("#plant_form_normal_list .buttons"));
		resetListStatus($("#plant_form_normal_list .addNoti"));
	}

	function plant_form_normal_menu(){
		var items = normal_menu_filed_values.items;
		for(var i=0;i<items.length;i++){
			var item =  $("#normal_plant_form_hidden ul.normal_menu").clone();
			$(item).find("input[name='menu_title']").val(items[i].menu_title);
			$(item).find("input[name='menu_tp']").val(items[i].menu_tp);
			$(item).find("input[name='menu_fontcolor']").val(items[i].menu_fontcolor);
			$(item).find("input[name='menu_bgcolor']").val(items[i].menu_bgcolor);
			//类型
			$(item).find("select.changeTp").val(items[i].menu_tp);
			$(item).find("input[name='tp"+items[i].menu_tp+"_value']").val(items[i].menu_tpvalue).parent().siblings(".tp_linked").hide().end().show();

			//类型值的备注
			var tp_note = $.trim(items[i].menu_tpnote).substr(0,10);
			var $tp_note = $(item).find("input[name='tp"+items[i].menu_tp+"_note']");
			$(item).find("input[name='tp"+items[i].menu_tp+"_note']").val(tp_note);
			if("1"==items[i].menu_tp){
				$tp_note.after(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_37") + "<a class='tpLinkNote' href='#'>"+ tp_note +"</a>");
			}else if("2"==items[i].menu_tp){
				$tp_note.after(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_38") + "<a class='tpLinkNote' href='#'>"+ tp_note +"</a>");
			}
			
			$("#plant_form_normal_menu .buttons").before(item);
		}
		resetName($("#plant_form_normal_menu .buttons"));
		resetListStatus($("#plant_form_normal_menu .addNoti"));
	}
	
	//可编辑区域-重置表单标签名称
	function resetName(obj){
		$(obj).parents(".page_item_plant_view").find("ul.item").each(function(i){
			$(this).find('input,select,textarea').each(function(){
				$(this).attr('name',$(this).attr('name').replace(/\d+$/,'') + i);
			});
		});
	}
	
	//可编辑区域-重置"收起与展开"
	function resetListStatus(obj){
		$(obj).parent().siblings("ul.item").children('li.lv1').find('span.st').text(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_39"));
		$(obj).parent().siblings("ul.item").find("ul").hide();
		$(obj).parent().siblings("ul.item:last").find("ul").show();
		$(obj).parent().siblings("ul.item:last").children('li.lv1').find('span.st').text(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_40"));
	}
	
	//normal_head
	plant_form_normal_head();
	//normal_link
	plant_form_normal_link();
	//normal_list
	plant_form_normal_list();
	//normal_menu
	plant_form_normal_menu();
	/**加载控件数据开始-end**/
	
	//以下是颜色选择器
	$(function(){
		$('.colorpickerField').ColorPicker({
			onSubmit: function(hsb, hex, rgb, el) {
				$(el).val(hex);
				$(el).ColorPickerHide();
			},
			onBeforeShow: function () {
				$(this).ColorPickerSetColor(this.value);
			}
		})
		.bind('keyup', function(){
			$(this).ColorPickerSetColor(this.value);
		});
	});
</script>
