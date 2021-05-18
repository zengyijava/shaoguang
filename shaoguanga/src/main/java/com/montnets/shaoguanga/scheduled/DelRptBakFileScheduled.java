package com.montnets.shaoguanga.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @param
 * @ClassName DelRptBakFileScheduled
 * @Author zengyi
 * @Description
 * @Date 2021/4/14 16:21
 **/
@Slf4j
@Component
@PropertySource("classpath:application.properties")
public class DelRptBakFileScheduled {

    @Scheduled(cron = "${scheduled.delBakFileTime}")
    public void deleteFile() {
        try {
            String bakPath = System.getProperty("user.dir") + File.separator + "rptsfile" + File.separator + "bak";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String nowTime = sdf.format(new Date());
            long timeDiff;
            long day;
            File bakFile = new File(bakPath);
            File[] files = bakFile.listFiles();
            if(null!=files && files.length>0){
                for (File dayFile : files) {
                    String dayFileName = dayFile.getName();
                    // 字符串 "yyyyMMdd" 转换为字符串 "yyyy-MM-dd"
                    String format = DateFormatUtils.format(DateUtils.parseDate(dayFileName, "yyyyMMdd"), "yyyy-MM-dd");
                    Date fileDate = sdf.parse(format);
                    Date nowDate = sdf.parse(nowTime);
                    timeDiff = nowDate.getTime() - fileDate.getTime();
                    day = timeDiff / (24 * 60 * 60 * 1000);
                    if(day>30){
                        func(dayFile);
                    }
                }
            }
        } catch (Exception e) {
            log.error("删除文件异常: ", e);
        }

    }

    /**
     * 删除一个月前备份文件
     *
     * @param dayFile
     * @throws ParseException
     */
    private static void func(File dayFile) {
        try {
            File[] files = dayFile.listFiles();
            if(null!=files && files.length>0){
                for (File file : files) {
                    if(file.isFile()){
                        file.delete();
                    }else if(file.isDirectory() && file.listFiles().length<=0){
                        file.delete();
                    }else {
                        func(file);
                    }
                }
                dayFile.delete();
            }
        } catch (Exception e) {
            log.error("删除文件失败",dayFile.getName());
        }

    }


}
