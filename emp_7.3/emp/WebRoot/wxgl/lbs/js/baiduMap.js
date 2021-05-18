	
	var currentCity = "";
	// 百度地图API功能 以下type qt 表示前台直接调用百度mapjs接口 jk表示后台请求http接口调用
	var map = new BMap.Map("allmap");
	//解析器
	var geocoder = new BMap.Geocoder();
	map.enableScrollWheelZoom(); // 启用滚轮放大缩小
	map.addControl(new BMap.NavigationControl()); // 向地图添加一个平移缩放控件
	map.addControl(new BMap.ScaleControl()); // 一个比例尺控件
	map.addControl(new BMap.OverviewMapControl()); // 一个缩略图控件。
	//创建菜单   右键
	var contextMenu = new BMap.ContextMenu();
	var txtMenuItem = [
	  {
	   text:getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_16"),
	   callback:function(){map.zoomIn()}
	  },
	  {
	   text:getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_17"),
	   callback:function(){map.zoomOut()}
	  },
	  {
	   text:getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_18"),
	   callback:function(pt){
		  //增加标注
		  translatePios(pt,"addmenu");
	   }
	  }
	 ];
	 for(var i=0; i < txtMenuItem.length; i++){
		  contextMenu.addItem(new BMap.MenuItem(txtMenuItem[i].text,txtMenuItem[i].callback,100));
		  if(i != txtMenuItem.length - 1){
			  contextMenu.addSeparator();
		  }
	 }
	 map.addContextMenu(contextMenu);
	var pagetype = $("#pagetype").val();
	var zoom = 15;
	//处理编辑
	if(pagetype == "update"){
		//经度
		var lng = $("#lng").val();
		//纬度
		var lat = $("#lat").val();
		var point = new BMap.Point(lng,lat);
		map.centerAndZoom(point,zoom);
		//增加一个气球   具备一些事件GCJ-02坐标转成百度坐标
		translatePios(point,"search");
	}else{
		//处理新建   根据本地获取城市名称
		var myCity = new BMap.LocalCity();
		myCity.get(myFun);
		function myFun(result) {
			currentCity = result.name;
			map.centerAndZoom(currentCity, zoom); // 初始化地图,设置中心点坐标和地图级别。
		}
	}
	
	// map.addControl(new BMap.MapTypeControl()); //增 加地图 类型的
	// map.setCurrentCity(currentCity); // 仅当设置城市信息时，MapTypeControl的切换功能才能可用
	// var traffic = new BMap.TrafficLayer(); // 创建交通流量图层实例
	// map.addTileLayer(traffic); // 将图层添加到地图上
	map.addEventListener("tilesloaded", function() {
		// 当地图加载完毕后，button才置为有效
	});

	//将坐标转换
	function translatePios(pt,type){
		//translateCallback = function (e){
			//清除MAP上的所有覆盖物
		    map.clearOverlays();
			//增加一个气球   具备一些事件
			addPersonMarker(pt,type);
		//}
		//GCJ-02坐标转成百度坐标
		//BMap.Convertor.translate(pt,2,translateCallback); 
	}
	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~增加红色 小气球标注~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//增加小气球
	function addPersonMarker(pt,type){
		//创建小气球
		var marker = new BMap.Marker(pt);
		//设置为拖动
		marker.enableDragging();
		marker.addEventListener("click", function(e){
				map.removeOverlay(marker.getLabel());
				geocoder.getLocation(e.point, function(rs){
		    	if(rs == null){
		    		alert(getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_19"));
		    		return;
		    	}
		    	var a = $("#address").val();
		    	if(a.length > 0 && a != rs.address){
					var msg = getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_20")+rs.address+
					getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_21")+ a +
					getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_22");
					
					if(confirm(msg)) {
						$("#address").val("");
						$("#address").val(rs.address);
					}
					/*art.dialog.confirm(msg, function(){
						
					});*/
				}else{
						$("#address").val("");
						$("#address").val(rs.address);
				}
				$("#lng").val(rs.point.lng);
				$("#lat").val(rs.point.lat);
			  });
		});
		//开始拖动气球触发
		marker.addEventListener("dragstart", function(e1){
			 map.removeOverlay(marker.getLabel());
		});
		//将气球拖动
		marker.addEventListener("dragend", function(e1){
			  map.removeOverlay(marker.getLabel());
			  geocoder.getLocation(e1.point, function(rs){
		    	if(rs == null){
		    		alert(getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_19"));
		    		return;
		    	}
		    	var a = $("#address").val();
		    	if(a.length > 0 && a != rs.address){
		    		var msg = getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_20")+rs.address+
					getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_21")+a+
					getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_22");
		    		
		    		if(confirm(msg)) {
		    			$("#address").val("");
						$("#address").val(rs.address);
		    		}
//					art.dialog.confirm(msg, function(){
//						
//					});
				}else{
						$("#address").val("");
						$("#address").val(rs.address);
				}
				$("#lng").val(rs.point.lng);
				$("#lat").val(rs.point.lat);
			  });
		});
		//当鼠标放在气球上面显示名称
		marker.addEventListener("mouseover", function(e2){
			  geocoder.getLocation(e2.point, function(rs){
		    	if(rs == null){
		    		alert(getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_19"));
		    		return;
		    	}
		    	map.removeOverlay(marker.getLabel());
		    	var label = new BMap.Label(rs.address,{offset:new BMap.Size(20,-10)});
				marker.setLabel(label);
			  });
		});
		//当鼠标离开 气球触发事件
		marker.addEventListener("mouseout", function(e3){
			map.removeOverlay(marker.getLabel());
		});
		//处理菜单操作添加的 ，区别于查询操作   将经度 纬度 设置进去
		if(type == "addmenu"){
			$("#lng").val(pt.lng);
			$("#lat").val(pt.lat);
		}
		
		map.addOverlay(marker);  
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~根据经度纬度解析地址~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//页面击按钮进入请求
	function searchAddressbylnglat(){
	    var lng = $.trim($("#lng").val());
	    var lat = $.trim($("#lat").val());
	    if(lat == null || lat.length == 0){
	    	 alert(getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_23"));
			return;
		}
	    if(lng == null || lng.length == 0){
	    	 alert(getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_24"));
			return;
		}
		var pt = new BMap.Point(lng,lat);
		map.centerAndZoom(pt, zoom);
	    //增加一个气球   具备一些事件
		translatePios(pt,"search");
	} 
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~根据物理地址获取经度纬度~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    function addressResolution(){
	    var pathUrl = $("#pathUrl").val();
	    var mapAk = $("#mapAk").val();
	    var address = $("#address").val();
	    if(address == null || address.length == 0){
	    	 alert(getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_25"));
			return;
		}
	     var SAMPLE_POST_REVERSE = 'http://api.map.baidu.com/geocoder/v2/?ak='+mapAk+'&callback=renderOption';
         var httpurl = SAMPLE_POST_REVERSE + "&address="+encodeURIComponent(address)+"&output=json";
	     $("#addressResolution").attr("src","");
	     $("#addressResolution").attr("src",httpurl);
	} 
	//请求的回调函数
    function renderOption(response) {
		if (response.status != 0) {
			 alert(getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_26")+response.status);
			return;
		}
		//获取其经度纬度
    	var location = response.result.location;
    	var lng = location.lng;
    	var lat = location.lat;
    	var pt = new BMap.Point(lng,lat);
		map.centerAndZoom(pt, zoom);
    	//增加一个气球   具备一些事件
		translatePios(pt,"search");
		return;
    }
    
    //根据物理地址获取其地图上位置
    function getphyaddress(){
//    	var pathUrl = $("#pathUrl").val();
//	    var mapAk = $("#mapAk").val();
	    var address = $("#address").val();
	    if(address == null || address.length == 0){
	    	 alert(getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_25"));
			return;
		}
		 // 创建地址解析器实例
	   	 //var myGeo = new BMap.Geocoder();
	   	 // 将地址解析结果显示在地图上,并调整地图视野
//	   	 myGeo.getPoint(address, function(point){
//	   	   if (point) {
//	   	     map.centerAndZoom(point, zoom);
//	   	    // map.addOverlay(new BMap.Marker(point));
//	   	     	translatePios(point,"search");
//	   	   }
//	   	 },'');
	   	 
	   	 var options = {
	   		 renderOptions:{map: map},
	   		 onMarkersSet: function(results){
	   			 map.clearOverlays();
	   			 for (var i = 0; i < results.length; i ++){
	   				var point = new BMap.Point(results[i].point.lng,results[i].point.lat);
	   				//增加一个气球   具备一些事件GCJ-02坐标转成百度坐标
	   				addPersonMarker(point,"search");
		   		 }
	   		 } 
	   		};  
	   	var local = new BMap.LocalSearch(map,options);
	   	local.search(address);
    }
    
	
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    