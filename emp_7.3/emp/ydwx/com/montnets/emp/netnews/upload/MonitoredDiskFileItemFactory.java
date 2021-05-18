package com.montnets.emp.netnews.upload;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
//download by http://www.codefans.net
import java.io.File;


public class MonitoredDiskFileItemFactory extends DiskFileItemFactory
{
	//为了实现上传文件的进度条功能，加上一个监听器
    private OutputStreamListener listener = null;

    public MonitoredDiskFileItemFactory(OutputStreamListener listener)
    {
        super();
        this.listener = listener;
    }

    public MonitoredDiskFileItemFactory(int sizeThreshold, File repository, OutputStreamListener listener)
    {
        super(sizeThreshold, repository);
        this.listener = listener;
    }

    public FileItem createItem(String fieldName, String contentType, boolean isFormField, String fileName)
    {   //在调用ServletFileUpload上的parseReuqest(request)方法的时候
    	//会自动执行这个方法（回调方法）
    	//正对每一个文件
    	//上传一个文件，执行这个方法一次
        return new MonitoredDiskFileItem(fieldName, contentType, isFormField, fileName, getSizeThreshold(), getRepository(), listener);
    }
}
