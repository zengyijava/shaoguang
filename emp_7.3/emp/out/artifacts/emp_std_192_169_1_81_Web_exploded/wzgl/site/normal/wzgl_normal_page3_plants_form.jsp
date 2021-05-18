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

//Jsp页面中获取session中的语言设置
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);	

//使用集群，文件服务器的地址
String filePath = GlobalMethods.getWeixFilePath();
HashMap<String,JSONObject> resultMap = (HashMap<String,JSONObject>)request.getAttribute("resultMap");
%> 
<link rel="stylesheet" type="text/css" href="<%=path%>/common/css/file.css?V=<%=StaticValue.getJspImpVersion() %>" />
<form id="plant_form_normal_content" plantType="normal_content" class="page_item_plant_view" style="display:none;" name="uploadSpecialForm" action="" method="post" enctype="multipart/form-data">
    <ul class="item normal_content">
		<li>
			<span style="float:left;"><emp:message key="wzgl_qywx_site_text_38" defVal="标题："
											fileName="wzgl" /></span>
			<div class="input_item">
				<label>
					<input type="text" maxlength="15" class="bd_none" value="" name="content_title">
				</label>
			</div>
		</li>
		<li>
			<span style="float:left;"><emp:message key="wzgl_qywx_site_text_54" defVal="图片："
											fileName="wzgl" /></span>
			<div class="input_item" style="height:50px;"> 
				<label>
					<img src="" class="tab_img" name="content_img" id="content_img"/>
					<input type="hidden" name="content_imgurl" value="">
						<div>
                           <a href="javascript:;" class="file"><emp:message key='wxgl_button_14' defVal='浏览' fileName='wxgl'/>
								<input type="file" name="uploadSpecialFile" id="uploadSpecialFile" class="uploadFile">
							</a>
                         </div> 					
				</label>
			</div>
		</li>
		<li>
			<span style="float:left;"><emp:message key="wzgl_qywx_site_text_55" defVal="内容："
											fileName="wzgl" /></span>
			<div class="input_item"> 
				<label>
					<textarea  name="content_body" maxlength="512" class="msgText" style="margin: 0px; width: 185px; height:137px;"></textarea>
					<span style="bottom:-15px;left:0;color:#656565;display: block;"><emp:message key="wzgl_qywx_site_text_56" defVal="最多输入512字符"
											fileName="wzgl" /></span>
				</label>
			</div>
		</li>
    </ul>
</form>

<script type="text/javascript">
	//取值
	var normal_content_field_values = <%=resultMap.get("normal_content")%>;
	window.console&&window.console.log(normal_content_field_values);
	function plant_form_normal_content(){
		var plantId = normal_content_field_values.plantId;
		var count  = normal_content_field_values.count;
		var items = normal_content_field_values.items;	
		
		for(var i=0;i<count;i++){
			var item =  $("#plant_form_normal_content");
			var imgurl = items[i].content_imgurl.match("wzgl/site/img/wzpic_b\\d.[a-zA-Z0-9]{3}")?
					items[i].content_imgurl.replace(".","_" + getJsLocaleMessage("wzgl","wzgl_site_lang") + "."):items[i].content_imgurl;
			item.find("input[name='content_title']").val(items[i].content_title);
			item.find("textarea[name='content_body']").val(items[i].content_body);
			item.find("input[name='content_imgurl']").val(imgurl);
			item.find("img[name='content_img']").attr("src",imgurl);
		}
	}
	//赋值
	plant_form_normal_content();
	
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
