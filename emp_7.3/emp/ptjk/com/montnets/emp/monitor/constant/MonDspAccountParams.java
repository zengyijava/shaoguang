package com.montnets.emp.monitor.constant;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.montnets.emp.entity.monitor.LfMonDspacinfo;

/**
 * SP账号动态信息表实体类
 * 
 * @description
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-12-30 下午01:54:51
 */
public class MonDspAccountParams extends LfMonDspacinfo
{

	
	/**KEY值说明
	 *1:SP启动告警标识
	 *2:帐号连接数告警标识	
	 *3:MT已转发量告警标识	
	 *4:MT滞留量告警标识	
	 *5:MT提交速度告警标识	
	 *6:MT下发速度告警标识	
	 *7:MO滞留量告警标识	
	 *8:MO最低接收率告警标识	
	 *9:Rpt滞留量告警标识	
	 *10:RPT接收速度告警标识	
	 *11:Rpt最低接收率告警标识
	 *12:在线状态告警标识
	 */
	private Map<Integer, Long> monThresholdFlag = new ConcurrentHashMap<Integer, Long>();
	
	private Map<String, Integer> spNodeMthavesndMap = new HashMap<String, Integer>();
	
	public MonDspAccountParams()
	{
	}

	public synchronized void setMonThresholdFlag(Map<Integer, Long> monThresholdFlag)
	{
		this.monThresholdFlag = monThresholdFlag;
	}

    public synchronized Map<Integer, Long> getMonThresholdFlag()
    {
        if(monThresholdFlag.isEmpty()){
            monThresholdFlag.put(1,getThresholdflag1());
            monThresholdFlag.put(2,getThresholdflag2());
            monThresholdFlag.put(3,getThresholdflag3());
            monThresholdFlag.put(4,getThresholdflag4());
            monThresholdFlag.put(5,getThresholdflag5());
            monThresholdFlag.put(6,getThresholdflag6());
            monThresholdFlag.put(7,getThresholdflag7());
            monThresholdFlag.put(8,getThresholdflag8());
            monThresholdFlag.put(9,getThresholdflag9());
            monThresholdFlag.put(10,getThresholdflag10());
            monThresholdFlag.put(11,getThresholdflag11());
            monThresholdFlag.put(12,getThresholdflag12());
            monThresholdFlag.put(13,getThresholdflag13());
            
            monThresholdFlag.put(14,getSendmailflag1());
            monThresholdFlag.put(15,getSendmailflag2());
            monThresholdFlag.put(16,getSendmailflag3());
            monThresholdFlag.put(17,getSendmailflag4());
            monThresholdFlag.put(18,getSendmailflag5());
            monThresholdFlag.put(19,getSendmailflag6());
            monThresholdFlag.put(20,getSendmailflag7());
            monThresholdFlag.put(21,getSendmailflag8());
            monThresholdFlag.put(22,getSendmailflag9());
            monThresholdFlag.put(23,getSendmailflag10());
            monThresholdFlag.put(24,getSendmailflag11());
            monThresholdFlag.put(25,getSendmailflag12());
            monThresholdFlag.put(26,getSendmailflag13());
        }
        return monThresholdFlag;
    }


    //设置告警标示
    public void initThresholdFlag(){
        this.setThresholdflag1(monThresholdFlag.get(1));
        this.setThresholdflag2(monThresholdFlag.get(2));
        this.setThresholdflag3(monThresholdFlag.get(3));
        this.setThresholdflag4(monThresholdFlag.get(4));
        this.setThresholdflag5(monThresholdFlag.get(5));
        this.setThresholdflag6(monThresholdFlag.get(6));
        this.setThresholdflag7(monThresholdFlag.get(7));
        this.setThresholdflag8(monThresholdFlag.get(8));
        this.setThresholdflag9(monThresholdFlag.get(9));
        this.setThresholdflag10(monThresholdFlag.get(10));
        this.setThresholdflag11(monThresholdFlag.get(11));
        this.setThresholdflag12(monThresholdFlag.get(12));
        this.setThresholdflag13(monThresholdFlag.get(13));
        
        this.setSendmailflag1(monThresholdFlag.get(14));
        this.setSendmailflag2(monThresholdFlag.get(15));
        this.setSendmailflag3(monThresholdFlag.get(16));
        this.setSendmailflag4(monThresholdFlag.get(17));
        this.setSendmailflag5(monThresholdFlag.get(18));
        this.setSendmailflag6(monThresholdFlag.get(19));
        this.setSendmailflag7(monThresholdFlag.get(20));
        this.setSendmailflag8(monThresholdFlag.get(21));
        this.setSendmailflag9(monThresholdFlag.get(22));
        this.setSendmailflag10(monThresholdFlag.get(23));
        this.setSendmailflag11(monThresholdFlag.get(24));
        this.setSendmailflag12(monThresholdFlag.get(25));
        this.setSendmailflag13(monThresholdFlag.get(26));
    }

	public Map<String, Integer> getSpNodeMthavesndMap()
	{
		return spNodeMthavesndMap;
	}

	public void setSpNodeMthavesndMap(Map<String, Integer> spNodeMthavesndMap)
	{
		this.spNodeMthavesndMap = spNodeMthavesndMap;
	}

}
