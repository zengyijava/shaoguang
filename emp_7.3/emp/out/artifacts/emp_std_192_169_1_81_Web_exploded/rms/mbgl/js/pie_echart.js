echartPie = {
  	title : {
        text: '',
        x:'center',
        textStyle : {
            color:'#333333' ,
            fontSize : 13   //文字的字体大小
        }
    },
    tooltip : {
      	show:true,
        formatter: "{b} : {c} ({d}%)"
    },
    legend: {
        orient : 'horizontal',
        top : 250,
        itemWidth: 15,
        itemHeight: 10,
        padding:5,
        itemGap : 4,
        data:'',
        textStyle :{
          fontSize:11
        }
    },
    calculable : false,
    series : [
        {
            name:'',
            type:'pie',
            radius : '60%',
            center: ['50%', '40%'],
            animation: false,
           	label:{            //饼图图形上的文本标签
	            normal:{
	                show:true,
	                position:'inner', //标签的位置
	                textStyle : {
	                    fontWeight : 300,
	                    fontSize : 13   //文字的字体大小
	                },
	                formatter:
	                	function (params) {                         
                        	return (params.percent - 0).toFixed(0) + '%'
                    	}
	            	}
	        	},
           	data:''
        }
    ]
};
$(function(){

 	$("#echartPie").css({"height":365,"width":308});
	echartPie_charts = echarts.init(document.getElementById('echartPie'));  
    echartPie_charts.setOption(echartPie, true);   //为echarts对象加载数据
});
