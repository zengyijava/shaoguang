package com.montnets.emp.shorturl.surlmanage.biz;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.shorturl.comm.constant.UrlGlobals;
import com.montnets.emp.shorturl.surlmanage.dao.UrlSendDao;
import com.montnets.emp.shorturl.surlmanage.entity.LfUrlTask;
import com.montnets.emp.shorturl.surlmanage.thread.Listener;
import com.montnets.emp.shorturl.surlmanage.thread.UrlSendThread;
import com.montnets.emp.shorturl.surlmanage.thread.UrlThread;
import com.montnets.emp.shorturl.surlmanage.thread.WriteUrlTask;

public class UrlTimerManagerBiz {
	private UrlTimerManagerBiz() {
	}

	// 定时器实例
	private static UrlTimerManagerBiz tm_instance = new UrlTimerManagerBiz();

	// 任务执行线程池对象
	private ExecutorService exec = null;

	private UrlSendDao dao = new UrlSendDao();

	protected static final LinkedBlockingQueue<LfUrlTask> taskQueue = new LinkedBlockingQueue<LfUrlTask>(1000);
	
	protected static final ConcurrentHashMap<Long, Long> taskMap = new ConcurrentHashMap<Long, Long>();
	
	

	public static LinkedBlockingQueue<LfUrlTask> getTaskqueue() {
		return taskQueue;
	}

	public static ConcurrentHashMap<Long, Long> getTaskmap() {
		return taskMap;
	}

	/**
	 * 获取实例
	 * 
	 * @return
	 */
	public static UrlTimerManagerBiz getTMInstance() {
		// 返回实例
		return tm_instance;
	}

	// 初始化短链接发送所需的所有定时服务
	private void initTimer() {
		initParam();
		//initAreaCode();
		// 根据配置大小初始化缓存
		//loadCache();
		// 初始化短链接发送状态、处理中的改为未处理
		initHandleStatus();

		// 加载队列定时刷新
		//loadQueue();
		// 短连接统计数据定时刷新
		// loadCountTimer();
		// TOWR 短链接错误数据定时刷新
		loadReadTimer();
		
		loadSendTimer();
	}



	private void loadSendTimer() {
		Listener listener = new Listener();
		UrlSendThread urlThread = new UrlSendThread();
		urlThread.addObserver(listener);
		new Thread(urlThread).start();
		EmpExecutionContext.info("启动短链接发送任务处理");
	}

	private void loadReadTimer() {
		Listener listener = new Listener();
		UrlThread urlThread = new UrlThread();
		urlThread.addObserver(listener);
		new Thread(urlThread).start();
		EmpExecutionContext.info("启动短链接定时读取任务处理");

	}

//	private void loadCountTimer() {
//		Listener listener = new Listener();
//		CountUrlThread countUrlThread = new CountUrlThread();
//		countUrlThread.addObserver(listener);
//		new Thread(countUrlThread).start();
//		EmpExecutionContext.info("启动短链接定时数据统计");
//	}

//	private void loadQueue() {
//		// TOWR 集群情况下需考虑每个节点的短链接生成顺序
//		Listener listener = new Listener();
//		CreateUrlThread createUrlThread = new CreateUrlThread();
//		createUrlThread.addObserver(listener);
//		new Thread(createUrlThread).start();
//		EmpExecutionContext.info("启动向队列加载短链接数据");
//
//	}

	/**
	 * 初始化短链接发送任务状态,处理中变成未处理
	 */
	private void initHandleStatus() {
		// TOWR 集群情况下要确认由哪个节点执行，或者每个节点都只初始化带有自己节点标识的数据
		try {
			int num = dao.initHandleStatus();
			EmpExecutionContext.info("初始化短链接发送任务状态成功,修改任务状态个数：" + num);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "初始化短链接发送任务状态异常");
		}

	}

//	private void loadCache() {
//		// 获取缓存实例
//		LoadingCache<String, LfUrlVisit> cache = UrlCache.getInstance()
//				.getCache();
//		// 从数据库中查找短链接数据
//		List<LfUrlVisit> details = dao.getUrlDetails();
//
//		if (details.isEmpty()) {
//			EmpExecutionContext.info("初始化加载缓存个数为0，数据库中没有当月短链接数据");
//			return;
//		}
//		for (LfUrlVisit lfUrlDetail : details) {
//			cache.put(lfUrlDetail.getShortUrl(), lfUrlDetail);
//		}
//		EmpExecutionContext.info("初始化缓存成功，加载个数：" + details.size());
//
//	}

	/**
	 * 启动定时器
	 */
	public void StartTimer() {
		// 是否运行定时任务，1代表运行，0代表不运行

		this.initTimer();
	}

	private boolean initParam() {
		try {
			// 创建定时器
			// timer = Executors.newScheduledThreadPool(1);
			// 创建执行任务线程池
			exec = new ThreadPoolExecutor(UrlGlobals.getCORE_POOL_SIZE(),
					UrlGlobals.getMAX_POOL_SIZE(), UrlGlobals.getKEEP_ALIVE_TIME(),
					TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
			
			
			return true;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "定时器初始化参数异常。");
			return false;
		}
	}

	public boolean execute(LfUrlTask urlTask) {
		try {
			exec.execute(new WriteUrlTask(urlTask));
			return true;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "短链接任务执行失败");
			return false;
		}
	}
}
