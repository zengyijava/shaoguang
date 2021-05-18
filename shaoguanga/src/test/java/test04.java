import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @param
 * @ClassName test04
 * @Author zengyi
 * @Description
 * @Date 2021/4/14 15:11
 **/
public class test04 {

    public static void main(String[] args) throws IOException, ParseException {
//        File file = new File("D:\\MWProject\\shaoguan\\real\\TEST02\\1.txt");
//        File file1 = new File("D:\\MWProject\\shaoguan\\real");
//        FileUtils.moveToDirectory(file,file1,true);
        String path="D:\\MWProject\\shaoguan\\test";
        String bakPath = new File(ResourceUtils.getURL("classpath:").getPath()).getParentFile().getParentFile().getParent() + File.separator + "bak";
        File file = new File(path);
        func(file);

    }

    private static void func(File file) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime = sdf.format(new Date());
        long timeDiff;
        long day;
        File[] files = file.listFiles();
        for (File F : files) {
            if(F.isDirectory()) {
                String userid = F.getName();
                for (File  f: F.listFiles()) {
                    String name = f.getName();
                    // 比如 name 等于 TEST0220210414165411.txt
                    if(name.indexOf(userid)!=-1){
                        String temp = name.split(".txt")[0];
                        String fileTime = temp.split(userid)[1];
                        Date nowDate = sdf.parse(nowTime);
                        // 字符串 "yyyyMMddHHmmss" 转换为字符串 "yyyy-MM-dd HH:mm:ss"
                        String format = DateFormatUtils.format(DateUtils.parseDate(fileTime, new String[]{"yyyyMMddHHmmss"}), "yyyy-MM-dd HH:mm:ss");
                        Date fileDate = sdf.parse(format);
                        timeDiff=nowDate.getTime()-fileDate.getTime();
                        day = timeDiff / (24 * 60 * 60 * 1000);
                        System.out.println("账号项目下文件： "+f.getName());
                        if(day>30){
                            System.out.println("30天前的日期：  "+f.getName());
                            //f.delete();
                        }
                    }
                }
            }else {
                System.out.println("备份目录下的文件： "+F.getName());
                F.delete();
            }
        }

    }



}
