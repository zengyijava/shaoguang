import com.montnets.shaoguanga.constant.SgConstant;
import com.montnets.shaoguanga.properties.MwConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * @param
 * @ClassName test07
 * @Author zengyi
 * @Description
 * @Date 2021/4/22 12:31
 **/
@Slf4j
public class test07 {
    public static void main(String[] args) {
        String str="123456$#MW#$http://192.169.1.144:9090/erp/accept";
        //REGEX = "\\$#MW#\\$"
        if(str.indexOf(SgConstant.REGEX)!=-1){
            System.out.println("----");
        }
        System.out.println(SgConstant.REGEX);
        System.out.println(str.split("http://")[1]);
        String[] spArr = str.split(SgConstant.REGEX);
        System.out.println(spArr[1]);


    }
}
