package com.montnets.emp.monitor.constant;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.montnets.emp.entity.monitor.LfMonDproce;

public class MonDproceParams extends LfMonDproce
{


	
	/*KEY值说明
	 *1：程序启动告警标识
	 *2:CPU占用量告警标识
	 *3:物理内存使用量告警标识
	 *4:虚拟内存使用量告警标识
	 *5:磁盘空间使用量告警标识
	 */
	private Map<Integer, Long> monThresholdFlag = new ConcurrentHashMap<Integer, Long>();

	public synchronized Map<Integer, Long> getMonThresholdFlag()
	{
        if(monThresholdFlag.isEmpty()){
            monThresholdFlag.put(1,getThresholdflag1());
            monThresholdFlag.put(2,getThresholdflag2());
            monThresholdFlag.put(3,getThresholdflag3());
            monThresholdFlag.put(4,getThresholdflag4());
            monThresholdFlag.put(5,getThresholdflag5());
            monThresholdFlag.put(6,getThresholdflag6());
            
            monThresholdFlag.put(7,getSendmailflag1());
            monThresholdFlag.put(8,getSendmailflag2());
            monThresholdFlag.put(9,getSendmailflag3());
            monThresholdFlag.put(10,getSendmailflag4());
            monThresholdFlag.put(11,getSendmailflag5());
            monThresholdFlag.put(12,getSendmailflag6());
        }
		return monThresholdFlag;
	}

	public synchronized void setMonThresholdFlag(Map<Integer, Long> monThresholdFlag)
	{
		this.monThresholdFlag = monThresholdFlag;
	}

    //设置告警标示
    public void initThresholdFlag(){
        this.setThresholdflag1(monThresholdFlag.get(1));
        this.setThresholdflag2(monThresholdFlag.get(2));
        this.setThresholdflag3(monThresholdFlag.get(3));
        this.setThresholdflag4(monThresholdFlag.get(4));
        this.setThresholdflag5(monThresholdFlag.get(5));
        this.setThresholdflag6(monThresholdFlag.get(6));
        
        this.setSendmailflag1(monThresholdFlag.get(7));
        this.setSendmailflag2(monThresholdFlag.get(8));
        this.setSendmailflag3(monThresholdFlag.get(9));
        this.setSendmailflag4(monThresholdFlag.get(10));
        this.setSendmailflag5(monThresholdFlag.get(11));
        this.setSendmailflag6(monThresholdFlag.get(12));
    }

}
