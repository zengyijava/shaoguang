
var smsContentMaxLen = 990;
// 获取1到n-1条英文短信内容的长度
var entotalLen = 153;

function getSmsContentMaxLen(serId)
{
	$.post("eng_mtProcess.htm",{method : "getSmsContentMaxLen",serId : serId},
		function(infoStr){
			if(infoStr !="error" && infoStr.startWith("infos:"))
			{
				var infos = infoStr.replace("infos:","").split("&");
				var interGW = infos[3];
				var interGWs = interGW.split(",");
				entotalLen = interGWs[5];
				//签名前置
				if(interGWs[10] ==1)
				{
					entotalLen = entotalLen - interGWs[7];
				}
				smsContentMaxLen = infos[4];
			}
		}
	);
}

//定义startWith方法
String.prototype.startWith=function(str){
	if(str==null||str==""||this.length==0||str.length>this.length)
	return false;
	if(this.substr(0,str.length)==str)
	return true;
	else
	return false;
	return true;
}

//获取英文短信长度
function checkSmsContentLen(content, smsContentMaxLen)
{

	var isChinese = false;
	var enLen = 0;
	var charCode;
	var pattern = /(9[1-4]|12[3-6])/;
	var len = content.length;
	var enMsgShortLen = 0;
	for(var j=0; j<len; j++)
	{
		enMsgShortLen += 1;
		enLen += 1;
		charCode = content.charAt(j).charCodeAt();
		if(charCode > 127)
		{
			isChinese = true;
			break;
		}
		if(smsContentMaxLen == 700 && pattern.test(charCode))
		{
			//长短信边界值
			if(enLen % entotalLen == 0)
			{
				//条数加2
				enLen += 2;
			}
			else
			{
				enLen += 1;
			}
			enMsgShortLen += 1;
		}
	}
	if(smsContentMaxLen == 700)
	{
		if(isChinese && len > 350)
		{
			//alert("发送内容长度大于短信最大长度限制，最大长度限制为："+350);
			alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_fsnrcddydxzdcdxz")+350);
			return false;
		}
		else if(!isChinese && enLen > 700)
		{
			//alert("发送内容长度大于短信最大长度限制，最大长度限制为："+700);
			alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_fsnrcddydxzdcdxz")+700);
			return false;
		}
		return true;
	}
	return true;
}