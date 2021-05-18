package test;
import com.montnets.emp.common.context.EmpExecutionContext;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 仓库类Storage实现缓冲区
 * 
 * Email:530025983@qq.com
 * 
 * @author MONKEY.D.MENG 2011-03-15
 * 
 */
public class Storage
{
	// 仓库最大存储量
	private final int MAX_SIZE = 100;

	// 仓库存储的载体
	private LinkedBlockingQueue<String> list = new LinkedBlockingQueue<String>();

	// 生产num个产品
	public void produce(int num)
	{
		// 如果仓库剩余容量为0
		if (list.size() == MAX_SIZE)
		{
			System.out.println("【库存量】:" + MAX_SIZE + "/t暂时不能执行生产任务!");
		}

		// 生产条件满足情况下，生产num个产品
		try
		{
			for (int i = 1; i <= num; ++i)
			{
				// 放入产品，自动阻塞
				list.put(i+"");
			}

			System.out.println("【现仓储量为】:" + list.size());
		}catch (InterruptedException e)
		{
			EmpExecutionContext.error(e, "线程异常");
			Thread.currentThread().interrupt();
		}
	}

	// 消费num个产品
	public void consume(int num)
	{
		// 如果仓库存储量不足
		if (list.size() == 0)
		{
			System.out.println("【库存量】:0/t暂时不能执行生产任务!");
		}

		// 消费条件满足情况下，消费num个产品
			try{
				for (int i = 1; i <= num; ++i)
				{
						// 消费产品，自动阻塞
						String str = list.take();
						System.out.println(str);
					}
				}
			catch (InterruptedException e)
			{
				EmpExecutionContext.error(e, "线程异常");
				Thread.currentThread().interrupt();
			}

		System.out.println("【现仓储量为】:" + list.size());
	}

	// set/get方法
	public LinkedBlockingQueue<String> getList()
	{
		return list;
	}

	public void setList(LinkedBlockingQueue<String> list)
	{
		this.list = list;
	}

	public int getMAX_SIZE()
	{
		return MAX_SIZE;
	}
}
