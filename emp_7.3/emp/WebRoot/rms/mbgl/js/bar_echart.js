echartBar = {
	title : {
        text: '',
        x:'center',
        textStyle : {
            color:'#333333' ,
            fontSize : 13   //文字的字体大小
        }
    },
    tooltip : {
        trigger: 'axis'
    },
    grid: {
        borderWidth: 0,
        x: 30,
        x2: 30,
        y: 50,
        y2: 120
    },
    legend: {
        orient : 'horizontal',
        itemWidth: 15,
        itemHeight: 10,
        top : 280,
        padding:5,
        itemGap : 1,
        data:'',
        textStyle :{
          fontSize:11
        }
    },
    calculable : true,
    xAxis : [
        {
            type : 'category',
            axisLabel: {  
         	   rotate:30 ,
               textStyle :{
               	fontSize:10
               }
         	},
            data : ''
        }
    ],
    yAxis : [
        {
            type : 'value'
        }
    ],
    series : [
        {
            name:'',
            type:'bar',
            itemStyle: {
	                  normal: {  
	                  		//color : '#37d39a',
	                      	barBorderRadius:0,
	                      	label : {
	                          	show: true, 
	                          	position: 'right',
	                          	textStyle: {
	                              	color:'#fff',
	                              	fontSize:10,
	                              	fontFamily : "微软雅黑"
	                          	}
	                      	}
	                  	}
	               	},
	            barWidth:10,
	            data:''
        },
        {
            name:'',
            type:'bar',
            itemStyle: {
	                  normal: {  
	                  		//color : '#37d39a',
	                      	barBorderRadius:0,
	                      	label : {
	                          	show: true, 
	                          	position: 'right',
	                          	textStyle: {
	                              	color:'#fff',
	                              	fontSize:10,
	                              	fontFamily : "微软雅黑"
	                          	}
	                      	}
	                  	}
	               	},
	            barWidth:10,
	            data:''
        },
        {
            name:'',
            type:'bar',
            itemStyle: {
	                  normal: {  
	                  		//color : '#37d39a',
	                      	barBorderRadius:0,
	                      	label : {
	                          	show: true, 
	                          	position: 'right',
	                          	textStyle: {
	                              	color:'#fff',
	                              	fontSize:10,
	                              	fontFamily : "微软雅黑"
	                          	}
	                      	}
	                  	}
	               	},
	            barWidth:10,
	            data:''
        },
        {
            name:'',
            type:'bar',
            itemStyle: {
	                  normal: {  
	                  		//color : '#37d39a',
	                      	barBorderRadius:0,
	                      	label : {
	                          	show: true, 
	                          	position: 'right',
	                          	textStyle: {
	                              	color:'#fff',
	                              	fontSize:10,
	                              	fontFamily : "微软雅黑"
	                          	}
	                      	}
	                  	}
	               	},
	            barWidth:10,
	            data:''
        }
    ]
};
$(function(){
 	$("#echartBar").css({"height":355,"width":378});
	echartBar_chart = echarts.init(document.getElementById('echartBar'));  
    echartBar_chart.setOption(echartBar, true);   //为echarts对象加载数据
});
                    