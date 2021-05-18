package com.montnets.emp.monitor.constant;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.montnets.emp.entity.monitor.LfMonDhost;

/**
 * 主机动态信息实体类
 * 
 * @description
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-12-30 上午10:02:01
 */
public class MonDhostParams extends LfMonDhost {


    //主机更新标识,第一位为:1000网络信息,第二位为：1001资源占用,第三位：1002硬盘空间信息，为1表示数据已正确获取
    private String hostUpdateState = "000";

    /**
     * KEY值说明
     * 1：主机启动告警标识
     * 2:CPU占用量告警标识
     * 3:物理内存使用量告警标识
     * 4:虚拟内存使用量告警标识
     * 5:磁盘空间使用量告警标识
     * 6:进程总数告警标识
     */
    private Map<Integer, Long> monThresholdFlag = new ConcurrentHashMap<Integer, Long>();

    public MonDhostParams() {
    }

    public String getHostUpdateState() {
        return hostUpdateState;
    }

    public void setHostUpdateState(String hostUpdateState) {
        this.hostUpdateState = hostUpdateState;
    }

    public synchronized void setMonThresholdFlag(Map<Integer, Long> monThresholdFlagMap) {
        this.monThresholdFlag = monThresholdFlagMap;
    }

    public synchronized Map<Integer, Long> getMonThresholdFlag() {
        if(monThresholdFlag.isEmpty()){
            monThresholdFlag.put(1,getThresholdFlag1());
            monThresholdFlag.put(2,getThresholdFlag2());
            monThresholdFlag.put(3,getThresholdFlag3());
            monThresholdFlag.put(4,getThresholdFlag4());
            monThresholdFlag.put(5,getThresholdFlag5());
            monThresholdFlag.put(6,getThresholdFlag6());
            
            monThresholdFlag.put(7,getSendmailflag1());
            monThresholdFlag.put(8,getSendmailflag2());
            monThresholdFlag.put(9,getSendmailflag3());
            monThresholdFlag.put(10,getSendmailflag4());
            monThresholdFlag.put(11,getSendmailflag5());
            monThresholdFlag.put(12,getSendmailflag6());

        }
        return monThresholdFlag;
    }

    //设置告警标示
    public void initThresholdFlag(){
        this.setThresholdFlag1(monThresholdFlag.get(1));
        this.setThresholdFlag2(monThresholdFlag.get(2));
        this.setThresholdFlag3(monThresholdFlag.get(3));
        this.setThresholdFlag4(monThresholdFlag.get(4));
        this.setThresholdFlag5(monThresholdFlag.get(5));
        this.setThresholdFlag6(monThresholdFlag.get(6));
        
        this.setSendmailflag1(monThresholdFlag.get(7));
        this.setSendmailflag2(monThresholdFlag.get(8));
        this.setSendmailflag3(monThresholdFlag.get(9));
        this.setSendmailflag4(monThresholdFlag.get(10));
        this.setSendmailflag5(monThresholdFlag.get(11));
        this.setSendmailflag6(monThresholdFlag.get(12));
    }
}
