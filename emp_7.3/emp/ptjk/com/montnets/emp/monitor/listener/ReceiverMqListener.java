/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2013-12-6 下午05:17:31
 */
package com.montnets.emp.monitor.listener;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.monitor.biz.GateAccountFeeBiz;
import com.montnets.emp.monitor.biz.MonDataAnalysisBiz;
import com.montnets.emp.monitor.biz.ParsetMqXmlBiz;
import com.montnets.emp.monitor.constant.MonitorStaticValue;
import com.montnets.emp.util.TxtFileUtil;

/**
 * @description
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2013-12-6 下午05:17:31
 */

public class ReceiverMqListener implements MessageListener, Runnable
{
	// ConnectionFactory ：连接工厂，JMS 用它创建连接
	private ConnectionFactory	connectionFactory = null;

	// Connection ：JMS 客户端到JMS Provider 的连接
	private Connection			connection	= null;

	// Session： 一个发送或接收消息的线程
	private Session				session		= null;

	// Destination ：消息的目的地;消息发送给谁.
	private Destination			destination	= null;

	// 消费者，消息接收者
	private MessageConsumer		consumer	= null;
	
	// 心跳消息队列名
	private Destination queue = null;
	// 创建生产者
	private MessageProducer producer = null;
	// 消息内容
	private TextMessage sendMessage = null;

	ParsetMqXmlBiz				parsetMq	= new ParsetMqXmlBiz();
	
	//通道账号费用线程启动标识
	boolean isGateAccountFee = false;
	
	//监控EMP程序线程启动标识
	boolean isMonEmpInfo = false;
	
	//数据分析线程标识
	boolean isMonDataAnalysis = false;
	
	//线程是否关闭
	private boolean isShutdown = false;
	
	public void shutdown(){
		this.isShutdown = true;
	}

	/**
	 * 启动连接消息服务器
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2013-12-30 上午10:22:14
	 */
	private boolean connectionMQ()
	{
		boolean isResult = false;
		try
		{
			// 构造从工厂得到连接对象(删除JAR包,屏蔽相关代码)
			//connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD, "tcp://"+StaticValue.MQ_SERVER_URL);
			// 获取操作连接
			connection = connectionFactory.createConnection();
			// 启动
			connection.start();
			// 获取session
			session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
			// 消息队列名
			destination = session.createQueue(StaticValue.getMqQueueName());
			// 获取消费者
			consumer = session.createConsumer(destination);
			
			/*心跳设置*/
			// 心跳消息队列名
			queue = session.createQueue("testMonitorQueue");
			// 创建生产者
			producer = session.createProducer(queue);
            // 设置消息非持久化
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			// 消息内容
			sendMessage = session.createTextMessage("监听消息中心服务器心跳测试！");
			// 设置消息过期时间(秒)
			producer.setTimeToLive(30);
			// 启动成功
			isResult = true;
			//设置告警标识为正常
			MonitorStaticValue.setMQServerFlag(0);
			// 设置通道账号费用信息
			if(!isGateAccountFee)
			{
				new Thread(new GateAccountFeeBiz()).start();
				isGateAccountFee = true;
			}
			// 启动监控EMP程序线程
			if(!isMonEmpInfo)
			{
				//加载动态库文件,兼容websphere
				String libPath = new TxtFileUtil().getWebRoot()+"WEB-INF/lib";
				System.setProperty("java.library.path", libPath);
//				new Thread(new MonEmpInfoBiz()).start();
				isMonEmpInfo = true;
			}
			
			if(!isMonDataAnalysis)
			{
				// 启动数据分析线程
				new Thread(new MonDataAnalysisBiz()).start();
				isMonDataAnalysis = true;
			}
		}
		catch (Exception e)
		{
			// 启动失败
			isResult = false;
			//处理状态不是处理中
			if(MonitorStaticValue.getMQServerFlag() != -1)
			{
				// 设置告警信息
				String msg = "连接消息中心服务器失败，服务器未启动或网络异常!";
				setAlarmInfo(msg);
			}
			EmpExecutionContext.error("初始化连接消息服务器失败!消息服务器URL:tcp://" + StaticValue.getMqServerUrl());
			if(null != connection )
			{
				try
				{
					connection.close();
					connection = null;
				}
				catch (JMSException e1)
				{
					EmpExecutionContext.error(e1, "关闭消息中心服务器连接失败！");
				}
			}
		}
		return isResult;
	}

	/**
	 * 设置告警信息
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-7 下午04:59:50
	 */
	private void setAlarmInfo(String msg)
	{
		try
		{
			// 错误告警信息
			//LfMonErr lfMonErr = new LfMonErr();
			//MonErrorInfo monErrorInfo = new MonErrorInfo();
			// 设置告警信息
			//monErrorInfo.setHostErrObjectInfo(lfMonErr, -2L, -2L, "0", "0", 5600, 1, new Timestamp(System.currentTimeMillis()));
			// 保存告警信息
			//monErrorInfo.addErrorInfo(lfMonErr, msg, 0, 2);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置消息服务器告警信息异常！");
		}
	}

	/**
	 * 监听消息队列
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2013-12-30 上午10:24:56
	 */
	public void consumerMessage()
	{
		// 启动连接消息服务器
		if(connectionMQ())
		{
			try
			{
				// 监听队列消息
				consumer.setMessageListener(this);
			}
			catch (JMSException e)
			{
				EmpExecutionContext.error(e, "监听消息队列失败!");
			}
		}
		// 未启动MQ消息服务器或连接失败,30秒后再次启动连接
		else
		{
			try
			{
				Thread.sleep(30000L);
			}
			catch (InterruptedException e)
			{
				EmpExecutionContext.error("连接消息服务器等待线程异常!");
				Thread.currentThread().interrupt();
			}
			consumerMessage();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2013-9-26 上午11:23:48
	 */
	public void onMessage(Message message)
	{
		try
		{
			if(message instanceof TextMessage)
			{
				// 解析消息
				parsetMq.paresetStringXml(((TextMessage) message).getText());
			}
		}
		catch (JMSException e)
		{
			EmpExecutionContext.error(e, "接收消息队列失败!");
		}

	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2013-9-26 上午11:23:48
	 */
	public void run()
	{
		// 初始化连接消息中心服务器,启动监听
		consumerMessage();
		while(!isShutdown)
		{
			try
			{
				Thread.sleep(50000L);
			}
			catch (InterruptedException e)
			{
				EmpExecutionContext.error("监听消息中心服务器线程等待异常!");
				Thread.currentThread().interrupt();
			}
			try
			{
				// 发送消息
				producer.send(sendMessage);
				//设置告警标识为正常
				MonitorStaticValue.setMQServerFlag(0);
			}
			catch (JMSException e)
			{
				//处理状态不是处理中
				if(MonitorStaticValue.getMQServerFlag() != -1)
				{
					// 设置告警信息
					String msg = "监听消息中心服务器心跳异常!";
					setAlarmInfo(msg);
				}
				EmpExecutionContext.error(e, "消息中心服务器未启动!");
				// 启动监听
				consumerMessage();
			}
		}

	}
}
