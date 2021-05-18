package com.montnets.emp.common.constant;

import java.io.Serializable;

/**
 * @author zousy
 * @project emp_std_192.169.1.81
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2015/12/11
 * @description
 */
public class RequestData implements Serializable{
    //请求方法
    private String method;
    //请求次数
    private int reqTimes;
    //首次请求时间
    private long firstReqTime;

    public RequestData() {
        this.reqTimes = 0;
    }

    public RequestData(String method, int reqTimes, long firstReqTime) {
        this.method = method;
        this.reqTimes = reqTimes;
        this.firstReqTime = firstReqTime;
    }

    public boolean isEmpty() {
        return reqTimes == 0;
    }

    public void push(String curMethod) {
        if (curMethod.equals(method)) {
            this.reqTimes = this.reqTimes + 1;
        } else {
            reset(curMethod);
        }
    }

    public boolean isFrequent(int allowMaxTimes, long allowMinMillis) {
        return this.reqTimes >= allowMaxTimes && isInTime(allowMinMillis);
    }

    public boolean isOverMax(int allowMaxTimes) {
        return this.reqTimes >= allowMaxTimes;
    }

    public boolean isInTime(long allowMinMillis) {
        return System.currentTimeMillis() - firstReqTime < allowMinMillis;
    }

    public void reset(String curMethod) {
        this.method = curMethod;
        this.reqTimes = 1;
        this.firstReqTime = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "RequestData{" +
                "method='" + method + '\'' +
                ", reqTimes=" + reqTimes +
                ", firstReqTime=" + firstReqTime +
                '}';
    }
}
