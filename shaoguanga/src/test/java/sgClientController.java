import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;


/**
 * @param
 * @ClassName ClientController
 * @Author zengyi
 * @Description
 * @Date 2021/4/8 11:30
 **/
@Slf4j
@RequestMapping("/sgClient")
@RestController
public class sgClientController {
    @PostMapping("/doInterfaces")
    public String doPost(HttpServletRequest request) throws IOException {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        String params = request.getParameter("params");
        String requestUri = request.getParameter("requestUri");
        if (StringUtils.isNotEmpty(params)) {
            Map<String, String> map = handleMap(params);
            Set<Map.Entry<String, String>> entries = map.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue());
                list.add(pair);
            }
        }else {
            log.error("params参数为空...");
        }

        HttpPost httpPost = null;
        String result = null;
        HttpClient client = HttpClients.createDefault();
        if (StringUtils.isNotEmpty(requestUri)) {
            httpPost = new HttpPost(requestUri);
            //设置请求和传输超时时间
            String timeout = request.getParameter("timeout");
            if (StringUtils.isNotEmpty(timeout)) {
                RequestConfig config = RequestConfig.custom().setSocketTimeout(Integer.parseInt(timeout)).setConnectTimeout(Integer.parseInt(timeout)).build();
                httpPost.setConfig(config);
            }
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));
                httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
                HttpResponse response = client.execute(httpPost);
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    result = EntityUtils.toString(entity);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
        }else {
            log.error("requestUri地址为空...");
        }
        return result;

    }

    public Map<String, String> handleMap(String params) {
        HashMap<String, String> map = new HashMap<String, String>();
        //params="TEST01&pwd=ad390a04ebaf839866254d7eba795266&timestamp=0409110926"
        String[] split = params.split("&");
        //[userid=TEST01,pwd=ad390a04ebaf839866254d7eba795266]
        try {
            for (String s : split) {
                //userid=TEST01
                String[] split1 = s.split("=");
                //[userid,TEST01]
                map.put(split1[0], split1[1]);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return map;
    }


}
