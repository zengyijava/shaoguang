//以用户坐标为中心创建点坐标
			//采集点的   坐标 
			
			//以用户坐标为中心创建点坐标
			var userlng = $("#userlng").val();
			var userlat = $("#userlat").val();
			var pioslng = $("#pioslng").val();
			var pioslat = $("#pioslat").val();
			var userpoint = new BMap.Point(userlng,userlat);
			var lbspoint = new BMap.Point(pioslng,pioslat);

			//百度地图API功能			以下type    qt 表示前台直接调用百度mapjs接口     jk表示后台请求http接口调用
			var map = new BMap.Map("allmap");
			map.centerAndZoom(lbspoint, 15); 
			map.enableScrollWheelZoom();                        //启用滚轮放大缩小
			map.addControl(new BMap.NavigationControl());  		//向地图添加一个平移缩放控件
			map.addControl(new BMap.ScaleControl());  			//一个比例尺控件
			map.addControl(new BMap.OverviewMapControl());  	//一个缩略图控件。
			//采集点 位置标注
			translateCallback = function (pt){
				// 初始化地图，设置中心点坐标和地图级别
				userpoint = new BMap.Point(pt.lng,pt.lat);
				addMarker(lbspoint);
			}
			//GCJ-02坐标转成百度坐标
			BMap.Convertor.translate(userpoint,2,translateCallback);  

			map.addEventListener("tilesloaded",function(){
				
			});



			var marker;
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~增加红色 小气球标注~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			function addMarker(pt){
				//增加气球的名称
				var title = $("#title").val();
				var address = $("#address").val();
				var telephone = $("#telephone").val();
				// 创建标注
				marker = new BMap.Marker(pt); 
				var label = new BMap.Label(title,{offset:new BMap.Size(20,-10)});
				marker.setLabel(label);
				//点小气球   增加出来的弹出框  
				var content = "<div class=\"map01_msg01\"><p>" + getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_33") +
						+title+"<br>"+getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_34")+telephone+"<br></p><table><tbody><tr><td>"
				+"<a class=\"map01_msg01_btn_bus\" href=\"#\" onclick=\"busRoute()\">"+getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_35")+"</a></td>"
				+"<td><a class=\"map01_msg01_btn_car\" href=\"#\" onclick=\"showRoute('','car')\">"+getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_36")+"</a></td></tr></tbody></table></div>"
				var opts = {
					 // width : 330,     			// 信息窗口宽度
					 // height: 170,     			// 信息窗口高度
					  title : title,			 // 信息窗口标题
					  enableMessage:false,		//设置允许信息窗发送短息
					  message:""
				}
				var infoWindow = new BMap.InfoWindow(content,opts);
				marker.addEventListener("click", function(e){
					this.openInfoWindow(infoWindow);
				});
				map.addOverlay(marker);  
			}

			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~点公交按纽处理
			//关闭所有div
			function closeAll(){
				$("#allmap").attr("style","display:none");
				$("#busplandiv").attr("style","display:none");
				$("#carplandiv").attr("style","display:none");
			}
				
			//获取乘车方案处理
			function busRoute(){
				marker.closeInfoWindow();
				closeAll();
				var transit = new BMap.TransitRoute(userpoint,{ policy:BMAP_TRANSIT_POLICY_LEAST_TIME});
				transit.setSearchCompleteCallback(function(results){
				  if(transit.getStatus() == BMAP_STATUS_SUCCESS){
				    //输出方案信息
				    var s = [];
				    for (i = 0; i < results.getNumPlans(); i ++){
					    if(i==5)break;
				    	var plan = results.getPlan(i);
				    	//返回方案总时间
				    	var shijian = plan.getDuration(true);
				    	//返回方案总距离
				    	var juli = plan.getDistance(true); 
				    	var chengchexiangxiname = "<li><i>"+getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_37")+(i + 1)  +
				    			"     </i>" +shijian +"/"+ juli+"<strong onclick=getxiangxi() >"+getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_38")+"</strong></li>";
						//返回方案描述文本
				        var xiangximiaoshu = plan.getDescription(false);
			            var click = "onclick=\"chengXiangxi('"+chengchexiangxiname+"','"+xiangximiaoshu+"',"+i+")\"";
					    s.push("<li "+click+" class=\"route01_list01_ul_li\"><i>"+getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_37")+(i + 1) + "    </i>"
					    		+shijian +"/"+ juli+"</li><div class=\"route01_list02\" style=\"display:none\" id=\"fangan01"+i+"\" ></div><div class=\"return-map01\" style=\"display:none\" id=\"fangan02"+i+"\"></div>");
				    }
				      document.getElementById("busplandiv_ul").innerHTML = s.join("<br/>");
				  }
				})
				transit.search(userpoint,lbspoint);
				$("#busplandiv").attr("style","display:block");
			}
			//详细方案安排
			function chengXiangxi(chengchexiangxiname,s,fangan_i){
				//展示详细方案
				var xiangxineirong = manageXianlu(s);
				document.getElementById("fangan01"+fangan_i).innerHTML = xiangxineirong;
				//展示地图链接
				var ditulianjie = "<a href=\"#\" onclick=\"showRoute("+fangan_i+",'bus')\">"+getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_39")+"</a>";
				document.getElementById("fangan02"+fangan_i).innerHTML = ditulianjie;
				fangan(fangan_i);
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
					if(i == 0){
						continue;
					}else if(i == 1){
						comment = comment + "<li class=\"route01_list02_in01\">"+ch[i]+"</li>";
					}
					else if(i == cLength - 1){
						comment = comment + "<li class=\"route01_list02_in03\">"+ch[i]+"</li>";
					}
					else{
						comment = comment + "<li class=\"route01_list02_in02\">"+ch[i]+"</li>";
					}
				}
				comment = comment + "<li class=\"route01_list02_ed01\">"+getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_50")+ "</li></ul>";
				return comment;
				
			}

			//详细方案显示
			function fangan(id){
				if($("#fangan01"+id).css("display") == 'none'){
					$("#fangan01"+id).attr("style","display:block");
				}else{
					$("#fangan01"+id).attr("style","display:none");
				}
				if($("#fangan02"+id).css("display") == 'none'){
					$("#fangan02"+id).attr("style","display:block");
				}else{
				    $("#fangan02"+id).attr("style","display:none");
				}
			}
			//显示路线     ---->   纬度   经度    第几方案   类型  	乘车bus或者自驾car
			function showRoute(plan,type){
				closeAll();
				 $("#allmap").attr("style","display:block");
				if(type == "bus"){
					var transit = new BMap.TransitRoute(userpoint,{renderOptions: {map: map, autoViewport:true}});
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
					  }
					})
					transit.search(userpoint,lbspoint);
				}else if(type == "car"){
					marker.closeInfoWindow();
					var driving = new BMap.DrivingRoute(map, {renderOptions:{map:map, autoViewport: true},policy:BMAP_DRIVING_POLICY_LEAST_TIME});
					driving.search(userpoint,lbspoint);
				}

				// 定义一个控件类,即function
				function ZoomControl(){
				  // 默认停靠位置和偏移量
				  this.defaultAnchor = BMAP_ANCHOR_BOTTOM_LEFT;
				  this.defaultOffset = new BMap.Size(10, 10);
				}
				// 通过JavaScript的prototype属性继承于BMap.Control
				ZoomControl.prototype = new BMap.Control();
				// 自定义控件必须实现自己的initialize方法,并且将控件的DOM元素返回
				// 在本方法中创建个div元素作为控件的容器,并将其添加到地图容器中
				ZoomControl.prototype.initialize = function(returnmap){
				  // 创建一个DOM元素
				  var div = document.createElement("div");
				  // 设置样式
				  div.style.cursor = "pointer";
				  // 绑定事件,点击一次放大两级
				  div.onclick = function(e){
						if(type == "bus"){
							 $("#busplandiv").attr("style","display:block");
							 $("#allmap").attr("style","display:none");
						}else if(type == "car"){
							 zijiafangan();
						}
				  }
				  div.innerHTML ="<img src=\"weix/common/img/fox.gif\" width=\"60px\" height=\"60px\" style=\"margin-bottom: 20px;\"/>";
				  // 添加DOM元素到地图中
				  returnmap.getContainer().appendChild(div);
				  // 将DOM元素返回
				  return div;
				}
				// 创建控件
				var myZoomCtrl = new ZoomControl();
				// 添加到地图当中
				map.addControl(myZoomCtrl);
			}

			//自驾方案
			function zijiafangan(){
				closeAll();
				//map.clearOverlays();
				var options = {onSearchComplete: function(results){
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
				      document.getElementById("carplan").innerHTML = s.join("<br/>");
				      document.getElementById("carinfo").innerHTML = "<strong>" +getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_45")+neirong+"</strong>";
				      document.getElementById("carlinkmap").innerHTML = "<a href=\"#\" onclick=\"carchangediv()\">" + getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_46")+ "</a>";
				    }
				  }
				};
				var driving = new BMap.DrivingRoute(map, options,{policy:BMAP_DRIVING_POLICY_LEAST_TIME});
				driving.search(userpoint,lbspoint);
				$("#carplandiv").attr("style","display:block");
			}
			//自驾方案
			function carchangediv(){
				 closeAll();
				 $("#allmap").attr("style","display:block");
				 $("#carplandiv").attr("style","display:none");
			}
			//点BUS还是 CAR  选项卡
			function changeDiv(type){
				map.clearOverlays();
				if(type == "bus"){
					busRoute();
				}else if(type == "car"){
					showRoute('','car')
				}
			}