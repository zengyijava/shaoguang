<%@ page language="java" import="java.util.List" pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.common.constant.SystemGlobals"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.entity.system.LfThiMenuControl"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));

	String aproinfo = (String) request.getAttribute("proInfo");
	Integer validday = (Integer) session.getAttribute("ValidDay");
	validday = validday == null ? 0 : validday;

	String skin = session.getAttribute("stlyeSkin") == null ? "default": (String) session.getAttribute("stlyeSkin");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head><%@ include file="/common/common.jsp"%>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<script type="text/javascript"
			src="<%=path%>/common/js/myjquery-a.js"></script>
		<link href="<%=iPath %>/css/index.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/index.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<style>
		.menuDiv {
	    	height: 192px;
	    }
		</style>
	</head>
	<body >
	<div id="bg_top_line">&nbsp;</div>
		<input type="hidden" id="isMiddel" value="0" />
			<div id="perCount" class="act_content">
			</div>
			<div id="contents" style="display: none">
					
			</div>
		<script type="text/javascript">
	    	var aproinfo = '<%=aproinfo%>';
	        var validday = <%=validday%>;
	        var iPath = "<%=iPath%>";
	        var path="<%=path%>";
	        var urlRouter={};
	        $(function(){
	        	 urlRouter={
					  modId:20,//在线客服模块ID
					  flag:1,//1表示开启跳转
					  userid:getField('#userid')||getLoginparams('#lguserid'),
					  tkn:getField('#appTkn')
				};
		        function getField(obj){
		        	return $(document).find(obj).val();
		        }
		        function getLoginparams(obj){
		        	var $pa = $(window.parent.document);
		        	var pahtm = $pa.find("#loginparams").children(obj).val();
		        	return pahtm;
		        }
	        });
	       
        </script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript">
		window.parent.complete();
		function openFirstMenu(modId){
            var secondMenuName = $(window.parent.document).find("#leftIframe").contents().find("#mod"+modId).find("p").eq(0).text();
            //如果存在快捷场景，则跳转到下一个二级菜单的第一个页面
            if(modId === 25 && secondMenuName === "我的快捷场景"){
                $(window.parent.document).find("#leftIframe").contents().find("#mod"+modId).find(".third-nav-menu").eq(1).find("li").eq(0).find("a").trigger("click");
			}else {
		        //保持原有逻辑
                $(window.parent.document).find("#leftIframe").contents().find("#mod"+modId).find(".third-nav-menu").eq(0).find("li").eq(0).find("a").trigger("click");
            }
		}

        function openServerUrl(userid,tkn,path){
            //在线坐席 对不兼容浏览器的处理
            if(!isBrowserOk()){
                return;
            }
            if(tkn){
                ajaxOnline(userid,tkn,path);
            }else{
                tkn=getField('#appTkn');
                ajaxOnline(userid,tkn,path);
            }

        }

        function ajaxOnline(userid,tkn,path){
            $.ajax({
                type:'GET',
                url:path+'/customChat.htm?method=checkUser',
                data:'userid='+userid+'&tkn='+tkn+'&isAsync=yes',
                success:function(data){
                    if(data == "outOfLogin")
                    {
                        location.href=path+"/common/logoutEmp.jsp";
                        return;
                    }

                    var data=eval('('+data+')');
                    if(data['aid']==0){
                        alert(data['msg']);
                        return false;
                    }else{
                        window.open(path+'/'+data['url']);
                    }
                }
            });
        }

        //判断浏览器版本是否可以运行在线客服
        function isBrowserOk(){
            var isIE = (document.all) ? true : false;
            var isIE6 = isIE && (navigator.appVersion.match(/MSIE 6.0/i)=="MSIE 6.0");
            var isIE7 = isIE && (navigator.appVersion.match(/MSIE 7.0/i)=="MSIE 7.0");
            var isIE8 = isIE && (navigator.appVersion.match(/MSIE 8.0/i)=="MSIE 8.0")&&(navigator.appVersion.search(/trident\/4\.0/i)>-1);
            //var isIE9 = isIE && (navigator.appVersion.match(/MSIE 9.0/i)=="MSIE 9.0");
            var is360 = isIE && ((navigator.userAgent).indexOf('360SE')>0);// Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1) 360SE
            //搜狗
            var userAgent = navigator.userAgent.toLowerCase();
            var isSG =  (userAgent.indexOf('se 2.x') != -1);// Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)SE 2.X
            if(isIE6||isIE7||isIE8||is360||isSG){
                /*您所使用的浏览器版本不支持此功能模块，请使用谷歌浏览器或IE9及以上版本！*/
                alert(getJsLocaleMessage("common","common_frame2_top_6"));
                return false;
            }
            return true;
        }

		$(document).ready(function() {
			if (aproinfo != null && aproinfo != "" && aproinfo == "over") 
			{
				alert(getJsLocaleMessage("common","common_frame2_index_1")+validday+getJsLocaleMessage("common","common_frame2_index_2"));
			}
			showImg();

			function showImg(){
				var userid=urlRouter.userid,tkn=urlRouter.tkn;
				$.ajax({ 
			        type : "post", 
			        url : path+'/thirdMenu.htm?method=getPageJson',
			        async : false,
			        success : function(data){ 
						if(data == "outOfLogin")
						{
							location.href=path+"/common/logoutEmp.jsp";
							return;
						}
						data=eval('('+data+')');
						var thirdMenuList=removeDuplicates(data['thirdMenuList']);
						var templateArr=[],menuList=[];
						var perCount=10,thirdMenuLength=thirdMenuList.length;
						templateArr.push('<div class="touchslider touchslider-demo">');
						for(var i=0;i<thirdMenuLength;i++){
							var menuNum=thirdMenuList[i].menuNum,
								title=thirdMenuList[i].title;
							if(i%perCount==0){
								templateArr.push('<div class="touchslider-item">');
							}
							templateArr.push("<div class=\"menuDiv\" style='margin-bottom:0;'>");
							//新增在线客服url跳转判断
							if(urlRouter['flag'] && urlRouter['modId']==menuNum){
                                var node = $(window.parent.parent.document).find("#loginparams");
                                var userid = node.find("#lguserid").val();
                                var tkn = node.find("#tkn").val();
								templateArr.push("<a href=\"javascript:void(0)\" onclick=\"openServerUrl(\'"+userid+"\',\'"+tkn+"\',\'"+path+"\')\" id=\"mod_style_"+menuNum+"\" class=\"act_btn\">");
							}else{
								templateArr.push("<a href=\"javascript:openFirstMenu("+menuNum+")\" id=\"mod_style_"+menuNum+"\" class=\"act_btn\">");
							}
							templateArr.push("<span>"+title+"</span>");
							templateArr.push("</a>");
							templateArr.push("</div>");
							if(i!=0 && i%perCount==(perCount-1)){
								templateArr.push('</div>');
							}
							if(urlRouter['flag'] && urlRouter['modId']==menuNum){
								menuList.push("<li><a href=\"javascript:void(0)\" onclick=\"openServerUrl(\'"+userid+"\',\'"+tkn+"\',\'"+path+"\')\" id=\"mod_style_"+menuNum+"\" class=\"act_btn\">"+title+"</a></li>");
							}else{
								menuList.push("<li><a href=\"javascript:void(0)\" onclick=\"openFirstMenu("+menuNum+")\" id=\"mod_style_"+menuNum+"\" class=\"act_btn\">"+title+"</a></li>");
							}
							
							
						}
						templateArr.push('</div>');
						templateArr.push('</div>');
					$('#perCount').html(templateArr.join(''));
					$("#uChildren").html(menuList.join(''));
						
			        } 
				});
				
			}
			
			
			
			function removeDuplicates(myArray){
				var length=myArray.length;
				var arr=[],temp={};
				for(var i=0;i<length;i++){
					var key=JSON.stringify(myArray[i].menuNum);
					var value=myArray[i];
					if(temp[key]=== undefined){
						arr.push(value);
						temp[key]=1;
					}else{
						temp[key]++;
					}
				}
				return arr;
			}
			
			
		});
        
        </script>
	</body>
</html>
