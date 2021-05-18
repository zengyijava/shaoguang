<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String lgcorpcode = request.getParameter("lgcorpcode");
	
	 String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
	<meta charset="utf-8">
	<%@include file="/common/common.jsp" %>
	<meta name="viewport" content="width=device-width,height=device-height,target-densitydpi=high-dpi,initial-scale=0.7,minimum-scale=0.5, maximum-scale=1.0, user-scalable=yes"/>  
	<title><emp:message key="wxgl_gzhgl_title_25" defVal="附近服务网点查询" fileName="wxgl"/></title>
	<link href="<%=iPath %>/css/cwc_servicestation.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/wxgl_<%=langName%>.js"></script>
	<script type="text/javascript" src="http://api.map.baidu.com/api?v=1.5&ak=23672900f1db3c4fad6d8ef17ec75cb3"></script>
	<script type="text/javascript" src="http://developer.baidu.com/map/jsdemo/demo/convertor.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<style type="text/css">
	
	#container{height:94%;width:100%;float:left;border-right:2px solid #bcbcbc;}
	#cheng{height:94%;width:100%;float:left;border-right:2px solid #bcbcbc;}
	#chemap{height:94%;width:100%;float:left;border-right:2px solid #bcbcbc;}
	#chemapf{height:94%;width:100%;float:left;border-right:2px solid #bcbcbc;}
	</style>
	<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
	</head>
	
	<body >
	<form id="stationForm" name="stationForm" method="post" action="searcheservicestation.php">
	<input name="to_x" id="to_x" type="hidden" value="22.542669" />
	<input name="to_y" id="to_y" type="hidden" value="113.935265" />
	
	<div style="line-height: 0px;height: 0px;width: 100%;z-index: 9999;display:none" id="notic">
	  <div style="line-height: 0px;height: 0px;margin:0px auto 0px auto; width:50%;">
	    <div style="position: absolute;width:50%;margin-top:200px; 100%;z-index: 9999;background-image: url(../images/20130626_bg03.png); line-height:30px;color:#fff; padding: 10px 10px 10px 10px; font-size:22px"><emp:message key="wxgl_gzhgl_title_26" defVal="抱歉，暂未查到适合您的服务网点，请更改您的产品或在微信中获取人工帮助" fileName="wxgl"/></div>
	  </div>
	</div>
	
	<div style="line-height: 0px;height: 0px;width: 100%;z-index: 9999;display:none" id="notic1" onclick="closeNotic1()">
	  <div style="line-height: 0px;height: 0px;margin:0px auto 0px auto; width:50%;">
	    <div style="position: absolute;width:50%;margin-top:200px; 100%;z-index: 9999;background-image: url(../images/20130626_bg03.png); line-height:30px;color:#fff; padding: 10px 10px 10px 10px; font-size:22px"><emp:message key="wxgl_gzhgl_title_27" defVal="抱歉，未查到适合您的乘车方案，请您选择其他方式出行或在微信中寻求人工帮助" fileName="wxgl"/></div>
	  </div>
	</div>
	
	</form>
	<%-- 服务站显示开始 --%>
	<div class="all" id="fuwu" >
	  <div class="map01">
	    <div class="map01_titlr01">
	      <div class="city" style="line-height: 50px;text-align: center;"><p id="city"><emp:message key="wxgl_gzhgl_title_28" defVal="深圳" fileName="wxgl"/></p></div>
	      <div class="model"><a href="cwc_stationService.hts?pl=101&x=22.542669&y=113.935265"><emp:message key="wxgl_gzhgl_title_29" defVal="Lenovo-笔记本" fileName="wxgl"/></a></div>
	    </div>
	    <div  id="nodata" style="line-height: 30px;background-color: #f1f1f1;padding: 0px 20px 0px 20px;display:none"><emp:message key="wxgl_gzhgl_title_26" defVal="抱歉，暂未查到适合您的服务网点，请更改您的产品或在微信中获取人工帮助" fileName="wxgl"/></div>
	    <div id="container"></div>
	  </div>
	  
	</div>
	<%-- 服务站显示结束 --%>
	
	<%-- 乘车方案开始  --%>
	<div class="all" id="chengfangan" style="display: block;">
	  <div class="route01">
	    <div class="route01_titlr01">
	      <div class="bus"><a href="#1" class="route01_titlr01_a01"><emp:message key="wxgl_gzhgl_title_30" defVal="公交" fileName="wxgl"/></a></div>
	      <div class="car"><a href="#1" onclick="zijiaDiv();closeNotic1()"><emp:message key="wxgl_gzhgl_title_31" defVal="驾车" fileName="wxgl"/></a></div>
	    </div>
	    <div class="route01_list01">
	      <ul id="chengfangan_ul"></ul>
	    </div>
	    <div class="logo"></div>
	  </div>
	</div>
	<%-- 乘车方案结束  --%>
	
	<%-- 乘车地图开始 --%>
	<div class="all" id="chengditu" style="display:none">
	  <div class="route01">
	 <div class="map01_titlr01">
	      <div class="city" style="line-height: 50px;text-align: center;"><p id="city"><emp:message key="wxgl_gzhgl_title_28" defVal="深圳" fileName="wxgl"/></p></div>
	      <div class="model"><a href="cwc_stationService.hts?pl=101&x=22.542669&y=113.935265"><emp:message key="wxgl_gzhgl_title_29" defVal="Lenovo-笔记本" fileName="wxgl"/></a></div>
	    </div>
	    
	  </div>
	  <div id="cheng"></div>
	</div>
	<%-- 乘车地图结束 --%>
	
	<%-- 自驾地图开始 --%>
	<div class="all" id="cheditu" style="display:none">
	  <div class="route01">
	 <div class="map01_titlr01">
	      <div class="city" style="line-height: 50px;text-align: center;"><p id="city"><emp:message key="wxgl_gzhgl_title_28" defVal="深圳" fileName="wxgl"/></p></div>
	      <div class="model"><a href="cwc_stationService.hts?pl=101&x=22.542669&y=113.935265"><emp:message key="wxgl_gzhgl_title_29" defVal="Lenovo-笔记本" fileName="wxgl"/></a></div>
	    </div>
	  </div>
	  <div id="chemap"></div>
	</div>
	<%-- 自驾地图结束 --%>
	
	<%-- 自驾方案开始 --%>
	<div class="all" id="chefangan" style="display:none">
	  <div class="route01">
	    <div class="route01_titlr01">
	      <div class="bus"><a href="#1" onclick="chengeDiv();closeNotic1()"><emp:message key="wxgl_gzhgl_title_30" defVal="公交" fileName="wxgl"/></a></div>
	      <div class="car"><a href="#1" class="route01_titlr01_a01"><emp:message key="wxgl_gzhgl_title_31" defVal="驾车" fileName="wxgl"/></a></div>
	    </div>
	    <div class="route01_list03" id="zijiafangan">
	      <div class="route01_list03_title01">
	        <p id = "chefanganneirong"></p>
	      </div>
	      <ul id="chexiangxifangan">
	
	      </ul>
	    </div>
	    <div class="return-map01" id="cheditulianjie"></div>
	  </div>
	  <div id="chemapf" style="display:none"></div>
	<div class="logo"></div>
	</div>
	<%-- 自驾方案结束 --%>
	
	<script type="text/javascript">
		//创建地图实例
		var map = new BMap.Map("container");
		//以用户坐标为中心创建点坐标
		var point = new BMap.Point(113.935265,22.542669);

		//获得用户所在城市
		function myFun(result){
		    var cityName = result.name;
		    //map.setCenter(cityName);
		    //alert(cityName);
		    //document.getElementById("city").innerHTML = "<strong>"+cityName+"</strong>";
		}
		var myCity = new BMap.LocalCity();
		myCity.get(myFun);

		//用户位置标注
		translateCallback = function (point1){
			// 初始化地图，设置中心点坐标和地图级别
			map.centerAndZoom(point1, 13);                 
			//用户位置
			var myIcon = new BMap.Icon("weix/common/img/lbs_ico_06.png", new BMap.Size(43,43));
			// 创建标注
			var marker2 = new BMap.Marker(point1,{icon:myIcon});  
			// 将标注添加到地图中
			map.addOverlay(marker2); 
			point=point1;
			//信息备注
			//var infoWindow2 = new BMap.InfoWindow("<p style='font-size:14px;'>哈哈，你看见我啦！我可不常出现哦！</p><p style='font-size:14px;'>赶快查看源代码，看看我是如何添加上来的！</p>");
			//marker2.addEventListener("click", function(){this.openInfoWindow(infoWindow2);});
		}

		//GCJ-02坐标转成百度坐标
		BMap.Convertor.translate(point,2,translateCallback);     

		//服务站点坐标标注
		function addMarker(point,content){

		  var myIcon1 = new BMap.Icon("weix/common/img/lbs_ico_03.png", new BMap.Size(36,55));
		  var marker = new BMap.Marker(point,{icon:myIcon1});  // 创建标注
		  map.addOverlay(marker);              // 将标注添加到地图中

		  var infoWindow2 = new BMap.InfoWindow(content);
		  infoWindow2.setTitle("联想服务中心") ;
		  marker.addEventListener("click", function(){
			  this.openInfoWindow(infoWindow2);
			  document.querySelector('.BMap_pop>img~img').style.display='none';
		  });
		}

	//php 调用服务站信息
    //document.getElementById("notic").style.display='block';
    var point1 = new BMap.Point(113.911227, 22.575974);
    //onclick="chengche(22.575974,113.911227)"
    var content = "<div class=\"map01_msg01\"><p>"+getJsLocaleMessage("wxgl","wxgl_qywx_jsp_text_5") +
    	 +"<a href='tel:0755-29994949' data-telnum='0755-29994949'>0755-29994949</a> <br>"+getJsLocaleMessage("wxgl","wxgl_qywx_jsp_text_7")+"9:00-18:00<br>"+
    	 + "</p><table><tbody><tr><td><a class=\"map01_msg01_btn_bus\" href=\"#\" onclick=\"chengche(22.575974,113.911227)\">"+
    	 getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_35") +"</a></td><td><a class=\"map01_msg01_btn_car\" href=\"#\" onclick=\"zijia(22.575974,113.911227)\">"+
    	 getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_36") +"</a></td></tr></tbody></table></div>"
    addMarker(point1,content);
    //document.getElementById("notic").style.display='block';
    var point1 = new BMap.Point(113.9333, 22.521484);
    //onclick="chengche(22.521484,113.9333)"
    var content = "<div class=\"map01_msg01\"><p>"+getJsLocaleMessage("wxgl","wxgl_qywx_jsp_text_6") +
       "<a href='tel:0755-26522866' data-telnum='0755-26522866'>0755-26522866</a> <br>"+
       +getJsLocaleMessage("wxgl","wxgl_qywx_jsp_text_7")+"9:00-18:00<br> </p><table><tbody><tr><td><a class=\"map01_msg01_btn_bus\" href=\"#\" onclick=\"chengche(22.521484,113.9333)\">"+
       + getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_35")+ "</a></td><td><a class=\"map01_msg01_btn_car\" href=\"#\" onclick=\"zijia(22.521484,113.9333)\">"+
       + getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_36")+"</a></td></tr></tbody></table></div>"
    addMarker(point1,content);
  	//向地图中添加缩放控件
	var ctrl_nav = new BMap.NavigationControl({anchor:BMAP_ANCHOR_TOP_RIGHT,type:BMAP_NAVIGATION_CONTROL_LARGE});
	map.addControl(ctrl_nav);
	//向地图中添加缩略图控件
	var ctrl_ove = new BMap.OverviewMapControl({anchor:BMAP_ANCHOR_TOP_RIGHT,isOpen:1});
	map.addControl(ctrl_ove);
	//向地图中添加比例尺控件
	//var ctrl_sca = new BMap.ScaleControl({anchor:BMAP_ANCHOR_BOTTOM_RIGHT});
	//map.addControl(ctrl_sca);
	
	//map.addControl(new BMap.GeolocationControl());   
	
	map.enableAutoResize();

	//关闭所有div
	function closeAll(){
		document.getElementById("fuwu").style.display='none';
		document.getElementById("chengfangan").style.display='none';
		document.getElementById("chengditu").style.display='none';
		document.getElementById("chefangan").style.display='none';
		document.getElementById("cheditu").style.display='none';
	}

	//处理乘车线路显示
	function manageXianlu(s){
		var reg=new RegExp(getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_40"),"g");
		var reg1=new RegExp(getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_41"),"g");
		s = s.replace(reg,getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_42"));
		s = s.replace(reg1,getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_43"));

		var ch = new Array;
		ch = s.split("--");
		var cLength = ch.length;
		
		var comment = "<ul><li class=\"route01_list02_op01\">"+getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_44")+"</li>";
		for(i=0;i<ch.length;i++){
		if(i == 0)
			continue;
			//comment = comment + "<li class=\"route01_list02_in01\">"+ch[i]+"</li>";
		else if(i == 1)
			comment = comment + "<li class=\"route01_list02_in01\">"+ch[i]+"</li>";
		else if(i == cLength - 1)
			comment = comment + "<li class=\"route01_list02_in03\">"+ch[i]+"</li>";
		else
			comment = comment + "<li class=\"route01_list02_in02\">"+ch[i]+"</li>";
		}
		comment = comment + "<li class=\"route01_list02_ed01\">"+getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_50")+"</li></ul>";
		return comment;
		
	}

	//详细方案安排
	function chengXiangxi(chengchexiangxiname,s,to_x,to_y,fangan_i){
		//展示详细方案
		var xiangxineirong = manageXianlu(s);
		document.getElementById("fangan01"+fangan_i).innerHTML = xiangxineirong;
		//展示地图链接
		var ditulianjie = "<a href=\"#\" onclick=\"chengchedituxianshi("+to_x+","+to_y+","+fangan_i+")\">"+getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_39")+"</a>";
		document.getElementById("fangan02"+fangan_i).innerHTML = ditulianjie;
		fangan(fangan_i);
	}

	//返回详细方案
	function getxiangxi(){
		closeAll();
		document.getElementById("chengfangan").style.display='block';
	}
	//详细方案显示
	function fangan(id){

		//alert("fangan01"+id);
		var f1 = document.getElementById("fangan01"+id).style.display;
		//alert(f1 == 'none');
		if(f1 == 'none'){
			document.getElementById("fangan01"+id).style.display = 'block';
		}else{
		    document.getElementById("fangan01"+id).style.display = 'none';
		}

		var f2 = document.getElementById("fangan02"+id).style.display;
		if(f2 == 'none'){
			document.getElementById("fangan02"+id).style.display = 'block';
		}else{
		    document.getElementById("fangan02"+id).style.display = 'none';
		}
	}
	
	//乘车方案处理
	function chengche(to_x,to_y){
		document.getElementById('to_x').Value = to_x;
		document.getElementById('to_y').Value = to_y;
		closeAll();
		
		var toPoint = new BMap.Point(to_y, to_x);
		var map_c = new BMap.Map("cheng");            // 创建Map实例
		map_c.centerAndZoom(point, 12);

		var transit = new BMap.TransitRoute(point,{ policy:BMAP_TRANSIT_POLICY_LEAST_TIME});
		transit.setSearchCompleteCallback(function(results){
		  if (transit.getStatus() == BMAP_STATUS_SUCCESS){
		    //输出方案信息
		    var s = [];
		    for (i = 0; i < results.getNumPlans(); i ++){
			    if(i==5)break;
		    	var plan = results.getPlan(i);
		    	var shijian = plan.getDuration(true);
		    	//var jingque = accDiv(shijian,3600);
		    	var juli    = plan.getDistance(true); 
		    	var chengchexiangxiname = "<li><i>"+getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_37")+(i + 1) + "    </i>" +shijian +"/"+ juli+"<strong onclick=getxiangxi() >"+getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_38")+"</strong></li>";
				//返回方案描述文本
		        var xiangximiaoshu = plan.getDescription(false);
	            var click = "onclick=\"chengXiangxi("+lng+","+lat+",'"+chengchexiangxiname+"','"+xiangximiaoshu+"',"+i+")\"";
			    s.push("<li "+click+" class=\"route01_list01_ul_li\"><i>"+getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_37") + (i + 1) + "    </i>" +shijian +"/"+ juli+"</li><div class=\"route01_list02\" style=\"display:none\" id=\"fangan01"+i+"\" ></div><div class=\"return-map01\" style=\"display:none\" id=\"fangan02"+i+"\"></div>");
		    }
		    document.getElementById("chengfangan_ul").innerHTML = s.join("<br/>");
		  }else{
			  document.getElementById("notic1").style.display='block';
		  }
		})
		transit.search(point, toPoint);
		document.getElementById("chengfangan").style.display='block';
	}

	//乘车地图显示
	function chengchedituxianshi(to_x,to_y,plan){
		closeAll();
		document.getElementById("chengditu").style.display='block';
		var toPoint = new BMap.Point(to_y, to_x);
		//var map_c = new BMap.Map("cheng");            // 创建Map实例
		//map_c.centerAndZoom(this.point, 12);
		var map_b = new BMap.Map("cheng");            // 创建Map实例
		map_b.centerAndZoom( point, 15);

		var transit = new BMap.TransitRoute(point,{renderOptions: {map: map_b, autoViewport:true}});
		transit.setSearchCompleteCallback(function(results){
		  if (transit.getStatus() == BMAP_STATUS_SUCCESS){
		    var firstPlan = results.getPlan(plan);
		    // 绘制步行线路
		    for (var i = 0; i < firstPlan.getNumRoutes(); i ++){
		      var walk = firstPlan.getRoute(i);
		      if (walk.getDistance(false) > 0){
		        // 步行线路有可能为0
		        map.addOverlay(new BMap.Polyline(walk.getPath(), {lineColor: "green"}));
		      }
		    }
		    // 绘制公交线路
		    for (i = 0; i < firstPlan.getNumLines(); i ++){
		      var line = firstPlan.getLine(i);
		      map.addOverlay(new BMap.Polyline(line.getPath()));
		    }
		  
		  }else{
			  document.getElementById("notic1").style.display='block';
		  }
		})
		transit.search(point, toPoint);

		// 定义一个控件类,即function
		function ZoomControl(){
		  // 默认停靠位置和偏移量
		  this.defaultAnchor = BMAP_NAVIGATION_CONTROL_PAN;
		  this.defaultOffset = new BMap.Size(10, 10);
		}

		// 通过JavaScript的prototype属性继承于BMap.Control
		ZoomControl.prototype = new BMap.Control();

		// 自定义控件必须实现自己的initialize方法,并且将控件的DOM元素返回
		// 在本方法中创建个div元素作为控件的容器,并将其添加到地图容器中
		ZoomControl.prototype.initialize = function(map){
		  // 创建一个DOM元素
		  var div = document.createElement("div");
		  // 添加文字说明
		  //div.appendChild(document.createTextNode("放大2级"));
		  // 设置样式
		  div.style.cursor = "pointer";
		 // div.style.border = "1px solid gray";
		  //div.style.backgroundColor = "white";
		  // 绑定事件,点击一次放大两级
		  div.onclick = function(e){
			  //zijiafangannew();
			  chengchegaibianDiv()
		  }

		  div.innerHTML ="<img src=\"weix/common/img/fox.gif\" width=\"60px\" height=\"60px\" style=\"margin-bottom: 20px;\"/>";
			 // div.appendChild(document.createTextNode("<img src=\"../images/fox.gif\" width=\"80px\" height=\"80px\" />"));
		  // 添加DOM元素到地图中
		  map.getContainer().appendChild(div);
		  // 将DOM元素返回
		  return div;
		}
		// 创建控件
		var myZoomCtrl = new ZoomControl();
		// 添加到地图当中
		map_b.addControl(myZoomCtrl);

		//向地图中添加缩放控件
		var ctrl_nav = new BMap.NavigationControl({anchor:BMAP_ANCHOR_TOP_RIGHT,type:BMAP_NAVIGATION_CONTROL_LARGE});
		map_b.addControl(ctrl_nav);
		//向地图中添加缩略图控件
		var ctrl_ove = new BMap.OverviewMapControl({anchor:BMAP_ANCHOR_TOP_RIGHT,isOpen:1});
		map_b.addControl(ctrl_ove);
		//向地图中添加比例尺控件
		//var ctrl_sca = new BMap.ScaleControl({anchor:BMAP_ANCHOR_BOTTOM_RIGHT});
		//map.addControl(ctrl_sca);
	}

	//自驾查询,显示地图
	function zijia(to_x,to_y){
		document.getElementById('to_x').Value = to_x;
		document.getElementById('to_y').Value = to_y;
	    closeAll();
	    document.getElementById("cheditu").style.display='block';
	    //document.getElementById("chemap").innerHTML='';
		var toPoint = new BMap.Point(to_y, to_x);
	    var map_c = new BMap.Map("chemap");            // 创建Map实例
		map_c.centerAndZoom(point, 12);

		var driving = new BMap.DrivingRoute(map_c, {renderOptions:{map: map_c, autoViewport: true},policy:BMAP_DRIVING_POLICY_LEAST_TIME});
		driving.search(point, toPoint);

		// 定义一个控件类,即function
		function ZoomControl(){
		  // 默认停靠位置和偏移量
		  this.defaultAnchor = BMAP_NAVIGATION_CONTROL_PAN;
		  this.defaultOffset = new BMap.Size(10, 10);
		}

		// 通过JavaScript的prototype属性继承于BMap.Control
		ZoomControl.prototype = new BMap.Control();

		// 自定义控件必须实现自己的initialize方法,并且将控件的DOM元素返回
		// 在本方法中创建个div元素作为控件的容器,并将其添加到地图容器中
		ZoomControl.prototype.initialize = function(map){
		  // 创建一个DOM元素
		  var div = document.createElement("div");
		  // 添加文字说明
		  //div.appendChild(document.createTextNode("放大2级"));
		  // 设置样式
		  div.style.cursor = "pointer";
		 // div.style.border = "1px solid gray";
		  //div.style.backgroundColor = "white";
		  // 绑定事件,点击一次放大两级
		  div.onclick = function(e){
			  //zijiafangannew();
			  zijiafangan(to_x,to_y);
		  }

		  div.innerHTML ="<img src=\"weix/common/img/fox.gif\" width=\"60px\" height=\"60px\" style=\"margin-bottom: 20px;\"/>";
			 // div.appendChild(document.createTextNode("<img src=\"../images/fox.gif\" width=\"80px\" height=\"80px\" />"));
		  // 添加DOM元素到地图中
		  map.getContainer().appendChild(div);
		  // 将DOM元素返回
		  return div;
		}
		// 创建控件
		var myZoomCtrl = new ZoomControl();
		// 添加到地图当中
		map_c.addControl(myZoomCtrl);

		//向地图中添加缩放控件
		var ctrl_nav = new BMap.NavigationControl({anchor:BMAP_ANCHOR_TOP_RIGHT,type:BMAP_NAVIGATION_CONTROL_LARGE});
		map_c.addControl(ctrl_nav);
		//向地图中添加缩略图控件
		var ctrl_ove = new BMap.OverviewMapControl({anchor:BMAP_ANCHOR_TOP_RIGHT,isOpen:1});
		map_c.addControl(ctrl_ove);
		//向地图中添加比例尺控件
		//var ctrl_sca = new BMap.ScaleControl({anchor:BMAP_ANCHOR_BOTTOM_RIGHT});
		//map_c.addControl(ctrl_sca);
		
	}

	//自驾方案
	function zijiafangan(to_x,to_y){
		closeAll();
		document.getElementById('to_x').Value = to_x;
		document.getElementById('to_y').Value = to_y;
		document.getElementById("chefangan").style.display='block';
		var map = new BMap.Map("chemapf");
		//map.centerAndZoom(this.point, 12);
		var toPoint = new BMap.Point(to_y, to_x);
		var options = {
		  onSearchComplete: function(results){
		    if (driving.getStatus() == BMAP_STATUS_SUCCESS){
		      // 获取第一条方案
		      var plan = results.getPlan(0);

		      // 获取方案的驾车线路
		      var route = plan.getRoute(0);

		      var juli = plan.getDistance(true);
		      var shijian = plan.getDuration(true); 

		      var neirong = juli+"/"+shijian;

		      // 获取每个关键步骤,并输出到页面
		      var s = [];
		      for (var i = 0; i < route.getNumSteps(); i ++){
		        var step = route.getStep(i);
		        s.push("<li> " + step.getDescription()+"</li>");
		      }
		      document.getElementById("chexiangxifangan").innerHTML = s.join("<br/>");
		      document.getElementById("chefanganneirong").innerHTML =  "<strong>"+getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_45")+neirong+"</strong>";
		      document.getElementById("cheditulianjie").innerHTML = "<a href=\"#\" onclick=\"zijiafangannew()\">"+getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_46")+"</a>";
		    }else{
				  document.getElementById("notic1").style.display='block';
			  }
		  }
		};
		var driving = new BMap.DrivingRoute(map, options,{policy:BMAP_DRIVING_POLICY_LEAST_TIME});
		driving.search(point, toPoint);
		
	}

	//乘车页面切换
	function chengeDiv(){
		var to_x = document.getElementById('to_x').Value;
		var to_y = document.getElementById('to_y').Value;
		chengche(to_x,to_y);
	}

	//乘车变换
	function chengchegaibianDiv(){
		document.getElementById("chengfangan").style.display='block';
		document.getElementById("chengditu").style.display='none';
	}

	//自驾方案
	function zijiafangannew(){
		closeAll();
		document.getElementById("cheditu").style.display='block';
		document.getElementById("chefangan").style.display='none';
	}

	//自驾页面切换
	function zijiaDiv(){
		var to_x = document.getElementById('to_x').Value;
		var to_y = document.getElementById('to_y').Value;
		zijia(to_x,to_y);
	}
	//关闭notice1
	function closeNotic1(){
		document.getElementById("notic1").style.display='none';
	}
	</script>
</html>
