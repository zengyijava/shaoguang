package com.montnets.emp.util;

import com.montnets.EMPException;
import com.montnets.emp.common.context.EmpExecutionContext;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
 * CSV格式文件导出下载工具类
 * @date 20181203
 * @author yangbo
 */
public class CSVUtils {
   /** 
      * 生成为CVS文件 
      * @param exportData 源数据List 
      * @param map csv文件的列表头map 
      * @param outPutPath 文件路径 
      * @param fileName 文件名称 
      * @return 
      */
    public static File createCSVFile(List exportData, LinkedHashMap map, String outPutPath, String fileName) throws Exception {
        if (exportData == null || exportData.isEmpty() || map.isEmpty() || StringUtils.isEmpty(outPutPath) || StringUtils.isEmpty(fileName)) {
            EmpExecutionContext.error("生成CVS文件时传入参数非法");
            return null;
        }
        File csvFile;
        BufferedWriter csvFileOutputStream = null;
        OutputStreamWriter writer = null;
        FileOutputStream fileOutputStream = null;
        try {
            File file = new File(outPutPath);
            if (!file.exists()) {
                if(!file.mkdirs()){
                    throw new EMPException("创建文件夹异常！");
                }
            }

            //对文件标题进行MD5
            String md5FileName=MD5.getMD5Str(fileName);
            EmpExecutionContext.info("系统下行记录导出，文件名称，编码前:"+fileName+",编码后:"+md5FileName);

            //定义文件名格式并创建
            csvFile = new File(outPutPath + File.separator + md5FileName + ".csv");
            EmpExecutionContext.info("生成CSV文件名为：" + csvFile);
            // UTF-8使正确读取分隔符","
            //如果生成文件乱码，windows下用gbk，linux用UTF-8
            fileOutputStream = new FileOutputStream(csvFile);
            writer = new OutputStreamWriter(fileOutputStream,"GBK");
            csvFileOutputStream = new BufferedWriter(writer, 1024);
            // 写入文件头部
            Map.Entry propertyEntry = null;
            for (Iterator propertyIterator = map.entrySet().iterator(); propertyIterator.hasNext();)
            {
                propertyEntry = (Map.Entry) propertyIterator.next();
                csvFileOutputStream.write(propertyEntry.getValue() != null ? (String) propertyEntry.getValue() : "" );
                if (propertyIterator.hasNext())
                {
                    csvFileOutputStream.write(",");
                }
            }
            csvFileOutputStream.newLine();
            // 写入文件内容
            Object row = null;
            for (Iterator iterator = exportData.iterator(); iterator.hasNext();)
            {
                row = iterator.next();
                StringBuilder line = new StringBuilder();
                for (Object o : map.entrySet()) {
                    propertyEntry = (Map.Entry) o;
                    String value = BeanUtils.getProperty(row, (String) propertyEntry.getKey());
                    // csv ",换行符 都是特殊字符，需要外面包含双引号, "要替换成""
                    if(value != null && (value.contains(",") || value.contains("\"") || value.contains("\n"))){
                        if(value.contains("\"")){
                            value.replaceAll("\"", "\"\"");
                        }
                        value = "\""+value+"\"";
                    }
                    line.append(value).append("\t").append(",");
                    //line.append("\t").append(BeanUtils.getProperty(row, (String) propertyEntry.getKey())).append("\t").append(",");
                }
                line = new StringBuilder(line.substring(0, line.length() - 1));
                csvFileOutputStream.write(line.toString());
                if (iterator.hasNext()) {
                    csvFileOutputStream.newLine();
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "系统下行导出生成CSV文件，异常。");
            throw e;
        }
        finally {
            try {
                if(null != csvFileOutputStream){
                    csvFileOutputStream.flush();
                    csvFileOutputStream.close();
                }
                if(null != writer){
                    writer.close();
                }
                if(null != fileOutputStream){
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                EmpExecutionContext.error(e, "系统下行导出写入文件时关闭流，异常。");
            }
        }
        return csvFile;
    }

    /** 
      * 下载文件 
      * @param response 
      * @param csvFilePath 文件路径 
      * @param fileName 文件名称 
      * @throws IOException 
    */
    public static void downloadFile(HttpServletResponse response, HttpServletRequest request, String csvFilePath, String fileName) throws IOException
    {
        response.setContentType("application/csv;charset=GBK");
        fileName = encodingFileName(request, fileName);
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        response.setContentType("multipart/form-data");
        response.setCharacterEncoding("GBK");
        BufferedReader reader = null;
        BufferedWriter writer = null;

        InputStreamReader inStream = null;
        OutputStreamWriter writerStream = null;
        try
        {
            inStream = new InputStreamReader(new FileInputStream(csvFilePath), "GBK");
            writerStream = new OutputStreamWriter(response.getOutputStream(),"GBK");
            reader = new BufferedReader(inStream);
            writer = new BufferedWriter(writerStream);
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                writer.write(line);
                writer.newLine();
            }
            writer.flush();
        }
        catch (FileNotFoundException e)
        {
            EmpExecutionContext.error(e, "下载系统下行CSV文件，找不到该文件异常。");
        }
        finally
        {
            if (reader != null)
            {
               try
               {
                   reader.close();
               }
               catch (Exception e)
               {
                   EmpExecutionContext.error(e, "下载系统下行CSV文件时关闭流，异常。");
               }
            }
             if (writer != null)
             {
                try
                {
                    writer.close();
                }
                catch (Exception e)
                {
                    EmpExecutionContext.error(e, "下载系统下行CSV文件时关闭文件输出流异常！");
                }
            }
            if(inStream != null){
                try{
                    inStream.close();
                }catch(Exception e){
                    EmpExecutionContext.error(e, "下载系统下行CSV文件时关闭流异常！");
                }
            }
            if(writerStream != null){
                try{
                    writerStream.close();
                }catch(Exception e){
                    EmpExecutionContext.error(e, "下载系统下行CSV文件时关闭流异常！");
                }
            }
        }
    }

    /**
     * 处理文件名称特殊字符
     * @param request
     * @param fileName
     * @return
     */
    private static String encodingFileName(HttpServletRequest request, String fileName)
    {
        //String userAgent = request.getParameter("userAgentParam").toLowerCase();//参数过长，这种方式取不到值
        //获取浏览器标识
        String [] params = request.getParameterValues("userAgentParam");
        String userAgent = "";
        if(params != null && params.length > 0)
        {
            userAgent = params[0].toLowerCase();
        }
        if (userAgent.contains("msie"))
        {// IE
            try
            {
                fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("%20", "\\+").replaceAll("%28", "\\(")
                        .replaceAll("%29", "\\)").replaceAll("%3B", ";").replaceAll("%40", "@").replaceAll("%23", "\\#")
                        .replaceAll("%26", "\\&").replaceAll("%2C", "\\,").replaceAll("%24", "\\$")
                        .replaceAll("%25", "\\%").replaceAll("%5E", "\\^").replaceAll("%3D", "\\=")
                        .replaceAll("%2B", "\\+");
            }
            catch (UnsupportedEncodingException e)
            {
                EmpExecutionContext.error(e, "文件名编码异常。");
            }
        }
        else if (userAgent.contains("firefox"))
        {// 火狐
            try
            {
                fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1").replaceAll("%20", "\\+")
                        .replaceAll("%28", "\\(").replaceAll("%29", "\\)").replaceAll("%3B", ";").replaceAll("%40", "@")
                        .replaceAll("%23", "\\#").replaceAll("%26", "\\&").replaceAll("%2C", "\\,")
                        .replaceAll("%24", "\\$");
            }
            catch (UnsupportedEncodingException e)
            {
                EmpExecutionContext.error(e, "文件名编码异常。");
            }
        }
        else
         {//默认谷歌
            try
            {
                fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("%20", "\\+").replaceAll("%28", "\\(")
                        .replaceAll("%29", "\\)").replaceAll("%3B", ";").replaceAll("%40", "@").replaceAll("%23", "\\#")
                        .replaceAll("%26", "\\&").replaceAll("%2C", "\\,").replaceAll("%24", "\\$")
                        .replaceAll("%25", "\\%").replaceAll("%5E", "\\^").replaceAll("%3D", "\\=")
                        .replaceAll("%2B", "\\+").replaceAll("%5B", "\\[").replaceAll("%5D", "\\]")
                        .replaceAll("%7B", "\\{").replaceAll("%7D", "\\}");
            }
            catch (UnsupportedEncodingException e)
            {
                EmpExecutionContext.error(e, "文件名编码异常。");
            }
        }
        return fileName;
    }

}
