package com.montnets.shaoguanga.bean;

import lombok.Data;

/**
 * @param
 * @ClassName SendRpts
 * @Author zengyi
 * @Description
 * @Date 2021/4/13 15:37
 **/
@Data
public class SendRpts {
    private String reportStatus;
    private String mobile;
    private String submitDate;
    private String receiveDate;
    private String errorCode;
    private String msgGroup;

}
