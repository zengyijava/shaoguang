package com.montnets.emp.ottbase.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.montnets.emp.common.context.EmpExecutionContext;


/**
 * 日志
 *
 * @author Administrator
 */
public class Logger implements Runnable {
    public static final int LEVEL_OFF = 0x7fffffff;
    public static final int LEVEL_FATAL = 0x27ffb;
    public static final int LEVEL_ERROR = 0x1fffc;
    public static final int LEVEL_WARN = 0x17ffd;
    public static final int LEVEL_INFO = 65534;
    public static final int LEVEL_DEBUG = 32767;
    public static final int LEVEL_ALL = 0;

    //logger对象
    private static Logger instance = null;
    //保存路径
    private String savePath;
    //打印信息级别
    private int printFilterLevel;
    //日志文件级别
    private int saveFilterLevel;

    //格式化时间
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //是否显示sql
    private boolean isShowSql;

    private final TxtFileUtil txtfileutil = new TxtFileUtil();

    //队列
    private LinkedList<String> queueData = null;

    //队列中的元素
    private StringBuffer result = null;

    /**
     * 初始化变量
     */
    private Logger() {
        savePath = "";
        printFilterLevel = 0;
        saveFilterLevel = 0;
        queueData = new LinkedList<String>();
        result = new StringBuffer();
    }

    /**
     * 单例模式
     *
     * @return
     */
    public synchronized static Logger get() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    /**
     * 初始化方法
     *
     * @param savePath
     * @param bufferSize
     */
    public void init(String savePath, int bufferSize) {
        this.savePath = savePath;
        if (!savePath.endsWith("/") && !savePath.endsWith("\\")) {
            this.savePath = (new StringBuilder(String.valueOf(savePath)))
                    .append("/").toString();
        }
    }

    public void setPrintFilter(int filterLevel) {
        printFilterLevel = filterLevel;
    }

    public int getPrintFilter() {
        return printFilterLevel;
    }

    public void setSaveFilter(int filterLevel) {
        saveFilterLevel = filterLevel;
    }

    public int getSaveFilter() {
        return saveFilterLevel;
    }

    public void setShowSql(boolean isShowSql) {
        this.isShowSql = isShowSql;
    }

    public boolean getShowSql() {
        return isShowSql;
    }

    /**
     * 保存异常信息到日志文件(暂不使用)
     *
     * @param e    异常对象
     * @param info 描述信息
     */
    public void saveErrorLog(String info, Exception e) {
        PrintStream ps = null;
        try {
            File file = getLogFile();
            if (file == null) {
                return;
            }

            //保存到日志文件
            ps = new PrintStream(new FileOutputStream(file, true), true);
            ps.println(new SimpleDateFormat("yyyy_MM_dd hh:mm:ss").format(new Date()) + " " + info);
            e.printStackTrace(ps);
        } catch (Exception e1) {
            System.out.println("保存异常信息到日志文件失败，失败信息：" + e.getMessage());
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    /**
     * 保存日志信息到日志文件
     *
     * @param info 日志信息
     */
    public void saveLog(String info) {
        FileWriter writer = null;
        try {
            File file = getLogFile();
            if (file == null) {
                return;
            }
            writer = new FileWriter(file, true);
            //写日志文件
            writer.write(info);
        } catch (Exception e) {
            //打印异常信息
            System.out.println("保存日志信息到日志文件失败，失败信息：" + e.getMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    //打印异常信息
                    System.out.println("关闭日志文件失败，失败信息：" + e.getMessage());
                }
            }
        }
    }

    /**
     * 获取日志文件对象
     *
     * @return
     */
    private File getLogFile() {
        try {
            //构造异常日志文件目录（年月日文件夹）
            String dayPath = (new SimpleDateFormat("yyyy/MM/dd/")).format(Calendar
                    .getInstance().getTime());
            //构造异常日志文件目录
            String sbTemp = new StringBuilder(String.valueOf(savePath)).append(dayPath).toString();
            //创建日常日志文件目录
            txtfileutil.makeDir(sbTemp);

            //构造异常日志文件名
            String fname = (new StringBuilder(String.valueOf((new SimpleDateFormat(
                    "yyyy_MM_dd")).format(Calendar.getInstance().getTime()))))
                    .append("_error.log").toString();
            //生成异常日志文件
            File file = new File((new StringBuilder(String.valueOf(savePath)))
                    .append(dayPath).append("/").append(fname).toString());
            if (!file.exists()) {
                //文件不存在则创建
                boolean state = file.createNewFile();
                if (!state) {
                    EmpExecutionContext.error("创建文件失败");
                }
            }
            return file;

        } catch (Exception e) {
            System.out.println("获取日志文件对象失败,失败信息:" + e.getMessage());
            return null;
        }
    }

    /**
     * 日志信息加入队列
     */
    public void putEvent(String logInfo) {
        //加入队列
        if (logInfo != null && !"".equals(logInfo)) {
            queueData.add(logInfo);
        }
    }

    /**
     * 获取队列中的所有元素,并清空队列
     *
     * @return 队列中的所有元素
     */
    private void getEvent() {
        if (queueData != null) {
            //获取队列中所有元素(日志信息)
            Object[] queueElement = (Object[]) queueData.toArray();
            //清空队列中的元素
            queueData.clear();
            //获取队列的元素数
            int queueDataCount = queueElement.length;
            //队列中存在元素，拼接日志信息
            for (int i = 0; i < queueDataCount; i++) {
                result.append(sdf.format(new Date()) + " " + queueElement[i]);
            }
        }
    }

    /**
     * 线程方法
     */
    @Override
    public void run() {
        /*while (true) {
            //获取队列中的元素(日志信息)
            getEvent();
            try {
                //如果元素不为空，则写日志
                if (result != null) {
                    saveLog(result.toString());
                    //清空已获取的队列元素
                    result.setLength(0);
                }
                //线程等待
                Thread.sleep(10000L);
            } catch (Exception e) {
                System.out.println("日志线程run方法发生异常,异常信息:" + e.getMessage());
            }
        }*/

    	try {
	        Runnable runnable = new Runnable() {
	    	    public void run() {
	                //获取队列中的元素(日志信息)
	                getEvent();
	                try {
	                    //如果元素不为空，则写日志
	                    if (result != null) {
	                        saveLog(result.toString());
	                        //清空已获取的队列元素
	                        result.setLength(0);
	                    }
	                } catch (Exception e) {
	                    System.out.println("日志线程run方法发生异常,异常信息:" + e.getMessage());
	                }
	            }
	    	};
	    	ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
	    	service.scheduleWithFixedDelay(runnable, 0, 10000L, TimeUnit.SECONDS);
    	} catch (Exception e) {
            EmpExecutionContext.error(e,"日志线程run方法发生异常,异常信息:");
        }
    }
}
