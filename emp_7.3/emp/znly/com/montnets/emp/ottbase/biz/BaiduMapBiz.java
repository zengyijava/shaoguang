package com.montnets.emp.ottbase.biz;

import java.util.Iterator;
import java.util.Map;



import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.ottbase.constant.BaiduHttpUrl;
import com.montnets.emp.ottbase.constant.WXStaticValue;
import com.montnets.emp.ottbase.param.BaiduMapParams;
import com.montnets.emp.ottbase.param.HttpReturnParams;
import com.montnets.emp.ottbase.service.HttpRequestService;

public class BaiduMapBiz extends SuperBiz
{
    /**
     * @description 参数传递转换
     * @param weixParams
     * @return
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-11-20 上午11:24:24
     */
    private void changeParams(HttpReturnParams params,BaiduMapParams baiduParams) throws Exception
    {
        try
        {
            baiduParams.setErrCode(params.getErrCode());
            baiduParams.setErrMsg(params.getErrMsg());
            if(baiduParams.getErrCode() != null && "000".equals(baiduParams.getErrCode()))
            {
                // 返回JSON格式
                if(params.getJsonObject() != null)
                {
                    baiduParams.setJsonObj(params.getJsonObject());
                }
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "changeParams出现异常");
        }
    }
    
    /**
     * 
     * @description    驾车—起/终点模糊检索
     * @param baiduParams   Map做传值，读取key value值
     * @return       			 
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-2 上午10:49:06
     */
    public BaiduMapParams searchDirection(BaiduMapParams baiduParams){
        try{
         Map<String,String> paramsMap = baiduParams.getParamsMap();
         if(paramsMap == null || paramsMap.size() == 0 ){
             baiduParams.setErrMsg("ParamsMapIsNull");
             EmpExecutionContext.error("BaiduMapBiz.searchBusDirection.ParamsMap.size() == 0 ");
             return baiduParams;
         }
         //获取迭代器
         Iterator<Map.Entry<String, String>> iter = baiduParams.getParamsMap().entrySet().iterator();
         Map.Entry<String, String> iternext = null;
         StringBuffer buffer = new StringBuffer();
         //循环获取MAP中的内容
         while (iter.hasNext())
         {
             iternext = iter.next();
             buffer.append(iternext.getKey()).append("=").append(iternext.getValue()).append("&");
         }   
         String paramsUrl = "";
         if(buffer.length() > 0){
             paramsUrl = buffer.toString().substring(0, buffer.toString().length() - 1);
             buffer.setLength(0);
         }
         //判断是否添加进去内容
         if(paramsUrl == null || "".equals(paramsUrl)){
             baiduParams.setErrMsg("BaiduMapBiz.searchBusDirection.paramsUrl == null");
             return baiduParams;
         }
        String httpurl = BaiduHttpUrl.DIRECTION_BUS_URL + paramsUrl;
       HttpReturnParams params = new HttpReturnParams();
        params.setUrl(httpurl);
        params.setRequestType("GET");
        params.setReturnType(WXStaticValue.RETURNHTTP_JSON);
        params = new HttpRequestService().requestOttHttp(params);
        this.changeParams(params, baiduParams);
        }catch (Exception e) {
            baiduParams.setErrMsg("errer");
            EmpExecutionContext.error(e, "BaiduMapBiz.searchBusDirection出现异常");
        }
        return baiduParams;
    }
    
    
    
    
    
    
    
}
