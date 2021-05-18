package com.montnets.emp.perfect.biz;

import java.util.LinkedHashMap;

import com.montnets.emp.common.biz.receive.IReportStart;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.system.LfReport;
import com.montnets.emp.perfect.dao.PerfectDao;
/**
 * 
 * @description 完美通知状态报告
 * @project p_ydcx
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author yejiangmin <282905282@qq.com>
 * @datetime 2013-10-23 下午06:35:32
 */

public class PrefectNoticeStateStart  implements IReportStart{

	//private BaseBiz baseBiz = new BaseBiz();
	private PerfectDao perfectDao = new PerfectDao();
	public boolean start(LfReport report){
		try{
			//TODO  直接更新状态表
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			conditionMap.put("taskId", String.valueOf(report.getTaskId()));
			conditionMap.put("spUser", report.getSpid());
			conditionMap.put("mobile", report.getPhone());
			//没有接收状态
			conditionMap.put("isAtrred", "2");
			//已经接收状态报告
			objectMap.put("isReceive","0");
			
			//状态报告是成功的状态，则更新为成功
			if(report.getMtstat() != null 
			&& "DELIVRD".equals(report.getMtstat().trim().toUpperCase()) 
			&& report.getMterrorcode() != null 
			&& "000".equals(report.getMterrorcode()))
			{
				//状态报告为成功
				objectMap.put("isAtrred","1");
			}
			else{
				//状态报告为失败
				objectMap.put("isAtrred","4");
				EmpExecutionContext.error("完美通知接收状态报告，状态报告为失败。mtstat="+report.getMtstat()+",mterrcode="+report.getMterrorcode());
			}
			//这里是标准该 状态报告已回复了，该回复状态提示不需要更新
			//objectMap.put("isAtrred","1");
			//return baseBiz.update(LfPerfectNoticUp.class, objectMap, conditionMap);
			//以+号开头的国际号码,网关侧自动转为00开头，对+号开头的号码需要做特殊处理
			return perfectDao.setPrefectNoticeState(objectMap, conditionMap);
		}catch (Exception e) {
			EmpExecutionContext.error(e,"完美通知接收状态报告，更新失败！");
			return false;
		}finally{
			EmpExecutionContext.error("完美通知接收状态报告，详情 ：taskid "+report.getTaskId()+" phone："+report.getPhone());
		}
	}

}
