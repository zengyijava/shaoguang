package com.montnets.emp.rms.meditor.tools;

import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.util.IOUtils;

import java.io.*;

public class FileOperatorTool {
	// 写文件
	public File writeFile(String filePath, byte[] contents) {
			File file = null;
			FileOutputStream fos = null;
			try {
				file = new File(filePath);
				File dir = new File(file.getParent());
				if(!dir.exists()){//文件夹不存在创建
					dir.mkdirs();
				}
				if (!file.exists()) {
                    boolean flag = file.createNewFile();
                    if (!flag) {
                        EmpExecutionContext.error("创建文件失败！");
                    }
				} else {
					if (file.isFile()) {
                        boolean flag = file.delete();
                        if (!flag) {
                            EmpExecutionContext.error("删除文件失败！");
                        }
					}
                    boolean flag = file.createNewFile();
                    if (!flag) {
                        EmpExecutionContext.error("创建文件失败！");
                    }
				}

				fos = new FileOutputStream(filePath);
				fos.write(contents, 0, contents.length);
				
			} catch (Exception e) {
				file = null;
				EmpExecutionContext.error(e, "文件写入异常！");
			} finally{
				if(fos != null){
					try {
						fos.close();
					} catch (IOException e) {
						EmpExecutionContext.error(e, "关闭异常！");
					}
				}
			}
			return file;
		}
	
	public void writeFile(String filePath, String content) {
		File file = new File(filePath, content);
		FileOutputStream fos = null;
		try {
			file = new File(filePath);
			File dir = new File(file.getParent());
			if(!dir.exists()){//文件夹不存在创建
				dir.mkdirs();
			}
			if (!file.exists()) {
                boolean flag = file.createNewFile();
                if (!flag) {
                    EmpExecutionContext.error("创建文件失败！");
                }
			} else {
				if (file.isFile()) {
                    boolean flag = file.delete();
                    if (!flag) {
                        EmpExecutionContext.error("删除文件失败！");
                    }
				}
                boolean flag = file.createNewFile();
                if (!flag) {
                    EmpExecutionContext.error("创建文件失败！");
                }
			}
			fos = new FileOutputStream(file.getAbsoluteFile());
			fos.write(content.getBytes("UTF-8"));
//			fos.close();
		} catch (IOException e) {
			EmpExecutionContext.error(e, "文本文件生成异常！");
		} finally{
			if(fos != null){
				try {
					fos.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e, "关闭异常！");
				}
			}
		}
	}


	/**
	 * 上传文件服务器
	 * @param fileUrL zip文件相对路径
	 * @return
	 */
	public static  boolean uploadFileCenter(String fileUrL){
		boolean flag = false;
		String uploadFileFlag= new CommonBiz().uploadFileToFileCenter(fileUrL);
		if("success".equals(uploadFileFlag)){
			flag =  true;
		}
		return flag;
	}

	/**
	 * 上传压缩包至H5服务器
	 * @param h5StoreUrl
	 * @param sourcePath
	 * @return
	 */
	public static String uploadH5Server(String h5StoreUrl,String sourcePath){
	String h5CardHtml ="";
    try {
        File file = new File(sourcePath);
        String btype = "1";//1-上传模板
        String ftype ="5";//5-zip
        h5CardHtml = String2FileUtil.uploadFile(file, h5StoreUrl, btype, ftype);
    } catch (Exception e) {
        EmpExecutionContext.error(e,"上传H5服务器出现异常");
    }
        return h5CardHtml;
	}


	/**
	 * 从富媒体的src 下的资源文件转移到OTT的src 目录下
	 * @param soureSrc
	 * @param targetSrc
	 */
	public  void copySrcFile(String soureSrc,String targetSrc){
        buildFolder(targetSrc);
		emptyFolder(targetSrc);
        File file = new File(soureSrc);
        File[] files = file.listFiles();
        for(File f : files){
        	if(f.getName().contains(".txt") || f.getName().contains(".smil")){
				continue;
			}
			copyFile(targetSrc+"/"+f.getName(),f.getAbsolutePath());
        }
	}

    /**
     * 复制文件
     *
     * @param newPath
     * @param oldPath
     */
    private void copyFile(String newPath, String oldPath) {
    	InputStream inStream = null;
    	FileOutputStream foutStream = null;
        try {
            int byteread = 0;
            File newfile = new File(newPath);
            File oldfile = new File(oldPath);
            if (oldfile.exists()) {
                inStream = new FileInputStream(oldPath);
                File folder = new File(newfile.getParent());
                if(!folder.exists()){//新文件
                    folder.mkdirs();
                }
                foutStream = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024];
                while ((byteread = inStream.read(buffer)) != -1) {
                    foutStream.write(buffer, 0, byteread);
                }
//                inStream.close();
//                foutStream.close();
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "文件复制异常！");
        } finally{
        	try {
				IOUtils.closeIOs(inStream, foutStream, null, null, getClass());
			} catch (IOException e) {
				EmpExecutionContext.error(e, "文件关闭异常！");
			}
        }
    }
    /**
     * 生成文件夹
     */
    private void buildFolder(String targetPath) {
        File folder = new File(targetPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

	/**
	 * 清空文件夹
	 * @param path
	 */
	private void emptyFolder(String path){
		File dir = new File(path);
		File[] files = dir.listFiles();
		for(File f :files){
			if(f.exists() && f.isFile()){
                boolean flag = f.delete();
                if (!flag) {
                    EmpExecutionContext.error("删除文件失败！");
                }
			}
		}
	}
}
