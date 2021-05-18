package com.montnets.emp.qyll.biz.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import com.montnets.emp.qyll.entity.LlOrderDetail;

/** 
* @ClassName: RptReceiveCache 
* @Description: 状态报告接收缓存类 
* @author xuty  
* @date 2017-11-13 上午10:20:40 
*  
*/
public enum RptReceiveCache {
	INSTANCE;
	private BlockingQueue<LlOrderDetail> rptQueue = new LinkedBlockingDeque<LlOrderDetail>();
	
	/**
	 * 入队列
	 * @param detaile
	 * @throws InterruptedException
	 */
	public void produce(LlOrderDetail detaile) throws InterruptedException{
		rptQueue.put(detaile);
	}
	
	/**
	 * 出队列
	 * @return
	 */
	public synchronized LlOrderDetail consume(){
		return this.rptQueue.poll();
	}
	
	/**
	 * 出队列，获取指定个数(获取的队列中订单编号的年份相同)
	 * @param count 获取个数
	 * @return
	 * @throws InterruptedException 
	 */
	public List<LlOrderDetail> consumeOrders(int count) throws InterruptedException{
		List<LlOrderDetail> list = new ArrayList<LlOrderDetail>();
		StringBuffer sb = new StringBuffer();
		//第一次获取的订单编号中的年份：订单编号格式[EMP]YYYYMMDD+序列码
		String beginYear = "";
		boolean fistTm = true;
		for(int i=0;i <= count; i++){
			LlOrderDetail detail = consume();
			if(null == detail){
				break;
			}
			sb.delete(0, sb.length())
				.append(detail.getOrderno());
			
			if(sb.length() > 0 ){
				if(sb.toString().startsWith("EMP")){//以EMP开头
					if(fistTm){
						beginYear = sb.substring(3,7);//EMP2017xxxxxxx
						fistTm = false;
					}
					if(!beginYear.equals(sb.substring(3,7))){
						//不是同一年，重新放回队列，退出
						produce(detail);
						break;
					}
				}else{
					if(fistTm){
						beginYear = sb.substring(0,4);//2017xxxxxxx
						fistTm = false;
					}
					if(!beginYear.equals(sb.substring(0,4))){
						//不是同一年，重新放回队列，退出
						produce(detail);
						break;
					}
				}
			}
			list.add(detail);
		}
		return list;
	}
}
