define(['jquery','artDialog'],function($){
	var _plus=$('.group-plus'),
		tabContainer=$('.tabContainer');
	tabContainer.delegate(_plus,'click',function(e){
		e.preventDefault();
		
	})
});