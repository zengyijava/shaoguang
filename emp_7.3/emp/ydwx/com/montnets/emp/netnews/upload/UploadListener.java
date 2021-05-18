package com.montnets.emp.netnews.upload;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import com.montnets.emp.common.context.EmpExecutionContext;

public class UploadListener implements OutputStreamListener ,Serializable{
	// 保存了一个Http Request对象
	private HttpServletRequest request;

	// 延迟时间
	private long delay = 0;

	// 开始上传的时间
	private long startTime = 0;

	// 上传文件的大小（所有文件大小的和)
	private int totalToRead = 0;

	// 已经读取的文件内容的大小
	private int totalBytesRead = 0;

	// 上传文件的数量
	private int totalFiles = -1;

	public UploadListener(HttpServletRequest request, long debugDelay) { 
		// 监听可能要使用到request对象
		this.request = request;
		this.delay = debugDelay;
		
		//使用request对象获得文件内容的总体大小
		totalToRead = request.getContentLength();
		this.startTime = System.currentTimeMillis();
	}

	public void start() {
		//没上传一个文件会执行一次
		totalFiles++;
		updateUploadInfo("start");
	}

	public void bytesRead(int bytesRead) {
		totalBytesRead = totalBytesRead + bytesRead;
		updateUploadInfo("progress");

		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			EmpExecutionContext.error(e, "线程执行异常！");
			Thread.currentThread().interrupt();
		}
	}

	public void error(String message) {
		updateUploadInfo("error");
	}

	public void done() {
		updateUploadInfo("done");
	}

	@SuppressWarnings("unused")
	private long getDelta() {
		return (System.currentTimeMillis() - startTime) / 1000;
	}

	private void updateUploadInfo(String status) {
		
		long delta = (System.currentTimeMillis() - startTime) / 1000;
		//客户端的进度条是通过session里的UploadInfo对象来确定
			try{
				request.getSession(false).setAttribute("uploadInfo",new UploadInfo(totalFiles, totalToRead, totalBytesRead, delta,status));
			}catch (Exception e1) {
				EmpExecutionContext.error(e1,"从Session取出信息出现异常！");
			}
		}

}
