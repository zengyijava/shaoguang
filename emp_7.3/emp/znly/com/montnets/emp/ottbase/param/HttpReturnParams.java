package com.montnets.emp.ottbase.param;

import java.io.Serializable;

import org.json.simple.JSONObject;

/**
 * @description 请求http返回参数
 * @project p_wx
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author yejiangmin <282905282@qq.com>
 * @datetime 2013-11-5 下午05:09:51
 */
public class HttpReturnParams implements Serializable
{
    private static final long serialVersionUID = -1198700760429938920L;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~用于请求后返回的参数
    // 错误编码 正确 返回 000
    private String            errCode;

    // 错误信息 正确 返回 success
    private String            errMsg;

    // 请求返回json格式
    private JSONObject        jsonObject;

    // 请求返回 xml/XML 格式 & FILE操作成功
    private String            returnXml;

    // 请求返回类型 json/"JSON" xml/"XML" ""/"FILE"
    private String            returnType;

    // 请求返回的状态值 成功 200/或者其他
    private int               statusCode;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~用于请求时设置的参数
    // 请求的http地址
    private String            url;

    // 请求的http的类型 POST/GET
    private String            requestType;

    // 设置POST请求时，是 [MSG]参数为JSON格式 / [FILE]上传文件 FILE
    private String            httpPostType;

    // 设置POST请求时 ， MSG情况下 JSON格式参数 / FILE情况下 inputName 文件在页面所对应的NAME 属性值
    private String            postMsg;

    // 设置POST请求 并且 httpPostType为 FILE 时候    上传或下载使用 文件的相对路径
    private String            fileUrl;

    // 上传或下载时 的全路径
    private String            updownFileUrl;

    // 是否支持输入 FALSE TRUE
    private boolean           doInput;

    // 是否支持输出 FALSE TRUE
    private boolean           doOutput;

    // 哪个菜单模块发送请求 ，写入日志
    private String            menuCode;

    public HttpReturnParams()
    {
        doInput = false;
        doOutput = false;
        statusCode = 0;
        errCode = "-9999";
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

    public JSONObject getJsonObject()
    {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject)
    {
        this.jsonObject = jsonObject;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public boolean isDoInput()
    {
        return doInput;
    }

    public void setDoInput(boolean doInput)
    {
        this.doInput = doInput;
    }

    public boolean isDoOutput()
    {
        return doOutput;
    }

    public void setDoOutput(boolean doOutput)
    {
        this.doOutput = doOutput;
    }

    public String getReturnXml()
    {
        return returnXml;
    }

    public void setReturnXml(String returnXml)
    {
        this.returnXml = returnXml;
    }

    public String getReturnType()
    {
        return returnType;
    }

    public void setReturnType(String returnType)
    {
        this.returnType = returnType;
    }

    public String getMenuCode()
    {
        return menuCode;
    }

    public void setMenuCode(String menuCode)
    {
        this.menuCode = menuCode;
    }

    public int getStatusCode()
    {
        return statusCode;
    }

    public void setStatusCode(int statusCode)
    {
        this.statusCode = statusCode;
    }

    public String getRequestType()
    {
        return requestType;
    }

    public void setRequestType(String requestType)
    {
        this.requestType = requestType;
    }

    public String getHttpPostType()
    {
        return httpPostType;
    }

    public void setHttpPostType(String httpPostType)
    {
        this.httpPostType = httpPostType;
    }

    public String getPostMsg()
    {
        return postMsg;
    }

    public void setPostMsg(String postMsg)
    {
        this.postMsg = postMsg;
    }

    public String getFileUrl()
    {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl)
    {
        this.fileUrl = fileUrl;
    }

    public String getUpdownFileUrl()
    {
        return updownFileUrl;
    }

    public void setUpdownFileUrl(String updownFileUrl)
    {
        this.updownFileUrl = updownFileUrl;
    }

}
