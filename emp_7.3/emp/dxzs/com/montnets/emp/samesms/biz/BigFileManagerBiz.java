package com.montnets.emp.samesms.biz;

import java.io.BufferedReader;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.montnets.emp.common.constant.PreviewParams;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.sms.LfBigFile;
import com.montnets.emp.samesms.thread.BigFileSplitTask;

public class BigFileManagerBiz {
	// 任务执行线程池对象
	private ExecutorService exec = null;
	/**
	 * 初始化2个线程
	 * 最大支持100个线程
	 * 空闲线程存活100秒
	 */
	private BigFileManagerBiz(){
		exec = new ThreadPoolExecutor(2,
				100, 100L,
				TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
		
	}
	private static BigFileManagerBiz instance = new BigFileManagerBiz();
	
	/**
	 * 获取实例
	 * 
	 * @return
	 */
	public static BigFileManagerBiz getTMInstance() {
		if (instance == null) {
			instance = new BigFileManagerBiz();
		}
		// 返回实例
		return instance;
	}
	
	
	public boolean execute(LfBigFile bigFile, List<BufferedReader> readerList, PreviewParams preParams,String langName) {
		try {
			exec.execute(new BigFileSplitTask(bigFile,readerList,preParams,langName));
			return true;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "短链接任务执行失败");
			return false;
		}
	}
}
