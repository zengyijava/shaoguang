package com.montnets.emp.qyll.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.util.GetSxCount;
import com.montnets.emp.util.TxtFileUtil;

public class LltextFileUtil extends TxtFileUtil {

	public String[] getSaveFileUrl(Long lgUserId, String param[])
	        throws EMPException
	    {
	        try
	        {
	            Calendar c = Calendar.getInstance();
	            String uploadPath = "file/qyll/lldgtxt/";
	            String url[] = new String[6];
	            String saveName = "";
	            GetSxCount sx = GetSxCount.getInstance();
	            String sxcount = sx.getCount();
	            if(param[0] != null && !"".equals(param[0].trim()))
	                saveName = (new StringBuilder("1_")).append(lgUserId.toString()).append("_").append((new SimpleDateFormat("yyyyMMddHHmmssS")).format(c.getTime())).append("_").append(param[0]).append("_").append(sxcount).append(".txt").toString();
	            else
	                saveName = (new StringBuilder("1_")).append(lgUserId.toString()).append("_").append((new SimpleDateFormat("yyyyMMddHHmmssS")).format(c.getTime())).append("_").append(sxcount).append(".txt").toString();
	            String physicsUrl = getWebRoot();
	            String dirPath = createDir((new StringBuilder(String.valueOf(physicsUrl))).append(uploadPath).toString(), c);
	            physicsUrl = (new StringBuilder(String.valueOf(physicsUrl))).append(uploadPath).append(dirPath).append(saveName).toString();
	            String logicUrl = (new StringBuilder(String.valueOf(uploadPath))).append(dirPath).append(saveName).toString();
	            url[0] = physicsUrl;
	            url[1] = logicUrl;
	            url[2] = url[0].replace(".txt", "_bad.txt");
	            url[3] = url[0].replace(".txt", "_view.txt");
	            url[4] = url[1].replace(".txt", "_view.txt");
				// 新增无效文件相对路径
	            url[5] = url[1].replace(".txt", "_view.txt");
	            return url;
	        }
	        catch(Exception e)
	        {
	            EmpExecutionContext.error(e, "\u83B7\u53D6\u6587\u4EF6\u8DEF\u5F84\u5931\u8D25\u3002\u9519\u8BEF\u7801\uFF1AB20001");
	            throw new EMPException("B20001", e);
	        }
	    }
}
