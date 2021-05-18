import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @param
 * @ClassName test05
 * @Author zengyi
 * @Description
 * @Date 2021/4/16 15:24
 **/
@Slf4j
public class test05 {
    public static void main(String[] args) {
        String bakPath = System.getProperty("user.dir") + File.separator + "rptsfile" + File.separator + "bak";
        File bakFile = new File(bakPath);
        func(bakFile);
    }

    private static void func(File bakFile) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String nowTime = sdf.format(new Date());
        long timeDiff;
        long day;
        try {
            File[] files = bakFile.listFiles();
            if(null!=files && files.length>0){
                for (File dayFile : files) {
                    String dayFileName = dayFile.getName();
                    // 字符串 "yyyyMMdd" 转换为字符串 "yyyy-MM-dd"
                    String format = DateFormatUtils.format(DateUtils.parseDate(dayFileName, new String[]{"yyyyMMdd"}), "yyyy-MM-dd");
                    if (dayFile.isDirectory()) {
                        for (File userIdFile : dayFile.listFiles()) {
                            if (userIdFile.isDirectory()) {
                                for (File txtFile : userIdFile.listFiles()) {
                                    boolean delete = txtFile.delete();
                                    if(delete){
                                        log.info("删除文件成功"+txtFile.getName());
                                    }else {
                                        log.error("删除文件失败"+txtFile.getName());
                                    }
                                }
                            }
                        }

                    }
                }
            }
        } catch (Exception e) {
            log.error("删除一个月之前的备份文件异常: {}", e);
        }

    }

}


