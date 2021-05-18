import com.fasterxml.jackson.databind.ObjectMapper;
import com.montnets.shaoguanga.bean.RespBody;
import com.montnets.shaoguanga.constant.SgConstant;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @param
 * @ClassName test08
 * @Author zengyi
 * @Description
 * @Date 2021/4/25 16:47
 **/
public class test08 {
    public static void main(String[] args) throws IOException {
        StringBuffer sb=new StringBuffer();
        sb.append("13723585518").append(",").append("13723585518").append(",");
        System.out.println(sb.toString());
        System.out.println(sb.deleteCharAt(sb.length() - 1));
        System.out.println(sb.delete(0, 1));

    }
}
