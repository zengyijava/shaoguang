package com.montnets.emp.wxgl.biz;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.lbs.LfLbsUserPios;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author yejiangmin <282905282@qq.com>
 * @description 处理用户地理位置线程
 * @project ott
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-12-18 上午09:18:36
 */
public class LbsPiosThread extends Thread {
    private final Map<String, LfLbsUserPios> piosMap;

    public LbsPiosThread(Map<String, LfLbsUserPios> piosMap) {
        this.piosMap = piosMap;
    }

    @Override
    public void run() {
        try {
            if (piosMap != null && piosMap.size() > 0) {
                List<String> removePios = new ArrayList<String>();
                Iterator<Map.Entry<String, LfLbsUserPios>> iter = piosMap.entrySet().iterator();
                Map.Entry<String, LfLbsUserPios> iternext = null;
                while (iter.hasNext()) {
                    iternext = iter.next();
                    //当前时间 减去  用户上传的地理位置时间 - 三分钟 ,>0  则 移出
                    if (System.currentTimeMillis() - iternext.getValue().getModifytime().getTime() - LbsPiosBiz.timeInterval > 0) {
                        removePios.add(iternext.getKey());
                    }
                }
                for (int i = 0; i < removePios.size(); i++) {
                    piosMap.remove(removePios.get(i).trim());
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "LbsPiosThread.run is error");
        }
        return;
    }

}
