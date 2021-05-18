package com.montnets.emp.netnews.upload;

import java.io.OutputStream;
import java.io.IOException;
//download by http://www.codefans.net

public class MonitoredOutputStream extends OutputStream
{
    //正真的输出流对象
	private OutputStream target;
	//监听输出流对象的一个监听器
    private OutputStreamListener listener;

    public MonitoredOutputStream(OutputStream target, OutputStreamListener listener)
    {
        this.target = target;
        this.listener = listener;
        //启动监听器
        this.listener.start();
    }

    public void write(byte b[], int off, int len) throws IOException
    {  
    	//读输出流的内容
        target.write(b,off,len);
        //监听器在记录
        listener.bytesRead(len - off);
    }

    public void write(byte b[]) throws IOException
    {
        target.write(b);
        listener.bytesRead(b.length);
    }

    public void write(int b) throws IOException
    {
        target.write(b);
        listener.bytesRead(1);
    }

    public void close() throws IOException
    {   //输出流关闭
        target.close();
        //当前这个文件已经上传完成
        listener.done();
    }

    public void flush() throws IOException
    {
        target.flush();
        //没有监听器监听
    }
}
