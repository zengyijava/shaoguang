package com.montnets.emp.appwg.biz;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.montnets.emp.appwg.initappplat.AppSdkPackage;
import com.montnets.emp.common.context.EmpExecutionContext;

/**
 * 
 * @project emp_std_192.169.1.81
 * @author huangzb
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2014-7-21 下午03:28:37
 * @description 用户处理仓库
 */
public class UserDealStorage
{

	// 仓库存储的载体
	private static LinkedBlockingQueue<String> list = new LinkedBlockingQueue<String>();

	/**
	 * 生产用户账号
	 * @param appcode 账号
	 * @param ecode 企业编码
	 */
	public static void produce(String appcode, String ecode)
	{
		try
		{
			// 放入产品，自动阻塞
			list.put(appcode);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "生产用户账号异常。");
		}

	}

	/**
	 * 消费用户账号
	 */
	public void consume()
	{
		try
		{
			// 消费产品，自动阻塞
			String appcode = list.poll(20, TimeUnit.SECONDS);
			if(appcode == null){
				return;
			}
			
			//判断用户账号是否已有
			boolean containRes = AppSdkPackage.getInstance().getUserCodeMap().containsKey(appcode);
			//已经有，就不用同步
			if(containRes){
				return;
			}
			
			EmpExecutionContext.info("同步用户线程，发现用户不存在，同步用户。用户账号："+appcode);
			//没有这个用户，则同步
			WgMwCommuteBiz wgBiz = new WgMwCommuteBiz();
			String ecode = wgBiz.getAppECode();
			//同步用户
			wgBiz.SynUserInfo(appcode, ecode);
		}
		catch (InterruptedException e)
		{
			EmpExecutionContext.error(e, "消费用户账号异常。");
			Thread.currentThread().interrupt();
		}

	}


}
