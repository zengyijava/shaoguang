import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @param
 * @ClassName test06
 * @Author zengyi
 * @Description
 * @Date 2021/4/20 9:14
 **/
public class test06 {
    public static void main(String[] args) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String nowTime = sdf.format(new Date());
        long timeDiff;
        long day;
        File bakFile = new File("C:\\Users\\dell\\Desktop\\bak");
        File[] files = bakFile.listFiles();
        if(null!=files && files.length>0){
            for (File dayFile : files) {
                String dayFileName = dayFile.getName();
                // 字符串 "yyyyMMdd" 转换为字符串 "yyyy-MM-dd"
                String format = DateFormatUtils.format(DateUtils.parseDate(dayFileName, new String[]{"yyyyMMdd"}), "yyyy-MM-dd");
                Date fileDate = sdf.parse(format);
                Date nowDate = sdf.parse(nowTime);
                timeDiff = nowDate.getTime() - fileDate.getTime();
                day = timeDiff / (24 * 60 * 60 * 1000);
                if(day>30){
                    func(dayFile);
                }
            }
        }
    }

    private static void func(File dayFile) {
        try {
            File[] files = dayFile.listFiles();
            if(null!=files && files.length>0){
                for (File file : files) {
                    if(file.isFile()) {
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
            e.printStackTrace();
        }

    }
}
