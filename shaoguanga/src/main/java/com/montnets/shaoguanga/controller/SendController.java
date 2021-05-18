package com.montnets.shaoguanga.controller;

import com.montnets.shaoguanga.bean.ReqBody;
import com.montnets.shaoguanga.bean.RespBody;
import com.montnets.shaoguanga.constant.SgConstant;
import com.montnets.shaoguanga.service.SendService;
import com.montnets.shaoguanga.util.Md5Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @Author WJH
 * @Description
 * @date 2021/4/7 15:00
 * @Email ibytecode2020@gmail.com
 */
@RestController
public class SendController {

    private static final Logger log = LoggerFactory.getLogger(SendController.class);

    @Autowired
    private SendService sendService;

    @PostMapping("/send")
    public List<RespBody> send(@Validated @RequestBody ReqBody reqBody) throws Exception {
        log.info("请求下行接口参数：{}", reqBody);
        RespBody respBody = new RespBody();
        if (!checkMac(reqBody)) {
            respBody.setCount(-1);
            respBody.setSuccess(false);
            respBody.setRspcod(SgConstant.ILLEGAL_MAC);
            respBody.setContent("mac 校验不通过");
            return Collections.singletonList(respBody);
        }
        Map<String, Object> result = sendService.send(reqBody);
        boolean flag = (boolean) result.get("flag");
        if (flag) {
            return (List<RespBody>) result.get("body");
        } else {
            respBody.setCount(-1);
            respBody.setSuccess(false);
            respBody.setRspcod((String) result.get("rspcod"));
            respBody.setContent(result.get("content"));
            return Collections.singletonList(respBody);
        }

    }

    /**
     * 校验 mac
     * @param reqBody
     * @return
     */
    private boolean checkMac(ReqBody reqBody) {
        String mac = reqBody.getMac();
        String sign = StringUtils.isEmpty(reqBody.getSign()) ? "" : reqBody.getSign();
        String addSerial = StringUtils.isEmpty(reqBody.getAddSerial()) ? "" : reqBody.getAddSerial();
        String temp = reqBody.getEcName() +
                reqBody.getApId() + reqBody.getAppSecret() + reqBody.getMobiles() + reqBody.getContent() +
                sign + addSerial;
        log.info("生成的mac码:{}", Md5Utils.getMd5(temp));
        return mac.equals(Md5Utils.getMd5(temp));
    }


}
