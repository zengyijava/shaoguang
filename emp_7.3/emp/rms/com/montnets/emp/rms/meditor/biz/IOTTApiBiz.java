package com.montnets.emp.rms.meditor.biz;

import com.montnets.emp.rms.rmsapi.model.SendTempParams;

import java.util.Map;

/**
 * @Author:yangdl
 * @Data:Created in 19:36 2018.8.7 007
 */
public interface IOTTApiBiz {

    /**
     *
     * 模板发送
     *@param tempParams 发送参数
     *@return  map{result:'',msgid:'',custid:''}
     *@throws Exception
     */
    public Map<String,String> sendTemplate(SendTempParams tempParams) throws Exception;
}
