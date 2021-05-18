package com.montnets.emp.netnews.upload;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.fileupload.disk.DiskFileItem;


public class MonitoredDiskFileItem extends DiskFileItem
{   
	/**
	 * 
	 */
	private static final long serialVersionUID = 3518253409628981004L;

	/**
	 * 类的实例代表了一个磁盘文件对象(受监控）
	**/
    private MonitoredOutputStream mos = null;
    
  /**
   * 监听器，是为了实现进度控制
  **/
    private OutputStreamListener listener;

    public MonitoredDiskFileItem(String fieldName, String contentType, boolean isFormField, String fileName, int sizeThreshold, File repository, OutputStreamListener listener)
    {
        super(fieldName, contentType, isFormField, fileName, sizeThreshold, repository);
        this.listener = listener;
    }
    
    //获得文件的输出流内容
    @Override
    public OutputStream getOutputStream() throws IOException
    {
    	//文件内容是以输出流的形式获得
        if (mos == null)
        {
            mos = new MonitoredOutputStream(super.getOutputStream(), listener);
        }
        return mos;
    }
}
