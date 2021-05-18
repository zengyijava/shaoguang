package com.montnets.shaoguanga.scheduled;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.montnets.shaoguanga.bean.GetRpts;
import com.montnets.shaoguanga.bean.SendRpts;
import com.montnets.shaoguanga.constant.SgConstant;
import com.montnets.shaoguanga.properties.MwConfig;
import com.montnets.shaoguanga.service.HttpService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @param
 * @ClassName RptPushScheduled
 * @Author zengyi
 * @Description
 * @Date 2021/4/9 16:39
 **/
@Slf4j
@Component
@PropertySource("classpath:application.properties")
public class RptPushScheduled {
    @Autowired
    private HttpService httpService;
    @Autowired
    private MwConfig mwConfig;

    @Scheduled(cron = "${scheduled.rptPushTime}")
    public void rptPush() {
        String getRpt = null;
        String pwd=null;
        String sendUrl=null;
        String sendParams=null;
        try {
            Map<String, String> spMap = mwConfig.getSpMap();
            //是否需要推送状态报告
            if (mwConfig.isNeedRpt() && null!=spMap && spMap.size()>0) {
                for (Map.Entry<String, String> entry : spMap.entrySet()) {
                    String userId = entry.getKey();
                    String[] spArr = entry.getValue().split(SgConstant.REGEX);
                    if (spArr.length < 2) {
                        continue;
                    }
                    pwd = spArr[0];
                    sendUrl = spArr[1];
                    if(StringUtils.isNotEmpty(userId) && StringUtils.isNotEmpty(pwd)){
                        sendParams = handleSendParams(userId, pwd);
                    }
                    //从边界获取状态报告
                    getRpt = httpService.send2GateKeeper(sendParams, mwConfig.getGetRpt());
                    if (StringUtils.isNotEmpty(getRpt)) {
                        log.info("状态报告： " + getRpt);
                        //状态报告解析成推送的内容
                        JSONObject sendRptsJSON = rptsToJSON(getRpt);
                        if (StringUtils.isNotEmpty(userId) && StringUtils.isNotEmpty(sendUrl) && !sendRptsJSON.isEmpty()) {
                            //推送给业务系统
                            try {
                                boolean flag = httpService.send2SpUrl(sendUrl, sendRptsJSON.getJSONArray("sendRpts").toString());
                                if (!flag) {
                                    writeTxt(userId, sendRptsJSON);
                                }
                            } catch (Exception e) {
                                writeTxt(userId, sendRptsJSON);
                            }
                        }
                    }
                }
            } else {
                log.info("不需要推送状态报告");
            }
        }catch (Exception e){
            log.error("定时任务异常: ",e);
        }
    }


    /**
     * 请求边界数据处理
     *
     * @param userId
     * @param pwd
     * @return
     */
    private static String handleSendParams(String userId, String pwd) {
        try {
            HashMap<String, Object> map = new HashMap<>();
            map.put("userid", URLEncoder.encode(userId,"UTF-8"));
            map.put("pwd", URLEncoder.encode(pwd,"UTF-8"));
            String temp = map.toString().replace(", ", "&");
            return temp.substring(1, temp.length() - 1);
        } catch (Exception e) {
            log.error("处理params异常");
            return null;
        }
    }


    /**
     * 将状态报告的字符串json转化为推送给sp账号地址的json
     *
     * @param getRpt
     * @return
     * @throws ParseException
     */
    private JSONObject rptsToJSON(String getRpt) {
        JSONObject returnJSON = new JSONObject();
        try {
            List<SendRpts> sendRptsList = new ArrayList<>();
            JSONObject jsonObject = JSONObject.parseObject(getRpt);
            if (null != jsonObject.getJSONArray("rpts")) {
                JSONArray rptsJSONArr = jsonObject.getJSONArray("rpts");
                SendRpts sendRpts = null;
                List<GetRpts> getRptsList = rptsJSONArr.toJavaList(GetRpts.class);
                for (GetRpts getRpts : getRptsList) {
                    sendRpts = new SendRpts();
                    sendRpts.setReportStatus("0");
                    sendRpts.setMobile(getRpts.getMobile());
                    //字符串"yyyy-MM-dd HH:mm:ss" 转换为字符串"yyyyMMddHHmmss"
                    sendRpts.setSubmitDate(DateFormatUtils.format(DateUtils.parseDate(getRpts.getStime(), "yyyy-MM-dd HH:mm:ss"), "yyyyMMddHHmmss"));
                    sendRpts.setReceiveDate(DateFormatUtils.format(DateUtils.parseDate(getRpts.getRtime(), "yyyy-MM-dd HH:mm:ss"), "yyyyMMddHHmmss"));
                    sendRpts.setMsgGroup(getRpts.getMsgid().toString());
                    sendRptsList.add(sendRpts);
                }
                returnJSON.put("sendRpts", sendRptsList);
            }
        } catch (Exception e) {
            log.error("状态报告解析成推送内容异常:{}", e);
        }
        return returnJSON;
    }


    /**
     * 在项目目录下写文件
     *
     * @param userid
     * @param param
     */
    public void writeTxt(String userid, JSONObject param) {
        SimpleDateFormat s = new SimpleDateFormat("yyyyMMddHHmmss");
        String second = s.format(new Date());
        SimpleDateFormat d = new SimpleDateFormat("yyyyMMdd");
        String day = d.format(new Date());
        FileOutputStream realOut = null;
        FileOutputStream bakOut = null;
        try {
            //获取当前项目路径
            String path = System.getProperty("user.dir");
            File rptsPath = new File(path + File.separator + "rptsfile");
            File realFile = new File(rptsPath + File.separator + "real" + File.separator + userid);
            File bakFile = new File(rptsPath + File.separator + "bak" +File.separator+day
                    +File.separator + userid);
            if(!realFile.exists() ){ realFile.mkdirs(); }
            if(!bakFile.exists()){ bakFile.mkdirs(); }
            realOut = new FileOutputStream(realFile + File.separator + userid + second + ".txt");
            String tempParam = param.toJSONString();
            realOut.write(tempParam.getBytes("utf-8"));
        } catch (Exception e) {
            log.error("状态报告存txt文件异常：{}", e);
        } finally {
            try {
                if (realOut != null) {
                    realOut.close();
                }
                if (bakOut != null) {
                    bakOut.close();
                }
            } catch (IOException e) {
                log.error("流关闭异常: {}", e);
            }
        }

    }


}
