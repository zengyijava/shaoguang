package com.montnets.emp.reportform.util;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.PrintWriter;

/**
 * IO 工具类
 * @author Chenguang
 * @date 2018-12-15 13:30:30
 */
public class IOUtils {
    public static void flush(PrintWriter printWriter, String result) {
        if (StringUtils.isEmpty(result)) {
            printWriter.write("no_service");
        } else {
            if (printWriter != null) {
                printWriter.write(result);
                printWriter.flush();
            }
        }
    }

    public static void close(PrintWriter printWriter) {
        if (null != printWriter) {
            printWriter.close();
        }
    }

    public static void flushAndClose(PrintWriter printWriter, String result) {
        flush(printWriter, result);
        close(printWriter);
    }

    public static void deleteDirByRecursion(File baseDir, String excludeFileType) throws Exception{
        if (baseDir.isDirectory()) {
            String[] subFiles = baseDir.list();
            if(subFiles != null){
                for (String file : subFiles) {
                    deleteDirByRecursion(new File(baseDir, file), excludeFileType);
                }
            }else {
                //空文件夹
                if(!baseDir.delete()){
                    throw new Exception("递归删除文件夹失败");
                }
            }
        }else {
            if(!baseDir.getName().endsWith(excludeFileType)){
                if(!baseDir.delete()){
                    throw new Exception("递归删除文件夹失败");
                }
            }
        }
    }
}
