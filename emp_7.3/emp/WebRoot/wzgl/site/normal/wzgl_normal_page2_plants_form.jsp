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

//Jsp页面中获取session中的语言设置
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);	

String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
//使用集群，文件服务器的地址
String filePath = GlobalMethods.getWeixFilePath();
HashMap<String,JSONObject> resultMap = (HashMap<String,JSONObject>)request.getAttribute("resultMap");
%> 
<link rel="stylesheet" type="text/css" href="<%=path%>/common/css/file.css?V=<%=StaticValue.getJspImpVersion() %>" />
<form id="plant_form_normal_tab" plantType="normal_tab" class="page_item_plant_view" style="display:none;">
	<div class="buttons" style="margin:10px 0 0 10px">
	     <input name="allowcount" value="3" type="hidden"/>
		 <a href="javascript:;" class="addNoti" id="add_normal_tab_img"><emp:message key="wzgl_qywx_site_text_33" defVal="新增一行"
											fileName="wzgl" /></a>
    </div>
</form>
<div id="normal_plant_form_hidden" style="display:none">
	<%-- normal_tab 表单 start --%>
	<ul class="item normal_tab">
	    <li class="lv1"><span class="st"><emp:message key="wzgl_qywx_site_text_37" defVal="收起"
											fileName="wzgl" /></span><span class="delNoti">X</span></li>
	    <li>
		    <ul>
				<li>
					<span class="tagName"><emp:message key="wzgl_qywx_site_text_52" defVal="页签名称："
											fileName="wzgl" /></span>
					<div class="input_item">
						<label>
							<input type="text" maxlength="5" class="bd_none" value="" name="tab_name">
						</label>
					</div>
				</li>
				<li>
					<span class="tagName"><emp:message key="wzgl_qywx_site_text_53" defVal="内容标题："
											fileName="wzgl" /></span>
					<div class="input_item">
						<label>
							<input type="text" maxlength="15" class="bd_none" value="" name="tab_title">
						</label>
					</div>
				</li>
				<li>
					<span class="tagName"><emp:message key="wzgl_qywx_site_text_54" defVal="图片："
											fileName="wzgl" /></span>
					<div class="input_item"> 
						<label>
							<img src="wzgl/site/img/wzpic_b1.png" class="tab_img" name="tab_img" id="tab_img"/>
							<input type="hidden" name="tab_imgurl" value="wzgl/site/img/wzpic_b1.png">
							
							<form id="uploadForm" name="uploadForm" action="" method="post" enctype="multipart/form-data">
								<div>
                                    <a href="javascript:;" class="file"><emp:message key='wxgl_button_14' defVal='浏览' fileName='wxgl'/>
								    	<input type="file" name="uploadFile" id="uploadFile" class="uploadFile">
									</a>
                                    <p id="filename"></p>
                                  </div>  
							</form>
						</label>
					</div>
				</li>
				<li>
					<span class="tagName"><emp:message key="wzgl_qywx_site_text_55" defVal="内容："
											fileName="wzgl" /></span>
					<div class="input_item"> 
						<label>
							<textarea name="tab_content" maxlength="512" style="margin: 0px; width: 185px; height: 137px;"></textarea>
							<span style="bottom:-15px;left:0;color:#656565;display: block;"><emp:message key="wzgl_qywx_site_text_56" defVal="最多输入512字符"
											fileName="wzgl" /></span>
						</label>
					</div>
				</li>
		    </ul>
	    </li>
	</ul>
	<%-- normal_tab 表单 end --%>
</div>
<script type="text/javascript">
	/**加载控件数据开始 -start**/
	//该页面的编辑控件类型有三个，分别是normal_tab
	var normal_tab_field_values = <%=resultMap.get("normal_tab")%>;
	window.console&&window.console.log(normal_tab_field_values);
	function plant_form_normal_tab(){
		var plantId = normal_tab_field_values.plantId;
		var count  = normal_tab_field_values.count;
		var items = normal_tab_field_values.items;
		for(var i=0;i<count;i++){
			//window.console.log(items[i]);
			var imgurl = items[i].tab_imgurl.match("wzgl/site/img/wzpic_b\\d.[a-zA-Z0-9]{3}")?
					items[i].tab_imgurl.replace(".","_" + getJsLocaleMessage("wzgl","wzgl_site_lang") + "."):items[i].tab_imgurl;
			var item =  $("#normal_plant_form_hidden ul.normal_tab").clone();
			$(item).find("input[name='tab_title']").val(items[i].tab_title);
			$(item).find("input[name='tab_imgurl']").val(imgurl);
			$(item).find("img[name='tab_img']").attr("src",imgurl);
			$(item).find("input[name='tab_name']").val(items[i].tab_name);
			$(item).find("textarea[name='tab_content']").val(items[i].tab_content);
			$("#plant_form_normal_tab .buttons").before(item);
		}
		resetName($("#plant_form_normal_tab .buttons"));
		resetListStatus($("#plant_form_normal_tab .addNoti"));
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
	
	//normal_tab
	plant_form_normal_tab();
	/**加载控件数据开始-end**/
	
	
	$(function(){
		//字符限制处理问题 
		$("textarea[maxlength]").live("blur",function() {
			checkAreaLen($(this));
		});
		$("textarea[maxlength]").live("keydown",function() {
			checkAreaLen($(this));
		});
		$("textarea[maxlength]").live("keyup",function() {
			checkAreaLen($(this));
		});
	});
	function checkAreaLen(obj){
		var area = obj;//$(this);
		var max = parseInt(area.attr("maxlength"), 10); // 获取maxlength的值
		if (max > 0) {
			if (area.val().length > max) { // textarea的文本长度大于maxlength
				area.val(area.val().substr(0, max)); // 截断textarea的文本重新赋值
			}
		}
	}
</script>