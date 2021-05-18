import org.springframework.util.ResourceUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @param
 * @ClassName test01
 * @Author zengyi
 * @Description
 * @Date 2021/4/13 17:19
 **/
public class test01 {
    public static void main(String[] args) throws IOException {
        String str="{\"sendRpts\":[{\"mobile\":\"13612345678\",\"msgGroup\":\"1660944840\",\"receiveDate\":\"20210414082056\",\"reportStatus\":\"0\",\"submitDate\":\"20210414082055\"},{\"mobile\":\"13612345678\",\"msgGroup\":\"1660944840\",\"receiveDate\":\"20210414082056\",\"reportStatus\":\"0\",\"submitDate\":\"20210414082055\"},{\"mobile\":\"13612345678\",\"msgGroup\":\"1660944840\",\"receiveDate\":\"20210414082056\",\"reportStatus\":\"0\",\"submitDate\":\"20210414082055\"}]}";

        String path = new File(ResourceUtils.getURL("classpath:").getPath()).getParentFile().getParentFile().getParent();
        System.out.println(path);
        System.out.println(System.getProperty("user.dir"));

    }

}
