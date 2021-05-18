package com.montnets.shaoguanga.bean;

import lombok.Data;

/**
 * @param
 * @ClassName GetRpts
 * @Author zengyi
 * @Description
 * @Date 2021/4/13 15:36
 **/
@Data
public class GetRpts {
    private Integer msgid;
    private String custid;
    private Integer pknum;
    private Integer pktotal;
    private String mobile;
    private String spno;
    private String exno;
    private String stime;
    private String rtime;
    private Integer status;
    private String errcode;
    private String errdesc;
    private String exdata;



}
