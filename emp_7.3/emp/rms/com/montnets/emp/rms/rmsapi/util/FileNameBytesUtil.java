package com.montnets.emp.rms.rmsapi.util;

/**
 * 文件类型判断工具类
 * @author Administrator
 *
 */
public class FileNameBytesUtil {

	public static int  getFileType(String fileName){
		if(fileName.contains(".smil")){
			return 1;
		}
		
		if(fileName.contains(".jpg")){
			return 2;
		}
		
		if(fileName.contains(".txt")){
			return 3;
		}
		
		if(fileName.contains(".mp4")){
			return 4;
		}
		if(fileName.contains(".mid")){
			return 5;
		}
		return 0;
		
	}
}
