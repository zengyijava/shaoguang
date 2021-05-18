package com.montnets.emp.rms.wbs.thread;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.rms.wbs.biz.ISvcContBiz;

/**
 * 数据转移线程
 * 
 * @ClassName TransferDataThread
 * @Description TODO
 * @author zhouxiangxian 203492752@qq.com
 * @date 2018年1月22日
 */
public class TransferDataThread implements Runnable {
	private ISvcContBiz svcContBiz;

	public TransferDataThread(ISvcContBiz svcContBiz) {
		this.svcContBiz = svcContBiz;
	}

	public void run() {
		try {
			// 数据转移处理
			//同步数据处理，为其加锁
			synchronized (this) {
				this.svcContBiz.transferDate();
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "Mboss富信同步接口调用,数据从临时表LF_RMS_SYN_TEMP转移到其他表中出错");
		}
	}

}
