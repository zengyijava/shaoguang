package com.montnets.emp.rms.rmsapi.util;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.rms.rmsapi.constant.RMSHttpConstant;
import com.montnets.emp.rms.rmsapi.model.TempParams;
import com.montnets.emp.rms.wbs.util.IEncodeAndDecode;
import com.montnets.emp.rms.wbs.util.impl.Base64MD5Coding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class JSONToMapUtil {
    /**
     * 将json字符串转为Map<String,String>
     *
     * @param message
     * @return
     * @throws Exception
     * @author chenly
     */
    public static Map<String, String> jsonStrToMapStr(String message) {
        Map<String, String> map = new HashMap<String, String>();
        if (message == null || "".equals(message)) {
            EmpExecutionContext.info("接口返回值为空");
            return map;
        }
        try {
            JSONObject jsonObj = new JSONObject(message);
            Iterator<?> iterator = jsonObj.keys();
            String key = null;
            Object value = null;
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                value = jsonObj.get(key);
                map.put(key, value.toString());
            }
        } catch (JSONException e) {
            EmpExecutionContext.error(e, "字符串转json对象失败");
        }

        return map;
    }

    /**
     * 将json字符串转为Map<String,Object>
     *
     * @param message
     * @return
     * @author chenly
     */
    public static Map<String, Object> jsonStrToMapObj(String message) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (message == null || message.length() == 0) {
            EmpExecutionContext.info("接口返回值为空");
            return map;
        }
        try {
            JSONObject jsonObj = new JSONObject(message);
            map.put("result", jsonObj.has("result") ? jsonObj.get("result") : "");
            Object status = jsonObj.has("status") ? jsonObj.get("status") : "";
            if (status != null && !"".equals(status)) {
                JSONArray jsonArray = (JSONArray) status;
                Object value = null;
                List<Map<String, String>> list = jsonArraytoListMap(jsonArray);
                value = list;
//				map.put("status", value == null ? "" : value);
                map.put("status", value);
            } else {
                map.put("status", "");
            }

        } catch (JSONException e) {
            EmpExecutionContext.error(e, e.getMessage());
        }
        return map;
    }

    /**
     * 将json格式的字符串转为map
     *
     * @param message
     * @return
     */
    public static Map<String, Object> jsonToMap(String message) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (message == null || message.length() == 0) {
            EmpExecutionContext.info("接口返回值为空");
            return map;
        }
        try {
            JSONObject jsonObj = new JSONObject(message);
            map.put("rstate", jsonObj.has("rstate") ? jsonObj.get("rstate") : "");
            map.put("statuscode", jsonObj.has("statuscode") ? jsonObj.get("statuscode") : "");
            map.put("resultMsg", jsonObj.has("resultMsg") ? jsonObj.get("resultMsg") : "");
            //请求成功
            if ((String.valueOf(map.get("rstate"))).equals("0")) {
                String scont = jsonObj.getString("scont");
                IEncodeAndDecode encodeUtil = new Base64MD5Coding();
                String scontDec = encodeUtil.decode(scont, RMSHttpConstant.RMS_MOSS_QUERY_ENCRY_KEY);
                if (scontDec != null && !"".equals(scontDec)) {
                    JSONArray jsonArray = new JSONArray(scontDec);
                    Object value = null;
                    List<Map<String, String>> list = jsonArraytoListMap(jsonArray);
                    value = list;
//					map.put("status", value == null ? "" : value);
                    map.put("scont", value);
                } else {
                    map.put("scont", "");
                }
            }
        } catch (JSONException e) {
            EmpExecutionContext.error(e, e.getMessage());
        }
        return map;
    }

    public static void main(String[] args) {
        String temp = "+LV+Mei3byGm+QYRvOWK9Nks5KnRWCS8XgD+XYAh6hKrFnN2WuymDg\u003d\u003d";
        IEncodeAndDecode encodeUtil = new Base64MD5Coding();
        String key = "6158ACD7414A7E5F";
        String scontDec = encodeUtil.decode(temp, key);
        System.out.println(scontDec);

    }

    /**
     * 将json格式的字符串转为map
     *
     * @param message
     * @return
     */
    public static Map<String, Object> getTempJsonToMap(String message) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (message == null || message.length() == 0) {
            EmpExecutionContext.info("接口返回值为空");
            return map;
        }
        try {
            JSONObject jsonObj = new JSONObject(message);
            map.put("result", jsonObj.has("result") ? jsonObj.get("result") : "");
            map.put("timestamp", jsonObj.has("timestamp") ? jsonObj.get("timestamp") : "");
            map.put("sign", jsonObj.has("sign") ? jsonObj.get("sign") : "");
            Object content = jsonObj.has("content") ? jsonObj.get("content") : "";
            if (null != content && !"".equals(content)) {
                JSONArray jsonArray = new JSONArray(String.valueOf(content));
                Object value = null;
                List<TempParams> list = jsonArrToListTempParams(jsonArray);
                value = list;
                map.put("content", value);
            } else {
                map.put("content", "");
            }
        } catch (JSONException e) {
            EmpExecutionContext.error(e, e.getMessage());
        }
        return map;
    }

    public static Map<String, Object> getCommonTempJsonToMap(String message) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (message == null || message.length() == 0) {
            EmpExecutionContext.info("接口返回值为空");
            return map;
        }
        try {
            JSONObject jsonObj = new JSONObject(message);
            map.put("result", jsonObj.has("result") ? jsonObj.get("result") : "");
            map.put("timestamp", jsonObj.has("timestamp") ? jsonObj.get("timestamp") : "");
            map.put("sign", jsonObj.has("sign") ? jsonObj.get("sign") : "");
            map.put("tmplid", jsonObj.has("tmplid") ? jsonObj.get("tmplid") : "");
            map.put("content",jsonObj.has("content") ? jsonObj.get("content") : "");
        } catch (JSONException e) {
            EmpExecutionContext.error(e, e.getMessage());
        }
        return map;
    }

    public static Map<String, Object> getEcTempJsonToMap(String message) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (message == null || message.length() == 0) {
            EmpExecutionContext.info("接口返回值为空");
            return map;
        }
        try {
            JSONObject jsonObj = new JSONObject(message);
            map.put("result", jsonObj.has("result") ? jsonObj.get("result") : "");
            map.put("timestamp", jsonObj.has("timestamp") ? jsonObj.get("timestamp") : "");
            map.put("sign", jsonObj.has("sign") ? jsonObj.get("sign") : "");
            map.put("tmplstock", jsonObj.has("tmplstock") ? jsonObj.get("tmplstock") : "");
            if (jsonObj.has("result") && "0".equalsIgnoreCase(String.valueOf(jsonObj.get("result")))) {
                JSONObject templateJSon = new JSONObject(jsonObj.get("template").toString());
                if (null != templateJSon) {
                    HashMap<String, Object> template = new HashMap<String, Object>();
                    template.put("tmplid", templateJSon.has("tmplid") ? templateJSon.get("tmplid") : "");
                    template.put("origin", templateJSon.has("origin") ? templateJSon.get("origin") : "");
                    template.put("tmplver", templateJSon.has("tmplver") ? templateJSon.get("tmplver") : "");
                    template.put("title", templateJSon.has("title") ? templateJSon.get("title") : "");
                    template.put("content", templateJSon.has("content") ? templateJSon.get("content") : "");
                    map.put("template", template);
                }
            } else {
                map.put("content", "");
                map.put("result", jsonObj.has("result") ? jsonObj.get("result") : "");
            }
        } catch (JSONException e) {
            EmpExecutionContext.error(e, e.getMessage());
        }
        return map;
    }

    /**
     * 将jSONArray转为List<TempParams>
     *
     * @param jsonarr
     * @return
     */
    private static List<TempParams> jsonArrToListTempParams(JSONArray jsonarr) {
        List<TempParams> list = new ArrayList<TempParams>();
        TempParams tempparams = null;
        JSONObject jsonObj = null;
        if (jsonarr == null) {
            return null;
        }
        try {
            for (int i = 0; i < jsonarr.length(); i++) {
                tempparams = new TempParams();
                jsonObj = (JSONObject) (jsonarr.get(i));
                tempparams.setType(jsonObj.getInt("type"));
                tempparams.setSize(jsonObj.getInt("size"));
                tempparams.setPnum(jsonObj.getInt("pnum"));
                tempparams.setDegree(jsonObj.getInt("degree"));
                tempparams.setContent(jsonObj.getString("content"));
                list.add(tempparams);
            }
        } catch (JSONException e) {
            EmpExecutionContext.error(e, e.getMessage());
        }
        return list;
    }

    private static List<TempParams> jsonArrToListTempParamsV1(JSONArray jsonarr) {
        List<TempParams> list = new ArrayList<TempParams>();
        TempParams tempparams = null;
        JSONObject jsonObj = null;
        if (jsonarr == null) {
            return null;
        }
        try {
            for (int i = 0; i < jsonarr.length(); i++) {
                tempparams = new TempParams();
                jsonObj = (JSONObject) (jsonarr.get(i));
                tempparams.setType(jsonObj.getInt("type"));
                tempparams.setSize(jsonObj.getInt("size"));
                tempparams.setContent(jsonObj.getString("content"));
                list.add(tempparams);
            }
        } catch (JSONException e) {
            EmpExecutionContext.error(e, e.getMessage());
        }
        return list;
    }


    /**
     * 将JSONArray转换为list<Map<String,String>>
     *
     * @param jsonarr
     * @return
     */
    private static List<Map<String, String>> jsonArraytoListMap(JSONArray jsonarr) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        if (jsonarr == null) {
            return null;
        }
        try {
            for (int i = 0; i < jsonarr.length(); i++) {
                Map<String, String> map = jsonStrToMapStr(String.valueOf(jsonarr.get(i)));
                list.add(map);
            }
        } catch (JSONException e) {
            EmpExecutionContext.error(e, "jsonArray转list<Map>失败");
        }
        return list;
    }
	
/*	//测试
	public static void main(String[] args) {
//		String test="{\"result\":\"1\",\"timestamp\":\"0803192020\",\"sign\":\"ff\",content:[{\"type\":1,\"degree\":1,\"pnum\":3,\"size\":1024,\"content\":\"Base64(rms文件内容)\"}]}";
//		getTempJsonToMap(test);
		String test1="{\"statuscode\":\"1\",\"resultMsg\":0803192020,\"rstate\":0,\"scont\":\"qwq\"}";
		System.out.println(jsonToMap(test1).get("scont"));
		
	}*/

}