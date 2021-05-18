package com.montnets.emp.netnews.upload;

public interface OutputStreamListener {
	// 开始上传
	public void start();
    
	//读上传文件内容(字节)
	public void bytesRead(int bytesRead);
    
	//出现错误
	public void error(String message);
    
	//文件上传完成
	public void done();
}
