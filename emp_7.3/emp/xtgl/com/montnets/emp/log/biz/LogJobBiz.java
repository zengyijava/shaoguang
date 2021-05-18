package com.montnets.emp.log.biz;

import java.io.File;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.util.FileUtils;
import com.montnets.emp.util.TxtFileUtil;

public class LogJobBiz {
	
	TxtFileUtil util = new TxtFileUtil();

    public boolean delLogFile(int monSize, String year, String month) throws Exception {
		boolean flag = false;
		//日志保留年
		int y = Integer.valueOf(year);
		//日志保留月
		int m = Integer.valueOf(month);
		//当前月 减去monSize（日志保存时间，单位：月）
		int ret = 0;

		if (monSize == 6) {		//保存6个月
			ret = m - 6;
			
			switch (ret) {
				case -5:
					y = y - 1;
					m = 12 - 4;
					break;
				case -4:
					y = y - 1;
					m = 12 - 3;
					break;
				case -3:
					y = y - 1;
					m = 12 - 2;
					break;
				case -2:
					y = y - 1;
					m = 12 - 1;
					break;
				case -1:
					y = y - 1;
					m = 12;
					break;
				case 0:
					m = 1;
					break;	
				case 1:
					m = 2;
					break;
				case 2:
					m = 3;
					break;
				case 3:
					m = 4;
					break;
				case 4:
					m = 5;
					break;
				case 5:
					m = 6;
					break;
				default:
					m = 7;
					break;
			}			
			
		} else if (monSize == 12) {		//保存12个月
			y = y - 1;
		} else if (monSize == 18) {		//保存18个月
			y = y - 1;
			ret = m - 6;
			switch (ret) {
			case -5:
				y = y - 1;
				m = 12 - 4;
				break;
			case -4:
				y = y - 1;
				m = 12 - 3;
				break;
			case -3:
				y = y - 1;
				m = 12 - 2;
				break;
			case -2:
				y = y - 1;
				m = 12 - 1;
				break;
			case -1:
				y = y - 1;
				m = 12;
				break;
			case 0:
				m = 1;
				break;	
			case 1:
				m = 2;
				break;
			case 2:
				m = 3;
				break;
			case 3:
				m = 4;
				break;
			case 4:
				m = 5;
				break;
			case 5:
				m = 6;
				break;
			default:
				m = 7;
				break;
			}
		} else if (monSize == 24) {		//保存24个月
			y = y - 2;
		} else if (monSize == 30) {		//保存30个月
			y = y - 2;
			ret = m - 6;
			switch (ret) {
			case -5:
				y = y - 1;
				m = 12 - 4;
				break;
			case -4:
				y = y - 1;
				m = 12 - 3;
				break;
			case -3:
				y = y - 1;
				m = 12 - 2;
				break;
			case -2:
				y = y - 1;
				m = 12 - 1;
				break;
			case -1:
				y = y - 1;
				m = 12;
				break;
			case 0:
				m = 1;
				break;	
			case 1:
				m = 2;
				break;
			case 2:
				m = 3;
				break;
			case 3:
				m = 4;
				break;
			case 4:
				m = 5;
				break;
			case 5:
				m = 6;
				break;
			default:
				m = 7;
				break;
			}
		} else if (monSize == 36) {		//保存36个月
			y = y - 3;
		}
		
		try {

			//日志文件年集合
			File[] yearFileList = null;
			//日志文件年
			File yearFile = null;
			//日志文件月集合
			File[] MonFileList = null; 
			//日志文件月
			File monFile = null;
			//当前年目录
			int yearNum = 0;
			//当前月目录
			int monNum = 0;
			
			//获取日志文件路径
			String pathUrl = "";
			//开启自定义路径且路径不为空，使用自定义路径保存日志
			if(StaticValue.LOG_SAVE_PATH != null && StaticValue.LOG_SAVE_PATH.trim().length() > 0 && !"-1".equals(StaticValue.LOG_SAVE_PATH.trim()))
			{
				pathUrl = StaticValue.LOG_SAVE_PATH;
				if(!StaticValue.LOG_SAVE_PATH.endsWith("/")&&!StaticValue.LOG_SAVE_PATH.endsWith("\\"))
				{
					pathUrl += String.valueOf(File.separatorChar);
				}
			}
			else
			{
				pathUrl = new TxtFileUtil().getWebRoot();
			}
			pathUrl += "logger";
			
			//logger目录下的年集合
			yearFileList = GetAllFileNames(pathUrl);
			if (yearFileList != null && yearFileList.length > 0) 
			{
				for (int i = 0; i < yearFileList.length; i++) 
				{
					// mon_data为监控数据文件
					if(!"mon_data".equals(yearFileList[i].getName()))
					{
						//当前年
						yearNum = Integer.valueOf(yearFileList[i].getName());
						//小于保留日志的年
						if(yearNum < y)
						{
							yearFile = new File(pathUrl + "\\" + yearNum);
							//删除年目录
                            boolean status = FileUtils.deleteDir(yearFile);
                            if (!status) {
                                EmpExecutionContext.error("刪除文件失敗！");
                            }
						}
						//等于保留日志的年
						else if(yearNum == y)
						{
							//年目录下的月集合
							MonFileList = GetAllFileNames(pathUrl + "\\" + yearNum);
							if(MonFileList != null && MonFileList.length > 0)
							{
								for(int j=0; j<MonFileList.length; j++)
								{
									//当前月
									monNum = Integer.valueOf(MonFileList[j].getName());
									//小于保留日志的年
									if(monNum < m)
									{
										if (monNum < 10) 
										{
											monFile = new File(pathUrl + "\\" + yearNum + "\\0" + monNum);
										} 
										else 
										{
											monFile = new File(pathUrl + "\\" + yearNum + "\\" + monNum);
										}
										//删除月目录
                                        boolean status = FileUtils.deleteDir(monFile);
                                        if (!status) {
                                            EmpExecutionContext.error("刪除文件失敗！");
                                        }
									}
								}
							}
						}
					}
				}
			}
			flag = true;
		} catch (Exception e) {
			flag = false;
			EmpExecutionContext.error(e, "删除查过保存期限日志文件异常！");
		}
		
		return flag;
	}
	
	/**
	 * 获取日志文件夹路径
	 * @param fileUrl
	 * @param sonUrl
	 * @return
	 */
	public String getPath(String fileUrl, String sonUrl) {		
		String path = util.getWebRoot();
		path = path.substring(0, path.lastIndexOf("/"));
		path = path + "\\logger";
		
		if (null != fileUrl && !fileUrl.equals("")) {	//路径为年
			path = path + fileUrl;
			
			if (sonUrl != null && !"".equals(sonUrl)) {		//路径为月
				path = path + fileUrl + sonUrl;
			}
		}
		
		return path;
	}
	
	/**
	 * 获取日志文件夹路径
	 * @return
	 */
	public String getPath() 
	{		
		return util.getWebRoot()+"logger";
	}
	
	/**
	 * 扫描文件夹，获取日志目录
	 * @param path
	 * @return
	 */
	public File[] GetAllFileNames(String path){		
		File[] fileList =  null;		
		File rootDir = new File(path);
		if(rootDir.isDirectory()) {			
			fileList =  rootDir.listFiles();	
			
		}
		
		return fileList;    
	}
	
	public static void main(String[] args) {
		File file = new File("D:\\FTP");
		File fileList[] =  null;		
		File rootDir = new File("D:\\FTP");
		
		if(rootDir.isDirectory()) {			
			fileList =  rootDir.listFiles();		
						
		}else{
			EmpExecutionContext.error("没有文件夹，文件名："+rootDir.getAbsolutePath());
		} 
		if(fileList!=null){
		for (int i = 0; i < fileList.length; i++) {
			
			try {
                boolean status = FileUtils.deleteDir(file);
                if (!status) {
                    EmpExecutionContext.error("刪除文件失敗！");
                }
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		}
		//rootDir
		
	}
}
