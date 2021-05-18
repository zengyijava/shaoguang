package com.montnets.emp.common.servlet;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.montnets.emp.common.biz.AccountInfoSyncBiz;
import com.montnets.emp.common.biz.EmpMonAlarmThread;
import com.montnets.emp.common.biz.HandleBlackThread;
import com.montnets.emp.common.biz.ScheduledBiz;
import com.montnets.emp.common.biz.ScheduledEmpMonBiz;
import com.montnets.emp.common.biz.SystemInitBiz;
import com.montnets.emp.common.biz.TimerLoadCache;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.ViewParams;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.timer.SpBalanceAlarmSmsTask;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
public class SystemInitServlet extends HttpServlet
{

	private static final long serialVersionUID = -1614005437488217583L;
	
	
	private SystemInitBiz initBiz = new SystemInitBiz();
	
	/**
	 * init方法
	 */
	public void init() throws ServletException {
		
		 try {
			//是否运行定时任务，1代表运行，0代表不运行
			if(StaticValue.ISRUNTIMEJOB==1){
				 //检测发送状态
				initBiz.checkSendState();
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "检测发送状态异常。");
		}
		
		try
		{
			//启动处理尾号定时任务
			initBiz.startDealSubno();
			//初始化定时任务
			initBiz.initTimer();
			//初始化号段(集合中的值设置为-1)
			new WgMsgConfigBiz().initNumSegmentType();
			//初始化关键字\黑名单\发送账号\号段信息
			new TimerLoadCache().initKeyBlackSpHD(true);
			//启动关键字\黑名单\发送账号\号段信息加载定时任务 
			new ScheduledBiz().startTimer();
			//上行接收
			initBiz.registerMoReceive();
			//初始化状态报告框架
			initBiz.initReveive();
			//获取控件信息
			initBiz.getPageField();
			
			
			//设置模块当前位置
			ViewParams.setPositionMap();
			//初始化全局变量
			initBiz.initGlobalVariable();
			//设置服务器操作系统类型
			initBiz.setServerSystemType();
			//启动删除指定目录及文件线程
			new Thread(new DeleteFolderFile()).start();
			//是否生成客户端监控文件 0:否；1:是
			if(StaticValue.getISCLIENTMONFILE() == 1)
			{
				//EMP服务器监控任务
				new ScheduledEmpMonBiz().startTimer();
				//EMP监控告警线程
				new Thread(new EmpMonAlarmThread()).start();
			}

			//单企业下
			if(StaticValue.getCORPTYPE() == 0)
            {
                //单独线程同步签名信息
                accountInfoSync();
            }

            //节点基础信息记录
            initBiz.UpdateNodeInfo();
            
            //启动公共数据处理线程
            initBiz.StartCommDataDealThread();
            //是否运行定时任务，1代表运行，0代表不运行
            if(StaticValue.ISRUNTIMEJOB==1)
            {
            	//启动SP账号余额阀值告警线程
            	new SpBalanceAlarmSmsTask().start();
            	//启动上行短信黑名单设置线程
            	new HandleBlackThread().start();
            }
            //系统临时目录
            File file = new File(System.getProperty("java.io.tmpdir"));
            //不存在则创建
            if(!file.exists())
            {
            	file.mkdirs();
            }
			//clearDB();
			//startCheckActiveUser();
			Calendar s=Calendar.getInstance();
			System.out.println("["+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(s.getTime())+"] EMP项目启动成功!");
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "启动初始化方法异常!");
		}
	}
	
	/**
	 * 销毁
	 */
	public void destroy() {
		super.destroy();
		initBiz.stopTimer();
		initBiz.stopDealSubno();
		initBiz.stopReveive();
		//停止公共数据处理线程
		initBiz.StopCommDataDealThread();
		StaticValue.setSpUserBalanceAlarmState(false);
		StaticValue.setEmpThreadrunState(false);
	}

	private boolean isshutdown = false;
	
	public void shutdown(){
		this.isshutdown = true;
	}
	
    /**
     * 线程启动签名同步
     */
    public void accountInfoSync(){
        new Thread(new Runnable() {
            //初始时间间隔 单位：分
            int initInterval = 10;
            //每次执行周期增加时长
            int step = 5;
            //最大周期
            int maxInterval = 120;
            public void run() {
                //执行间隔
                int interval = initInterval;
                while (!isshutdown){
                    //签名同步操作
                    new AccountInfoSyncBiz().accountInfoSync();
                    try {
                        EmpExecutionContext.info("签名同步线程"+interval+"分钟后再次运行！");
                        Thread.sleep((long)interval*60*1000);
                    } catch (InterruptedException e) {
                        EmpExecutionContext.error("签名同步操作线程等待异常!");
                        Thread.currentThread().interrupt();
                    }
                    //每次执行 周期增加5分钟
                    interval += step;
                    //周期大于2小时 重置为10分钟
                    if(interval > maxInterval){
                        interval = initInterval;
                    }
                }
            }
        }).start();
        EmpExecutionContext.info("签名同步线程启动成功！");
    }
	

}
