$(document).ready(function(){
	var browser=browserVal();
		$('#printPage').show();
})

function printPage(){
		window.print();
}

function browserVal(){
	var userAgent=navigator.userAgent.toLowerCase();
	var browser={
		version:(userAgent.match(/(?:firefox|opera|safari|chrome|msie)[\/: ]([\d.]+)/))[1],
        safari:/version.+safari/.test(userAgent),
        chrome:/chrome/.test(userAgent),
        firefox:/firefox/.test(userAgent),
        ie:/msie/.test(userAgent),
        opera: /opera/.test(userAgent )
	};
	return browser;
}