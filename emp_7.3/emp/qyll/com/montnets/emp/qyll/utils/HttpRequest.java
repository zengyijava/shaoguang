package com.montnets.emp.qyll.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.montnets.emp.common.context.EmpExecutionContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;


public class HttpRequest {
    /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "发送GET请求出现异常！");
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                EmpExecutionContext.error(e2, "出现异常！");
            }
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("contentType", "UTF-8");

            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e,"发送 POST 请求出现异常！");
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
            	EmpExecutionContext.error(ex,"发送 POST 请求关闭流失败");
            }
        }
        return result;
    }  
    public static String sendPost(String url, JsonObject param) {
    	PrintWriter out = null;
    	BufferedReader in = null;
    	String result = "";
    	try {
    		URL realUrl = new URL(url);
    		// 打开和URL之间的连接
    		URLConnection conn = realUrl.openConnection();
    		// 设置通用的请求属性
    		conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
    		conn.setRequestProperty("accept", "*/*");
    		conn.setRequestProperty("connection", "Keep-Alive");
    		conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
    		// 发送POST请求必须设置如下两行
    		conn.setDoOutput(true);
    		conn.setDoInput(true);
    		conn.setRequestProperty("Accept-Charset", "UTF-8");
    		conn.setRequestProperty("contentType", "UTF-8");
    		
    		// 获取URLConnection对象对应的输出流
    		out = new PrintWriter(conn.getOutputStream());
    		// 发送请求参数
    		out.print(param);
    		// flush输出流的缓冲
    		out.flush();
    		// 定义BufferedReader输入流来读取URL的响应
    		in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
    		String line;
    		while ((line = in.readLine()) != null) {
    			result += line;
    		}
    	} catch (Exception e) {
    		EmpExecutionContext.error(e, "发送 POST 请求出现异常！");
    	}
    	//使用finally块来关闭输出流、输入流
    	finally{
    		try{
    			if(out!=null){
    				out.close();
    			}
    			if(in!=null){
    				in.close();
    			}
    		}
    		catch(IOException ex){
                EmpExecutionContext.error(ex, "出现异常！");
    		}
    	}
    	return result;
    }  
    
    
    public static JsonObject EM1001() throws Exception{
    	JsonObject json = new JsonObject();
    	json.addProperty("BCode", "EMI1001");
    	json.addProperty("Ack", "1");
    	json.addProperty("SqId", "2017101115052810000019");
    	json.addProperty("ECID", "100107");
    	
    	JsonObject Cnxt = new JsonObject();
    	Cnxt.addProperty("ECOrderId", "20171114000006");
    	Cnxt.addProperty("Activity", "");
    	Cnxt.addProperty("UserID", "SP0001");
    	Cnxt.addProperty("PWD", "123qwe");
    	Cnxt.addProperty("MSG", "19:51接口短信订购流量提醒");
    	
    	JsonObject OrderList = new JsonObject();
    	OrderList.addProperty("PID", "100016");
    	OrderList.addProperty("Phones", "15102086920");
    	
    	JsonArray jaArray = new JsonArray();
    	jaArray.add(OrderList);
    	
    	Cnxt.add("OrderList", jaArray);
    	
    	String result = EncryptOrDecrypt.encryptString(Cnxt.toString(), "9HOhIwRYYjg=ed+nHWrsl5E=");
    	json.addProperty("Cnxt", result);
		return json;
    	
    }
    
    public static JsonObject EM1002() throws Exception{
    	
    	
    	JsonObject json = new JsonObject();
    	json.addProperty("BCode", "EMI1002");
    	json.addProperty("Ack", "1");
    	json.addProperty("SqId", "2017101115052810000002");
    	json.addProperty("ECID", "100229");
    	
    	JsonObject json1 = new JsonObject();
    	json1.addProperty("ECOrderIds", "20171101090007");
    	
    	String result = EncryptOrDecrypt.encryptString(json1.toString(), "brZPJjfN06g=+S3R0Ul8hts=");
    	StringBuffer buf = new StringBuffer("BCode=EMI1002&Ack=1&SqId=2017101115052810000002&ECID=100229&");
    	buf.append("Cnxt=").append(result);
    	json.addProperty("Cnxt", result);
    	return json;
    	
    }
    
    public static JsonObject EM1003() throws Exception{
    	JsonObject json = new JsonObject();
    	json.addProperty("BCode", "EMI1003");
    	json.addProperty("Ack", "2");
    	json.addProperty("SqId", "2017101115052810000002");//回调返回的SqId
    	json.addProperty("RtState", "0");
    	json.addProperty("Cnxt", "");
    	
    	
    	return json;
    	
    }
    public static JsonObject EM1004() throws Exception{
    	JsonObject json = new JsonObject();
    	json.addProperty("BCode", "EMI1004");
    	json.addProperty("Ack", "1");
    	json.addProperty("SqId", "2017101115052810000002");
    	json.addProperty("ECID", "100229");
    	
    	JsonObject json1 = new JsonObject();
    	json1.addProperty("CorpCode", "100229");
    	String result = EncryptOrDecrypt.encryptString(json1.toString(), "brZPJjfN06g=+S3R0Ul8hts=");
    	
    	json.addProperty("Cnxt", result);
    	
    	return json;
    	
    }
    public static JsonObject EM1005() throws Exception{
    	JsonObject json = new JsonObject();
    	json.addProperty("BCode", "EMI1005");
    	json.addProperty("Ack", "1");
    	json.addProperty("SqId", "2017101115052810000002");
    	json.addProperty("ECID", "100229");
    	
    	JsonObject json1 = new JsonObject();
    	json1.addProperty("CorpCode", "100229");
    	String result = EncryptOrDecrypt.encryptString(json1.toString(), "brZPJjfN06g=+S3R0Ul8hts=");
    	
    	json.addProperty("Cnxt", result);
    	
    	return json;
    	
    	
    }
    public static JsonObject EM1006() throws Exception{
    	JsonObject json = new JsonObject();
    	json.addProperty("BCode", "EMI1006");
    	json.addProperty("Ack", "1");
    	json.addProperty("SqId", "2017101115052810000003");
    	json.addProperty("ECID", "100229");
    	
    	JsonObject json1 = new JsonObject();
    	json1.addProperty("CorpCode", "100229");
    	String result = EncryptOrDecrypt.encryptString(json1.toString(), "brZPJjfN06g=+S3R0Ul8hts=");
    	json.addProperty("Cnxt", result);
    	return json;
    	
    }

    
    
    
    
    public static void main(String[] args) throws Exception {
    	
    	JsonObject j = EM1001();
    	
 
    	String sr =HttpRequest.sendPost("http://192.169.2.179:7071/mdgg/MdosEcHttp.hts",j.toString());
    	System.out.println("返回结果："+sr);
//    	JsonObject json1 = new JsonParser().parse(sr).getAsJsonObject();
//        String strr =json1.get("Cnxt").getAsString();
//       String ss = "a95qdFwziYBHEnXiIet3hWgKjR+JZtPw9VTX/3ehldhxj69Un/TD7UrGIWaXRs4mwPfK6tc6fbEhbxPvCZeeB30P+oQPU/BtiJ6K453dQPH7rTYNiBZPBPAD/Iqk2nLDflx8L8W2547N/qOjk8ThBSHRpu7/iS7D0qliNdzbq2oTHytO4XXCINr1q6JfDm+eYk6PrcE0YhYv29iILVokFARbNvEJj6gplizS5DaYgJzP9JbT8MYJfms6xjzU1RE1hfNmYnnCPwuFxM4p4ujaxwt1E00SRSBIqbI+9RII3KGBeU4xTeWYbuvPqiF/1tzTai+gFPQ8V0hjU1aklE8qkayIKyQKYbertXUoSfKQ5I32SRv8D5JQnqBv94HSbtS3CterjzryYhKGF6KYuu5gfuYxN31xKDrMZnd8pbYJkYpkIesBEvRomqo3PEU4EI1z";
        
//        String sr = HttpRequest.sendPost("http://10.10.202.61:8088/mdgg/MdosEcHttp.hts", EM1001());
//        JsonObject json1 = new JsonParser().parse(sr).getAsJsonObject();
        
        
        
//        String strr =json1.get("Cnxt").getAsString();
//        System.out.println(sr);
//        String data = "uU9KsUS+jxVcd4WvuZDKZYHWe9T2XnADxxCnRPS5Ntap3KB7tQmrNRcASgTLx8DBR6JAraQCLCyPkCaYbPnOww==";
//        System.out.println(EncryptOrDecrypt.decryptString(data, "brZPJjfN06g=+S3R0Ul8hts="));
//       
	}
}