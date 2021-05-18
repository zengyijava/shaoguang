package test;


import com.montnets.emp.common.context.EmpExecutionContext;

public class TestThread
{
	Storage storage = new Storage();
	private void setDealUserThread(){
		class _cls1DealUserThread extends Thread
		{
			@Override
            public final void run()
			{
				while(true){
					System.out.println("开始");
					storage.consume(1);
					System.out.println("处理完");
				}
				
			}

		}

		_cls1DealUserThread heartbeatThread = new _cls1DealUserThread();
		heartbeatThread.setName("处理用户同步线程");
		heartbeatThread.start();
	}
	
	private void settestThread(){
		class _cls1testThread extends Thread
		{
			@Override
            public final void run()
			{

				try {
					Thread.sleep(10*1000L);
				} catch (InterruptedException e) {
					EmpExecutionContext.error(e, "线程中断");
					Thread.currentThread().interrupt();
				}
				System.out.println("休眠结束");
				storage.produce(10);
				
			}

		}

		_cls1testThread heartbeatThread = new _cls1testThread();
		heartbeatThread.setName("test");
		heartbeatThread.start();
	}
	
	public static void main(String[] args)
	{
		TestThread t = new TestThread();
		t.setDealUserThread();
		t.settestThread();
	}
	
}
