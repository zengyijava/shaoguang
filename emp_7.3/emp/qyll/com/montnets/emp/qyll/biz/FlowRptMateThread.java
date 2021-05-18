package com.montnets.emp.qyll.biz;

import java.util.List;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.qyll.biz.cache.RptReceiveCache;
import com.montnets.emp.qyll.dao.LldgDao;
import com.montnets.emp.qyll.entity.LlOrderDetail;
import com.montnets.emp.util.Logger;

/**
 * 流量订购状态报告匹配线程
 * @author xiebk
 *
 */
public class FlowRptMateThread extends Thread {
	//流量Rpt缓存
	private RptReceiveCache rptCache = RptReceiveCache.INSTANCE;
	
	private boolean isExit = false;
	
	List<LlOrderDetail> listDet = null;
	
	LldgDao llDao = new LldgDao();
	
	private int count = 500;
	
	public FlowRptMateThread(){
		setName("流量订购状态报告匹配线程");
	}
	
	@Override
	public void run(){
			while(!isExit){
				try{
					//从缓存中获取状态报告
					listDet = rptCache.consumeOrders(count);
					
					//判断是否有缓存的状态报告
					if(listDet == null || listDet.isEmpty()){
						Thread.sleep(2000);
						continue;
					}
					
					if(!llDao.updateFlowRpt(listDet)){ //如果批量更新状态报告失败，尝试逐条更新
						//更新状态报告
						for(LlOrderDetail det : listDet){
							//为空不处理
							if(det == null || det.getOrderno() == null || det.getLlrpt() == null || det.getMobile() == null ){
								continue;
							}
							updateRpt(det);
						}
					}
					//处理完成，清除缓存数据
					listDet.clear();
					
				}catch (Exception e) {
					EmpExecutionContext.errorLog(e,"流量订购状态报告匹配线程异常！",Logger.LEVEL_ERROR);
				}
			}
	}
	
	public void updateRpt(LlOrderDetail det) throws InterruptedException{
		//更新失败，重新尝试3次
		int count = 0;
		while(!llDao.updateFlowRptOne(det)){
			Thread.sleep(2000);
			if(++count > 3){//3次更新失败后，将当前状态报告记录到异常表
				llDao.insertStatRpt(det);
				break;
			}
		}
		
	}
}
