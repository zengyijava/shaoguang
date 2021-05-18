package com.montnets.emp.ottbase.param;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.simple.JSONObject;

/**
 * 
 * @description     百度LBS参数传递类
 * @project ott
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author yejiangmin <282905282@qq.com>
 * @datetime 2013-11-29 下午02:33:03
 */
public class BaiduMapParams implements Serializable
{
    
    /**
     * @description  
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-11-29 下午02:36:13
     */
    private static final long serialVersionUID = -5124141957569259024L;
    
    //城市
    private String city;
    //详细地址
    private String location;
    //错误编码  正确 返回 000
    private String errCode;
    //错误信息  正确  返回 success
    private String errMsg;
    //返回JSON格式
    private JSONObject jsonObj;
    //返回XML格式
    private String returnXml;
    //存放传递参数KEY /VALUE对
    private Map<String, String> paramsMap;
    //请求地址
    private String url;
    //请求类型  get / post
    private String requestType;
    //返回类型  xml/json
    private String returnType;
    
    
    //公交 自驾 步行 类型   driving（驾车）、walking（步行）、transit（公交）
    private String mode;
    
    public BaiduMapParams(){
        this.errCode = "-9999";
        paramsMap = new LinkedHashMap<String, String>();
    }
    
    public String getCity()
    {
        return city;
    }
    public void setCity(String city)
    {
        this.city = city;
    }
    public String getLocation()
    {
        return location;
    }
    public void setLocation(String location)
    {
        this.location = location;
    }
    public String getErrCode()
    {
        return errCode;
    }
    public void setErrCode(String errCode)
    {
        this.errCode = errCode;
    }
    public String getErrMsg()
    {
        return errMsg;
    }
    public void setErrMsg(String errMsg)
    {
        this.errMsg = errMsg;
    }
    public JSONObject getJsonObj()
    {
        return jsonObj;
    }
    public void setJsonObj(JSONObject jsonObj)
    {
        this.jsonObj = jsonObj;
    }
    public String getReturnXml()
    {
        return returnXml;
    }
    public void setReturnXml(String returnXml)
    {
        this.returnXml = returnXml;
    }

    public Map<String, String> getParamsMap()
    {
        return paramsMap;
    }

    public void setParamsMap(Map<String, String> paramsMap)
    {
        this.paramsMap = paramsMap;
    }
    
    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getRequestType()
    {
        return requestType;
    }

    public void setRequestType(String requestType)
    {
        this.requestType = requestType;
    }

    public String getReturnType()
    {
        return returnType;
    }

    public void setReturnType(String returnType)
    {
        this.returnType = returnType;
    }
    
    public String getMode()
    {
        return mode;
    }

    public void setMode(String mode)
    {
        this.mode = mode;
    }
    
}
