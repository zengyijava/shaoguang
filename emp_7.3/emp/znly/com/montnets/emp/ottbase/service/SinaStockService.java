package com.montnets.emp.ottbase.service;

import com.montnets.emp.common.context.EmpExecutionContext;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 新浪股票接口调用方法
 * http://hq.sinajs.cn/list=sh601003
 * 
 * @description
 * @project p_wxgl
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author foyoto <foyoto@gmail.com>
 * @datetime 2014年6月26日 下午3:46:03
 */
public class SinaStockService
{
    // 创建HttpClient实例
    private final DefaultHttpClient    httpclient = new DefaultHttpClient();
    public static final String URL        = "http://hq.sinajs.cn/list=";

    /**
     * 获取单支股票的msgXml
     * 
     * @description
     * @param stockStr
     * @return
     * @author foyoto <foyoto@gmail.com>
     * @datetime 2014年6月26日 下午12:50:35
     */
    public String getSingleStockMsgXml(String stockStr)
    {
        // 待返回的消息文档
        String msgXml = "";
        // 股票的编号
        String stockId = "";
        stockStr = stockStr.replace(" ", "");
        if(stockStr == null || "".equals(stockStr))
        {
            return "";
        }
     
        // 合法性判断
        if(isValiable(stockStr))
        {
            stockId = getStockId(stockStr);
            String data = getRemoteSingleStockInfo(stockId);
            msgXml = generateSingleStockMsgXml(data);
        }
        return msgXml;
    }

    /**
     * 将请求的股票数据生成微信的文本xml格式
     * 
     * @description
     * @param data
     * @return
     * @author foyoto <foyoto@gmail.com>
     * @datetime 2014年6月26日 下午4:35:26
     */
    public String generateSingleStockMsgXml(String data)
    {
        String msgXml = "";
        if(data != null && !"".equals(data))
        {
            String regEx = "=\"(.*?)\"";
            Pattern pat = Pattern.compile(regEx);
            Matcher mat = pat.matcher(data);
            if(mat.find())
            {
                if(data.contains("hq_str_sh")||data.contains("hq_str_sz"))
                {
                    msgXml = generateShTextMsgXml(mat.group(1));
                }
            } 
        }
        return msgXml;
    }

    /**
     * 获取单支股票的信息
     * 
     * @description
     * @param stockStr
     * @return
     * @author foyoto <foyoto@gmail.com>
     * @datetime 2014年6月26日 上午11:37:03
     */
    public String getRemoteSingleStockInfo(String stockId)
    {
        String result = "";
        // 为空判断
        if(stockId == null || "".equals(stockId))
        {
            return "blank";
        }
        try
        {
            // 创建Get方法实例
            HttpGet httpgets = new HttpGet(URL + stockId);
            HttpResponse response = httpclient.execute(httpgets);
            HttpEntity entity = response.getEntity();
            if(entity != null)
            {
                InputStream instreams = entity.getContent();
                result = convertStreamToString(instreams);
                httpgets.abort();
            }
        }
        catch (Exception e)
        {
            result = "error";
            EmpExecutionContext.error("获取sina股票信息失败！" + stockId);
        }
        return result;
    }

    // 获取流数据
    private String convertStreamToString(InputStream is)
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try
        {
            while((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
        }
        catch (IOException e)
        {
            EmpExecutionContext.error(e, "发现异常");
        }
        finally
        {
            try
            {
                is.close();
            }
            catch (IOException e)
            {
                EmpExecutionContext.error(e, "发现异常");
            }
        }
        return sb.toString();
    }

    /**
     * 验证股票编号的合法性
     * 
     * @description
     * @param str
     * @return
     * @author foyoto <foyoto@gmail.com>
     * @datetime 2014年6月26日 上午11:37:23
     */
    private boolean isValiable(String stockStr)
    {
        String regEx = "\\d+{4,8}";
        Pattern pat = Pattern.compile(regEx);
        Matcher mat = pat.matcher(stockStr);
        boolean rs = mat.find();
        return rs;
    }

    /**
     * 获取股票的Id
     * 
     * @description
     * @param stockStr
     * @return
     * @author foyoto <foyoto@gmail.com>
     * @datetime 2014年6月26日 下午1:07:56
     */
    private String getStockId(String stockStr)
    {
        String stockId = "";
        String shregEx = "^([6|9]\\d+{3,8})";
        String szregEx = "^([0|2|3]\\d+{3,8})";
        Pattern shpat = Pattern.compile(shregEx);
        Pattern szpat = Pattern.compile(szregEx);
        Matcher shmat = shpat.matcher(stockStr);
        Matcher szmat = szpat.matcher(stockStr);
        if(shmat.find())
        {
            stockId = "sh" + shmat.group(1);
        }else if(szmat.find()){
            stockId = "sz" + szmat.group(1);
        }
        
        return stockId;
    }

    /**
     * 生成大盘指数
     * 
     * @description
     * @param data
     * @return
     * @author foyoto <foyoto@gmail.com>
     * @datetime 2014年6月26日 下午5:36:04
     */
    private String generateSshTextMsgXml(String data)
    {
        String msgXml = "";
        StringBuffer msgXmlStringBuffer = new StringBuffer();
        try
        {
            if(null != data && !"".equals(data))
            {
                String[] dataList = data.split(",");
                msgXmlStringBuffer.append("指数名称:").append(dataList[0]).append("\r\n");
                msgXmlStringBuffer.append("当前点数:").append(dataList[1]).append("\r\n");
                msgXmlStringBuffer.append("当前价格:").append(dataList[2]).append("\r\n");
                msgXmlStringBuffer.append("涨跌率:").append(dataList[3]).append("\r\n");
                msgXmlStringBuffer.append("成交量（手）:").append(dataList[4]).append("\r\n");
                msgXmlStringBuffer.append("成交额（万元）:").append(dataList[5]);
            }
            msgXml = generateTextReplyMsgXml(msgXmlStringBuffer.toString());
        }
        catch (Exception e)
        {
            EmpExecutionContext.error("生成msgxml格式数据出错。SinaStockService#generateSshTextMsgXml");
        }
        return msgXml;
    }

    /**
     * 生成股票信息数据
     * 
     * @description
     * @param data
     * @return
     * @author foyoto <foyoto@gmail.com>
     * @datetime 2014年6月26日 下午5:37:59
     */
    private String generateShTextMsgXml(String data)
    {
        String msgXml = "";
        StringBuffer msgXmlStringBuffer = new StringBuffer();
        try
        {
            if(null != data && !"".equals(data))
            {
                String[] dataList = data.split(",");
                msgXmlStringBuffer.append("股票名字:").append(dataList[0]).append("\r\n");
                msgXmlStringBuffer.append("今日开盘价:").append(dataList[1]).append("\r\n");
                msgXmlStringBuffer.append("昨日收盘价:").append(dataList[2]).append("\r\n");
                msgXmlStringBuffer.append("当前价格:").append(dataList[3]).append("\r\n");
                msgXmlStringBuffer.append("今日最高价:").append(dataList[4]).append("\r\n");
                msgXmlStringBuffer.append("今日最低价:").append(dataList[5]).append("\r\n");
                msgXmlStringBuffer.append("日期:").append(dataList[30]).append(dataList[31]);
            }
            msgXml = generateTextReplyMsgXml(msgXmlStringBuffer.toString());
        }
        catch (Exception e)
        {
            EmpExecutionContext.error("生成msgxml格式数据出错。SinaStockService#generateShTextMsgXml");
        }
        return msgXml;
    }

    /**
     * 生成文本回复格式的msgXML
     * 
     * @description
     * @param content
     * @return
     * @author foyoto <foyoto@gmail.com>
     * @datetime 2014年6月26日 下午5:53:16
     */
    public String generateTextReplyMsgXml(String content)
    {
        StringBuffer msgXml = new StringBuffer();
        msgXml.append("<xml>");
        msgXml.append("<ToUserName></ToUserName>");
        msgXml.append("<FromUserName></FromUserName>");
        msgXml.append("<CreateTime>" + String.valueOf(System.currentTimeMillis()) + "</CreateTime>");
        msgXml.append("<MsgType><![CDATA[text]]></MsgType>");
        msgXml.append("<Content><![CDATA[" + content + "]]></Content>");
        msgXml.append("</xml>");
        return msgXml.toString();
    }

    public static void main(String args[])
    {
        SinaStockService s = new SinaStockService();
        System.out.println(s.getSingleStockMsgXml("900901"));
        System.out.println(s.getSingleStockMsgXml("000001"));
    }
}
