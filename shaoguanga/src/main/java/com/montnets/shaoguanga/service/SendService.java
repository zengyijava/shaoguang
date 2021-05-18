package com.montnets.shaoguanga.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.montnets.shaoguanga.bean.ReqBody;
import com.montnets.shaoguanga.bean.RespBody;
import com.montnets.shaoguanga.constant.SgConstant;
import com.montnets.shaoguanga.properties.MwConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @Author WJH
 * @Description
 * @date 2021/4/7 16:52
 * @Email ibytecode2020@gmail.com
 */
@Service
public class SendService {

    private static final Logger log = LoggerFactory.getLogger(SendService.class);
    private static final int MAX_MOBILES = 5000;
    private static final int MAX_COUNT = 1000;
    private static final String SUCCESS_CODE = "0";
    private static final String AUTH_FAIL_CODE = "-100012";
    private final Pattern pattern = Pattern.compile("^(?:\\+?86)?1[3-9]\\d{9}$|^(?:0086)?1[3-9]\\d{9}$");

    @Autowired
    private HttpService httpService;

    @Autowired
    private MwConfig mwConfig;


    /**
     * 发送方法
     * @param reqBody
     * @return map {flag: false, rspcod: "", content: "", body: respBodies}
     * @throws IOException
     */
    public Map<String, Object> send(ReqBody reqBody) throws IOException {
        // 返回值
        Map<String, Object> result = new HashMap<>(4);
        // 响应的json数组
        List<RespBody> respBodies = new ArrayList<>();

        // 分批配置
        Integer count = mwConfig.getSplitCount();
        if (count == null || count <= 0 || count > MAX_COUNT) {
            count = MAX_COUNT;
        }

        // 号码数量校验
        String msg = checkMobileList(reqBody);
        if (!StringUtils.isEmpty(msg)) {
            result.put("flag", false);
            result.put("rspcod", msg);
            return result;
        }

        // 去重，非法手机号过滤
        Set<String> mobileSet = reqBody.getMobilesList();
        mobileSet.removeIf(m -> !pattern.matcher(m).matches());

        // 过滤后校验数量
        List<String> mobiles = new ArrayList<>(mobileSet);
        if (mobiles.size() <= 0) {
            result.put("flag", false);
            result.put("rspcod", SgConstant.NO_MOBILES);
            return result;
        }

        // 取第一个手机号发送
        String firstMobile = mobiles.get(0);
        String params = getSendParam(firstMobile, reqBody);
        String firstResp = httpService.send2GateKeeper(params, mwConfig.getSendSingle());

        // 第一次成功发送
        if (resolveResp(firstResp, result)) {
            result.put("flag", true);
            respBodies.add(createRespBodyWithResp(firstResp));
            StringBuilder sb = new StringBuilder();
            // 分批发送
            for (int i = 1; i < mobiles.size(); i++) {
                sb.append(mobiles.get(i)).append(",");
                if (i % count == 0) {
                    sb.deleteCharAt(sb.length() - 1);
                    String sendParam = getSendParam(sb.toString(), reqBody);
                    // 后续调用批量发送接口
                    String resp = httpService.send2GateKeeper(sendParam, mwConfig.getSendBatch());
                    respBodies.add(createRespBodyWithResp(resp));
                    sb.delete(0, sb.length());
                }
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
                String sendParam = getSendParam(sb.toString(), reqBody);
                String resp = httpService.send2GateKeeper(sendParam, mwConfig.getSendBatch());
                respBodies.add(createRespBodyWithResp(resp));
            }
            result.put("body", respBodies);
            return result;
        }
        result.put("flag", false);
        result.put("content", "调用边界接口发送失败，响应：" + firstResp);
        return result;
    }

    /**
     * 组装 网关接口需要的参数
     * @param mobile 手机号码字符串：156xxx,157xxx,158xxx
     * @param reqBody 请求体
     * @return 返回字符串 形如：userid=TEST01&pwd=123456
     */
    private String getSendParam(String mobile, ReqBody reqBody) {
        try {
            String userId = reqBody.getApId();
            String pwd = reqBody.getAppSecret();
            Map<String, Object> param = new LinkedHashMap<>();
            String content = reqBody.getContent();
            param.put("userid", URLEncoder.encode(userId, "UTF-8"));
            param.put("pwd", URLEncoder.encode(pwd, "UTF-8"));
            param.put("mobile", URLEncoder.encode(mobile, "UTF-8"));
            param.put("content", URLEncoder.encode(content, "UTF-8"));
            String temp = param.toString().replace(", ", "&");
            return temp.substring(1, temp.length() - 1);
        } catch (Exception e) {
            log.error("转param失败：", e);
            return null;
        }
    }

    /**
     * 判断手机号数量
     * @param reqBody 请求体
     * @return 返回对应错误码 TOO_MANY_MOBILES 或者 NO_MOBILES
     */
    private String checkMobileList(ReqBody reqBody) {
        String mobiles = reqBody.getMobiles();
        String[] mobileArr = mobiles.split(",");
        if (mobileArr.length > MAX_MOBILES) {
            return SgConstant.TOO_MANY_MOBILES;
        } else if (mobileArr.length <= 0) {
            return SgConstant.NO_MOBILES;
        }
        return null;
    }

    /**
     * 解析网关响应的字符串
     * @param resp 网关响应的字符串
     * @return 根据解析结果判断是否发送成功
     */
    private boolean resolveResp(String resp) {
        if (resp == null) {
            return false;
        }
        ObjectMapper om = new ObjectMapper();
        try {
            Map map = om.readValue(resp, Map.class);
            String result = String.valueOf(map.get("result"));
            String msgid = String.valueOf(map.get("msgid"));
            String custid = String.valueOf(map.get("custid"));
            if (SUCCESS_CODE.equals(result) && !StringUtils.isEmpty(msgid) && !StringUtils.isEmpty(custid)) {
                return true;
            }
        } catch (IOException e) {
            log.error("{}，解析响应失败：", resp, e);
        }
        return false;
    }

    /**
     * 解析网关响应的字符串，如果鉴权失败这设置响应码为 INVALID_USR_OR_PWD
     * @param resp 网关响应的字符串
     * @param res 返回给controller的map
     * @return 根据解析结果判断是否发送成功
     */
    private boolean resolveResp(String resp, Map<String, Object> res) {
        if (resp == null) {
            return false;
        }
        ObjectMapper om = new ObjectMapper();
        try {
            Map map = om.readValue(resp, Map.class);
            String result = String.valueOf(map.get("result"));
            String msgid = String.valueOf(map.get("msgid"));
            String custid = String.valueOf(map.get("custid"));
            if (SUCCESS_CODE.equals(result) && !StringUtils.isEmpty(msgid) && !StringUtils.isEmpty(custid)) {
                return true;
            } else if (AUTH_FAIL_CODE.equals(result)) {
                res.put("rspcod", SgConstant.INVALID_USR_OR_PWD);
                return false;
            }
        } catch (IOException e) {
            log.error("{}，解析响应失败：", resp, e);
        }
        return false;
    }

    /**
     * 根据网关响应的字符串构造 RespBody
     * @param resp 响应的字符串
     * @return RespBody
     * @throws IOException
     */
    private RespBody createRespBodyWithResp(String resp) throws IOException {
        ObjectMapper om = new ObjectMapper();
        RespBody respBody = new RespBody();
        if (resolveResp(resp)) {
            Map map = om.readValue(resp, Map.class);
            respBody.setMgsGroup(String.valueOf(map.get("msgid")));
            respBody.setSuccess(true);
            respBody.setRspcod("success");
            respBody.setCount(0);
        } else {
            // 边界响应异常
            respBody.setContent(-1);
            respBody.setSuccess(false);
            respBody.setContent(resp);
        }
        return respBody;
    }

}
