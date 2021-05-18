package com.montnets.emp.rms.meditor.tools;

import com.alibaba.fastjson.JSON;
import com.montnets.emp.common.context.EmpExecutionContext;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.*;

/**
 * Created by 15810 on 2017/7/5.
 */
public class MD5Utils {
	
    public static String MD5deal(String sysid,String timestamp,String btype,String ftype,String fpath,String fname,String secretkey) {
        Map<String, String> paramValues = new HashMap<String, String>();
        paramValues.put("sysid", sysid);
        paramValues.put("timestamp", timestamp);
        paramValues.put("btype", btype);
        paramValues.put("ftype", ftype);
        paramValues.put("fpath", fpath);
        paramValues.put("fname", fname);
        //System.out.println(JSON.toJSON(paramValues));
        return sign(paramValues, secretkey);
    }

    /**
     * 使用<code>secret</code>对paramValues按以下算法进行签名： <br/>
     * uppercase(hex(sha1(secretkey1value1key2value2...secret))
     *
     * @param paramValues 参数列表
     * @param secret
     * @return
     */
    public static String sign(Map<String, String> paramValues, String secret) {
        return sign(paramValues, null, secret);
    }

    /**
     * 对paramValues进行签名，其中ignoreParamNames这些参数不参与签名
     *
     * @param paramValues
     * @param ignoreParamNames
     * @param secret
     * @return
     */
    public static String sign(Map<String, String> paramValues, List<String> ignoreParamNames, String secret) {
        try {
            StringBuilder sb = new StringBuilder();
            List<String> paramNames = new ArrayList<String>(paramValues.size());
            paramNames.addAll(paramValues.keySet());
            if (ignoreParamNames != null && ignoreParamNames.size() > 0) {
                for (String ignoreParamName : ignoreParamNames) {
                    paramNames.remove(ignoreParamName);
                }
            }
            Collections.sort(paramNames);

            sb.append(secret);
            for (String paramName : paramNames) {
                sb.append(paramName).append(paramValues.get(paramName));
            }
            sb.append(secret);
            byte[] sha1Digest = getSHA1Digest(sb.toString());
            return byte2hex(sha1Digest);
        } catch (IOException e) {
            EmpExecutionContext.error(e, "发现异常");
            return null;
        }
    }

    private static byte[] getSHA1Digest(String data) throws IOException {
        byte[] bytes = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            bytes = md.digest(data.getBytes("UTF-8"));
        } catch (GeneralSecurityException gse) {
            throw new IOException(gse);
        }
        return bytes;
    }

    /**
     * 二进制转十六进制字符串
     *
     * @param bytes
     * @return
     */
    private static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }

    public static void testmd5() {
        Long currTime = System.currentTimeMillis() / 1000;
        System.out.println(currTime);
        Map<String, String> paramValues = new HashMap<String, String>();
        paramValues.put("appKey", "11130");
//        paramValues.put("appKey","126966");
        Double temp=(Math.random() * 9 + 1) * 10000;
        paramValues.put("nonce", String.valueOf(temp.intValue()));
        paramValues.put("timestamp", currTime.toString());

        paramValues.put("cardIds", "89860918700004039282");
//        paramValues.put("cardIds","89860617030056072807");
        paramValues.put("operator", "2");
        paramValues.put("cardLenType", "1");
        System.out.println(JSON.toJSON(paramValues));
        String signStr = sign(paramValues, "a22388f905a447b280557888bd659391");
        System.out.println(signStr);
    }
    

    public static void main(String args[]) {
    	//testmd5();
    	
    	String sysid = "1001";
        Long currTime = System.currentTimeMillis() / 1000;
        System.out.println(currTime);
    	String timestamp = currTime.toString();
    	String btype = "1";
    	String ftype = "1";
    	String fpath = "1000/corpcode/0001";
    	String fname = "card2.html";
    	String secretkey="a22388f905a447b280557888bd659391";
    	
    	String signStr = MD5deal(sysid,timestamp,btype,ftype,fpath,fname,secretkey);
        System.out.println(signStr);
    	
    }

}
