package com.montnets.emp.rms.rmsapi.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.rms.rmsapi.constant.RMSHttpConstant;
import com.montnets.emp.rms.rmsapi.model.TempParams;

public class EncodeUtil {

    /**
	 * 对字符串进行urlencode编码
	 * @author chenly		
	 * @param message
	 * @return
	 */
	public static String getUtf8Encode(String message)
	{
		String[] contents = message.split("&");
		String result = "";
		try
		{
			for (int i = 0; i < contents.length; i++)
			{
				String[] data = contents[i].split("=");
				if (i == 0)
				{
					result += data[0] + "=" +data[1];
				} else
				{
					result += "&" + data[0] + "=" + data[1];
				}
			}
			
			result = URLEncoder.encode(result, RMSHttpConstant.UTF8_ENCODE);
			
		} catch (UnsupportedEncodingException e)
		{
			EmpExecutionContext.error(e,"getUtf8Encode:UnsupportedEncodingException");
		}
		return result;
	}
	
	/**
	 * 对文件内容进行base64加密
	 * @author chenly
	 * @param message
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static List<TempParams>  base64Encode(List<TempParams> templist) throws UnsupportedEncodingException
	{
		TempParams tempParams=null;
		String base64Content=null;
		for(int i=0;i<templist.size();i++){
			tempParams=templist.get(i);
			
			base64Content=new String(Base64.encodeBase64(tempParams.getContentByte()));
			tempParams.setContent(base64Content);
			tempParams.setContentByte(null);
			templist.set(i,tempParams);
		}
		return templist;
	}

}
