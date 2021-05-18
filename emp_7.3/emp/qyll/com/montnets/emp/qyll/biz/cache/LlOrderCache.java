package com.montnets.emp.qyll.biz.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import com.montnets.emp.qyll.entity.LlOrderDetail;

/**
 * 流量订购缓存
 * @author Administrator
 *
 */
public enum LlOrderCache {
	
	INSTANCE;
	
	private BlockingQueue<LlOrderDetail> orderQueue = new LinkedBlockingDeque<LlOrderDetail>();
	
	/**
	 * 入队列
	 * @param detaile
	 * @throws InterruptedException
	 */
	public void produce(LlOrderDetail detaile) throws InterruptedException{
		orderQueue.put(detaile);
	}
	
	/**
	 * 入队列
	 * @param detaile
	 * @throws InterruptedException
	 */
	public void produceList(List<LlOrderDetail> detaileList) throws InterruptedException{
		for(LlOrderDetail detaile : detaileList){
			orderQueue.put(detaile);
		}
	}
	
	
	
	/**
	 * 出队列
	 * @return
	 */
	public synchronized LlOrderDetail consume(){
		return this.orderQueue.poll();
	}
	
	/**
	 * 出队列，获取指定个数
	 * @param count 获取个数
	 * @return
	 */
	public List<LlOrderDetail> consumeOrders(int count){
		List<LlOrderDetail> list = new ArrayList<LlOrderDetail>();
		for(int i=0;i <= count; i++){
			LlOrderDetail detail = consume();
			if(null == detail){
				break;
			}
			list.add(detail);
		}
		return list;
	}
	
	/**
	 * 获取队列长度
	 * @return
	 */
	public int getSize(){
		return orderQueue.size();
	}
	
}

