// js 创建XMLHttpRequest 对象
function createXMLHttpRequest()
{
    var XMLHttpReq;
    try
    {
        XMLHttpReq = new ActiveXObject("Msxml2.XMLHTTP");//IE高版本创建XMLHTTP
    }
    catch(E)
    {
        try
        {
            XMLHttpReq = new ActiveXObject("Microsoft.XMLHTTP");//IE低版本创建XMLHTTP
        }
        catch(E)
        {
            try
            {
                XMLHttpReq = new XMLHttpRequest();//兼容非IE浏览器，直接创建XMLHTTP对象
            }
            catch(E)
            {
                alert("你的浏览器不支持XMLHTTP对象，请升级到IE6以上版本！");
                XMLHttpReq = false;
            }
        }
    }
    return XMLHttpReq;
}
//返回最顶级window
function topWindow(){
    var maxdeep = 5;
    var parentWindow = window;
    //window.frameElement == null 或 window.parent = window 判断顶级
    while(parentWindow !== parentWindow.parent && maxdeep--){
        parentWindow = parentWindow.parent;
    }
    return parentWindow;
}

function imgfix(){
    var path = topWindow().emppatch || '/emp';
    var e = window.event || arguments.callee.caller.arguments[0];
    var ele = e.srcElement || e.target;
    var src = ele.src;
    // 获取XMLHttpRequest对象
    var XMLHttpReq = createXMLHttpRequest();

    XMLHttpReq.open("POST",path+"/loginImg.login",true);
    XMLHttpReq.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
    // 发送参数
    XMLHttpReq.send("method=imgfix&url="+src);
    // 调用回调函数
    XMLHttpReq.onreadystatechange = function(){
        ele.onerror = null;
        if (XMLHttpReq.readyState==4)
        {
            // 200表示访问成功
            if (XMLHttpReq.status==200)
            {
                // 获取相应文本
                var res = XMLHttpReq.responseText;
                if(res.length > 0)
                {
                    ele.src = res;
                }
            }
            XMLHttpReq = null;
        }
    };
}