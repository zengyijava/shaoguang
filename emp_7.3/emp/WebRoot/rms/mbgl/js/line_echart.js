echartLine = {
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
        x: 40,
        x2: 40,
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
            boundaryGap : false,
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
            type:'line',
            data:''
        },
        {
            name:'',
            type:'line',
            data:''
        },
        {
            name:'',
            type:'line',
            data:''
        }
    ]
};
$(function(){
 	$("#echartLine").css({"height":355,"width":378});
	echartLine_chart = echarts.init(document.getElementById('echartLine'));  
    echartLine_chart.setOption(echartLine, true);   //为echarts对象加载数据
});
                    