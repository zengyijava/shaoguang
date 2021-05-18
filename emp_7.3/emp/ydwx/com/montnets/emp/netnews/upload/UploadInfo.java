package com.montnets.emp.netnews.upload;

import java.io.Serializable;


public class UploadInfo implements Serializable
{   
	//文件的总大小
    private long totalSize = 0;
    
    //已经上传的内容的大小
    private long bytesRead = 0;
    
    //已经上传的时间
    private long elapsedTime = 0;
    
    //文件上传的状态
    private String status = "done";
    
    //可以上传多个文件，文件的索引号
    //当前时间正在上传那个文件
    private int fileIndex = 0;

    public UploadInfo()
    {
    }

    public UploadInfo(int fileIndex, long totalSize, long bytesRead, long elapsedTime, String status)
    {
        this.fileIndex = fileIndex;
        this.totalSize = totalSize;
        this.bytesRead = bytesRead;
        this.elapsedTime = elapsedTime;
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public long getTotalSize()
    {
        return totalSize;
    }

    public void setTotalSize(long totalSize)
    {
        this.totalSize = totalSize;
    }

    public long getBytesRead()
    {
        return bytesRead;
    }

    public void setBytesRead(long bytesRead)
    {
        this.bytesRead = bytesRead;
    }

    public long getElapsedTime()
    {
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime)
    {
        this.elapsedTime = elapsedTime;
    }
    
    //文件上传状态判断：是否正在上传
    //true正在上传
    //false已经完成
    public boolean isInProgress()
    {
        return "progress".equals(status) || "start".equals(status);
    }

    public int getFileIndex()
    {
        return fileIndex;
    }

    public void setFileIndex(int fileIndex)
    {
        this.fileIndex = fileIndex;
    }
}
