
import com.montnets.shaoguanga.bean.SendRpts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @param
 * @ClassName sgZRPController
 * @Author zengyi
 * @Description
 * @Date 2021/4/15 15:41
 **/
@Slf4j
@RestController
@RequestMapping("/erp")
public class SgERPController {

    /**
     * 模拟业务系统
     * @param sendRpts
     * @return
     */
    @PostMapping("/accept")
    public void test(@RequestBody List<SendRpts> sendRpts){
        if(CollectionUtils.isEmpty(sendRpts)){
            log.error("业务系统接收的内容为空...");
        }
    }

}
