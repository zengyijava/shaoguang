package com.montnets.emp.common.tools;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.montnets.emp.common.context.EmpExecutionContext;

public class JsonReturnUtil {


    /**
     * 成功
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public static void success(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 200);
        jsonObject.put("msg", TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SUCCESS));
        ifJsonP(jsonObject, request, response);
    }

    /**
     * 成功
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public static void success(String msg, HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 200);
        jsonObject.put("msg", msg);
        ifJsonP(jsonObject, request, response);
    }

    /**
     * 成功
     *
     * @param data     数据
     * @param request
     * @param response
     * @throws IOException
     */
    public static void success(Object data, String msg, HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 200);
        jsonObject.put("msg", msg);
        jsonObject.put("data", data);
        ifJsonP(jsonObject, request, response);
    }

    /**
     * 成功
     *
     * @param data     数据
     * @param request
     * @param response
     * @throws IOException
     */
    public static void success(Object data, HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 200);
        jsonObject.put("msg", TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SUCCESS));
        jsonObject.put("data", data);
        ifJsonP(jsonObject, request, response);
    }

    /**
     * 分页
     *
     * @param data
     * @param request
     * @param response
     * @param totalPage
     * @param totalRecord
     */
    public static void success(Object data, HttpServletRequest request, HttpServletResponse response, Integer totalPage, Integer totalRecord) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 200);
        jsonObject.put("msg", TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SUCCESS));
        JSONObject jsonObjectList = new JSONObject();
        jsonObjectList.put("list", data);
        jsonObjectList.put("totalPage", totalPage);
        jsonObjectList.put("totalRecord", totalRecord);
        jsonObject.put("data", jsonObjectList);
        ifJsonP(jsonObject, request, response);
    }

    /**
     * 分页
     *
     * @param data
     * @param msg
     * @param request
     * @param response
     * @param totalPage
     * @param totalRecord
     */
    public static void success(Object data, String msg, HttpServletRequest request, HttpServletResponse response, Integer totalPage, Integer totalRecord) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 200);
        jsonObject.put("msg", msg);
        JSONObject jsonObjectList = new JSONObject();
        jsonObjectList.put("list", data);
        jsonObjectList.put("totalPage", totalPage);
        jsonObjectList.put("totalRecord", totalRecord);
        jsonObject.put("data", jsonObjectList);
        ifJsonP(jsonObject, request, response);
    }


    /**
     * 失败
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public static void fail(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 99);
        jsonObject.put("msg", TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.FAIL));
        ifJsonP(jsonObject, request, response);
    }

    /**
     * 失败
     *
     * @param code
     * @param request
     * @param response
     * @throws IOException
     */
    public static void fail(Integer code, HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("msg", TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.FAIL));
        ifJsonP(jsonObject, request, response);
    }

    /**
     * 失败
     *
     * @param code
     * @param request
     * @param response
     * @throws IOException
     */
    public static void fail(Integer code, String msg, HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("msg", msg);
        ifJsonP(jsonObject, request, response);
    }

    /**
     * 失败
     *
     * @param msg      自定义提示信息
     * @param request
     * @param response
     * @throws IOException
     */
    public static void fail(String msg, HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 99);
        jsonObject.put("msg", msg);
        ifJsonP(jsonObject, request, response);
    }


    /**
     * 判断是否为jsonp
     *
     * @param jsonObject
     * @param request
     * @param response
     * @throws IOException
     */
    private static void ifJsonP(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            EmpExecutionContext.error(e.getMessage());
        } finally {
            if(out != null) {
                out.print(jsonObject);
                out.close();
            }
        }
    }
}
