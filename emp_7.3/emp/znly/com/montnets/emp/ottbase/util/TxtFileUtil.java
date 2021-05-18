package com.montnets.emp.ottbase.util;

import com.montnets.emp.common.constant.IErrorCode;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.ottbase.constant.OttException;
import com.montnets.emp.ottbase.constant.WXStaticValue;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 读写文件的工具类
 * 
 * @author Administrator
 */
public class TxtFileUtil
{
    private File  file   = null;

    /**
     * 以字符为单位读取文件
     * 
     * @param fileName
     * @return
     */
    public String readFileByChars(String fileName)
    {
        file = new File(fileName);
        StringBuffer fileContent = new StringBuffer();
        Reader reader = null;
        try
        {
            reader = new InputStreamReader(new FileInputStream(file));
            int tempchar;
            // 一次读一个字节
            while((tempchar = reader.read()) != -1)
            {

                fileContent.append((char) tempchar);

            }
            // 关闭流
            reader.close();
        }
        catch (Exception e)
        {
            // 异常信息打印
            EmpExecutionContext.error(e, "以字符为单位读取文件异常！");
        }
        finally
        {
            if(reader != null)
            {
                try
                {
                    // 关闭流
                    reader.close();
                }
                catch (IOException e1)
                {
                    // 异常信息打印
                    EmpExecutionContext.error(e1, "关闭流异常");

                }
            }
        }
        // 返回读取到的数据
        return fileContent.toString();
    }

    /**
     * 读取第一行数据
     * 
     * @param fileName
     * @return
     */
    public String readFileFirstLine(String fileName)
    {
        File filee = new File(fileName);
        String tempString = null;
        BufferedReader brr = null;
        try
        {
            brr = new BufferedReader(new FileReader(filee));
            tempString = brr.readLine();
            brr.close();
        }
        catch (IOException e)
        {
            // 异常信息打印
            EmpExecutionContext.error(e, "读取文件第一行数据异常！");
        }
        finally
        {
            if(brr != null)
            {
                try
                {
                    // 关闭流
                    brr.close();
                }
                catch (IOException ioe)
                {
                    EmpExecutionContext.error(ioe, "文件流关闭异常！");
                }
            }
        }
        // 返回数据
        return tempString;
    }

    /**
     * 指定编码格式写文件
     * 
     * @param srcFile
     * @param srcCode
     * @param distFile
     * @param distCode
     * @return
     * @throws Exception
     */

    public boolean writeToTxtFile(String srcFile, String srcCode, String distFile, String distCode) throws Exception
    {
        // 输出流
        Writer writer = null;
        InputStreamReader read = null;
        BufferedReader reader = null;
        try
        {
            File dist_File = new File(distFile);
            // 判断文件是否存在
            if(!dist_File.exists() != false)
            {
                boolean flag = dist_File.createNewFile();
                if (!flag) {
                    EmpExecutionContext.error("创建文件失败！");
                }
            }
            writer = new OutputStreamWriter(new FileOutputStream(dist_File), "GBK");
            File src_File = new File(srcFile);
            // 输入流
            read = new InputStreamReader(new FileInputStream(src_File));
            reader = new BufferedReader(read);
            String line;
            // 逐行读取
            while((line = reader.readLine()) != null)
            {
                // 写入文件内
                writer.write(line);
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "指定编码格式写文件异常！");
            throw e;
        }
        finally
        {
        	 if(writer != null){
	            // 关闭流
	            writer.close();
        	 }
            if(reader != null){
            	reader.close();
            }
            if(read != null){
            	read.close();
            }
        }
        // 返回结果
        return true;
    }

    /**
     * 将字符串内容写入文件
     * 
     * @param fileName
     *        文件路径
     * @param content
     *        内容
     * @return
     * @throws Exception
     */
    public boolean writeToTxtFile(String fileName, String content) throws OttException
    {
        FileOutputStream foss = null;
        try
        {
            File filee = new File(fileName);
            // 判断文件是否存在，不存在就新建一个
            if(!filee.exists() != false)
            {
                boolean flag = filee.createNewFile();
                if (!flag) {
                    EmpExecutionContext.error("删除文件失败！");
                }
            }

            // 输出流
            foss = new FileOutputStream(fileName, true);
            // 将要输入的信息转换成流
            ByteArrayInputStream baInput = new ByteArrayInputStream(content.getBytes("GBK"));

            byte[] buffer = new byte[8192];
            int ch = 0;
            while((ch = baInput.read(buffer)) != -1)
            {
                foss.write(buffer, 0, ch);
            }
            // 关闭流
            baInput.close();
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "将字符串内容写入文件异常！");
            throw new OttException(IErrorCode.B20002, e);
        }
        finally
        {
            if(foss != null)
            {
                // 关闭流
                try
                {
                    foss.close();
                }
                catch (IOException e)
                {
                	 EmpExecutionContext.error(e, "将字符串内容写入文件异常！");
                }
            }
        }
        return true;

    }

    /**
     * 转换流字符为大写
     * 
     * @param ips
     *        输入流
     * @param ops
     *        输出流
     */
    public static void transform(InputStream ips, OutputStream ops)
    {
        int ch = 0;
        try
        {
            while((ch = ips.read()) != -1)
            {
                int upperCh = Character.toUpperCase((char) ch);
                ops.write(upperCh);
            }

        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "转换流字符为大写异常！");
        }
    }

    /**
     * 创建目录
     * 
     * @param dirPath
     * @return
     */
    public boolean makeDir(String dirPath)
    {
        file = new File(dirPath);
        boolean isExist = file.exists();
        if(isExist != true)
        {
            if(file.mkdirs() == false)
            {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * 创建文件目录
     * 
     * @param type
     * @param fileName
     * @return
     * @throws Exception
     */
    public String[] createUrlAndDir(int type, String fileName) throws Exception
    {
        String[] urlValuesArray = new String[2];
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);

        String realUrl = getWebRoot();

        String dirUrl = realUrl + WXStaticValue.FILEDIRNAME;

        GetSxCount sx = GetSxCount.getInstance();

        String saveName = "0_" + fileName + "_" + (new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date()) + "_" + sx.getCount() + ".txt";

        String fileDirType = "";
        if(type == 0)
        {
            // dirUrl += StaticValue.IMSMSTXT;
            // fileDirType = StaticValue.IMSMSTXT;
        }
        else if(type == 1)
        {
            dirUrl += WXStaticValue.MANUALSMSTXT;
            fileDirType = WXStaticValue.MANUALSMSTXT;
        }
        else if(type == 2)
        {
            dirUrl += WXStaticValue.SERVICESMSTXT;
            fileDirType = WXStaticValue.SERVICESMSTXT;
        }
        else if(type == 3)
        {
            dirUrl += WXStaticValue.MOBILEBUSINESSTXT;
            fileDirType = WXStaticValue.MOBILEBUSINESSTXT;
        }

        dirUrl += year + "/" + month + "/" + day;

        makeDir(dirUrl);

        realUrl = dirUrl + "/" + saveName;
        urlValuesArray[0] = realUrl;

        String mobileUrl = WXStaticValue.FILEDIRNAME + fileDirType + year + "/" + month + "/" + day + "/" + saveName;
        urlValuesArray[1] = mobileUrl;

        return urlValuesArray;
    }

    /**
     * 创建文件目录
     * 
     * @param dirName
     * @param fileName
     * @return
     * @throws Exception
     */
    public String[] createUrlAndDir(String dirName, String fileName) throws Exception
    {
        String[] urlValuesArray = new String[2];
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);

        String realUrl = getWebRoot();

        String dirUrl = realUrl + WXStaticValue.FILEDIRNAME + dirName;

        String saveName = "0_" + fileName + "_" + (new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date()) + ".txt";

        String fileDirType = dirName;

        dirUrl += year + "/" + month + "/" + day;
        makeDir(dirUrl);

        realUrl = dirUrl + "/" + saveName;
        urlValuesArray[0] = realUrl;

        String mobileUrl = WXStaticValue.FILEDIRNAME + fileDirType + year + "/" + month + "/" + day + "/" + saveName;
        urlValuesArray[1] = mobileUrl;

        return urlValuesArray;
    }

    /**
     * 返回当前年月日
     * 
     * @param dirName
     * @param fileName
     * @return
     * @throws Exception
     */
    public String getCurNYR() throws Exception
    {

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);

        String dirUrl = "";

        dirUrl += year + "/" + month + "/" + day + "/";

        return dirUrl;
    }

    /**
     * 获取路径
     * 
     * @return
     */
    public String getWebRoot()
    {
        String realUrl = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();

        String newUrl = "";

        if(realUrl.contains("/WEB-INF/"))
        {
            newUrl = realUrl.substring(0, realUrl.lastIndexOf("WEB-INF/"));
        }

        realUrl = newUrl.replace("%20", " ");// 此路径不兼容jboss

        return realUrl;
    }

    /**
     * 返回文件全路径
     * 
     * @param url
     * @return
     */
    public String getPhysicsUrl(String url)
    {
        String physicsUrl = getWebRoot();
        physicsUrl = physicsUrl + url;
        return physicsUrl;
    }

    /**
     * 检查文件是否存在(公共方法)
     * 
     * @param url
     *        ：文件地址
     * @return
     */
    public boolean checkFile(String url)
    {

        url = getPhysicsUrl(url);
        File file = new File(url);

        if(file.isFile() && file.exists())
        {
            return true;
        }
        return false;
    }

    /**
     * 复制文件
     * 
     * @param oldPath
     * @param newPath
     */
    public void copyFile(String oldPath, String newPath)
    {
    	InputStream inStream = null;
    	FileOutputStream fs = null;
        try
        {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if(oldfile.exists())
            {
                inStream = new FileInputStream(oldPath);
                fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                // int length;
                while((byteread = inStream.read(buffer)) != -1)
                {
                    bytesum += byteread;
                    // System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "复制单个文件操作异常！");

        }finally{
        	if(fs != null){
        		try {
					fs.close();
				} catch (IOException e) {
					 EmpExecutionContext.error(e, "复制单个文件操作异常！关闭流异常");
				}
        	}
        	if(inStream != null){
        		try {
        			inStream.close();
				} catch (IOException e) {
					 EmpExecutionContext.error(e, "复制单个文件操作异常！关闭流异常");
				}
        	}
        }

    }

    /**
     * 记录短信发送网关的返回状态
     * 
     * @param taskid
     *        任务id ，传入0时为发送请求
     * @param content
     * @return
     */
    public boolean writeSendResult(long taskid, String result)
    {
        try
        {
            // 日历对象
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH) + 1;
            int day = c.get(Calendar.DAY_OF_MONTH);

            String dirPath = year + "/" + month + "/" + day + "/";
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String fileName = "smshttp";

            String filep = getWebRoot() + WXStaticValue.FILEDIRNAME + "manualsmstxt/" + dirPath + "smsresult/";
            String filePath = getWebRoot() + WXStaticValue.FILEDIRNAME + "manualsmstxt/" + dirPath + "smsresult/" + fileName + ".txt";

            String time = sd.format(new Date());
            String line = WXStaticValue.getSystemProperty().getProperty(WXStaticValue.LINE_SEPARATOR);
            // 默认记录接收请求
            String content = "reciveTime=" + time + line + "taskid=" + taskid + line + "resultCode=" + line + result + line;
            // 传入0时为发送请求
            if(taskid - 0 == 0)
            {
                content = "sendTime=" + time + line + "requestUrl=" + line + result + line + line;
            }
            File filee = new File(filep);
            if(!filee.exists())
            {
                filee.mkdirs();
            }
            writeToTxtFile(filePath, content);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "记录网关发送日志异常！");
            return false;
        }
        return true;
    }

    /**
     * 以行为单位读取文件
     * 
     * @param fileName
     *        文件路径名
     * @return
     */
    public String readFileByLines(String fileName)
    {
        String phoneStr = null;
        BufferedReader br = null;
        try
        {
            File file = new File(getWebRoot() + fileName);
            StringBuffer sb = new StringBuffer();
            br = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while((tempString = br.readLine()) != null)
            {
                // 将读取到的数据添加到stringBuffer
                sb.append(tempString.trim()).append(",");
            }
            // 截取字符串
            if(sb.lastIndexOf(",") != -1)
            {
                sb.deleteCharAt(sb.lastIndexOf(","));
            }
            phoneStr = sb.toString();
            sb.setLength(0);
        }
        catch (Exception e)
        {
            // 异常信息打印
            EmpExecutionContext.error(e, "以行为单位读取文件异常！");
        }
        finally
        {
            try
            {
                if(br != null)
                {
                    // 关闭流
                    br.close();
                }
            }
            catch (IOException ioe)
            {
                // 异常信息打印
                EmpExecutionContext.error(ioe, "关闭文本流异常！ ");
            }
        }
        // 返回读到的数据
        return phoneStr;
    }

    /**
     * delete file
     * 删除文件
     * 
     * @param fileName
     * @return
     */
    public boolean deleteFile(String fileName)
    {
        File file = new File(fileName);
        // 如果文件存在并类型是文件
        if(file.isFile() && file.exists())
        {
            // 删除并返回结果
            return file.delete();
        }
        return false;
    }

    /**
     * 创建年月日层级目录
     * 
     * @param physicsUrl
     *        绝对路径
     * @param c
     *        日历对象
     * @return
     */
    public String createDir(String physicsUrl, Calendar c)
    {
        // 文件操作类
        TxtFileUtil util = new TxtFileUtil();
        // 年
        int year = c.get(Calendar.YEAR);
        // 月
        int month = c.get(Calendar.MONTH) + 1;
        //
        int day = c.get(Calendar.DAY_OF_MONTH);

        String dirPath = year + "/" + month + "/" + day + "/";
        // 生成目录
        util.makeDir(physicsUrl + dirPath);
        return dirPath;
    }

    /**
     * 获取号码文件存放路径
     * 
     * @param id
     *        当前登录的操作员Id
     * @return 返回数组对应下标值：
     *         0:有效号码绝对路径
     *         1：有效号码相对路径
     *         2：无效号码绝对路径
     *         3:预览文件绝对路径
     *         4：预览文件相对路径
     * @throws OttException
     */
    public String[] getSaveUrl(Long id) throws OttException
    {
        try
        {
            // 获取日历对象
            Calendar c = Calendar.getInstance();
            // 文件存放目录默认
            String uploadPath = WXStaticValue.FILEDIRNAME + "manualsmstxt/";
            // 存放路径的数组
            String[] url = new String[5];
            String saveName = "";
            // 获取顺序号单例对象
            GetSxCount sx = GetSxCount.getInstance();
            String sxcount = sx.getCount();
            // 拼接文件名
            saveName = "1_" + id.toString() + "_" + (new SimpleDateFormat("yyyyMMddHHmmssS")).format(c.getTime()) + sxcount + ".txt";

            // 相对路径
            String logicUrl;
            // 绝对路径
            String physicsUrl = getWebRoot();
            // 构建年月日目录结构
            String dirPath = createDir(physicsUrl + uploadPath, c);
            physicsUrl = physicsUrl + uploadPath + dirPath + saveName;
            logicUrl = uploadPath + dirPath + saveName;
            url[0] = physicsUrl;
            url[1] = logicUrl;
            url[2] = url[0].replace(".txt", "_bad.txt");
            // 预览文件路径
            url[3] = url[0].replace(".txt", "_view.txt");
            url[4] = url[1].replace(".txt", "_view.txt");
            return url;
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "获取文件路径失败。错误码：" + IErrorCode.B20001);
            throw new OttException(IErrorCode.B20001, e);
        }

    }

    
    /**
     * 
     * @description    将信息写入文件     请求   回复 以及 http请求接口
     * @param type      request 接收      response 回复  http 请求url
     * @param result    内容
     * @return       	成功true  失败false		 
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-18 下午04:24:10
     */
    public boolean writeWeixMsgToFile(String type, String result)
    {
        try
        {
            String strNYR = "";
            try
            {
                strNYR = getCurNYR();
            }
            catch (Exception e)
            {
                EmpExecutionContext.error(e, "TxtFileUtilwriteRequestMessage.getCurNYR Is Error");
                return false;
            }
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String line = WXStaticValue.getSystemProperty().getProperty(WXStaticValue.LINE_SEPARATOR);
            String fileDirUrl = "";
            String filePath = "";
            String content = "";
            String webroot = getWebRoot();
            if("request".equals(type))
            {
                fileDirUrl = WXStaticValue.WEIX_REQUEST + strNYR;
                filePath = fileDirUrl + "weixrequest.txt";
                content = "request=" + sd.format(new Date()) + line + "getmessage=" + line + result + line;
            }
            else if("response".equals(type))
            {
                fileDirUrl = WXStaticValue.WEIX_RESPONSE + strNYR;
                filePath = fileDirUrl + "weixresponse.txt";
                content = "response=" + sd.format(new Date()) + line + "backmessage=" + line + result + line;
            }
            else if("http".equals(type))
            {
                fileDirUrl = WXStaticValue.WEIX_HTTPREQUEST + strNYR;
                filePath = fileDirUrl + "weixhttp.txt";
                content = "http=" + sd.format(new Date()) + line + "httpurl=" + line + result + line;
            }
            try
            {
                File file = new File(webroot + fileDirUrl);
                if(!file.exists())
                {
                    file.mkdirs();
                }
            }
            catch (Exception e)
            {
                EmpExecutionContext.error(e, "TxtFileUtilwriteRequestMessage.mkdirs() Is Error");
                return false;
            }
            // 默认记录接收请求
            /*File filee = new File(fileDirUrl);
            if(!filee.exists())
            {
                filee.mkdirs();
            }*/
            writeToTxtFile(webroot + filePath, content);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "TxtFileUtilwriteRequestMessage. is Error");
            return false;
        }
        return true;
    }

}
