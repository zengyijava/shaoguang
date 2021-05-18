import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.montnets.shaoguanga.bean.GetRpts;
import com.montnets.shaoguanga.bean.SendRpts;
import com.montnets.shaoguanga.properties.MwConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * @param
 * @ClassName test
 * @Author zengyi
 * @Description
 * @Date 2021/4/13 8:48
 **/
public class test {

    public static void main(String[] args) throws ParseException {
        //{
        //    "result":0,
        //    "rpts":""
        //}
        String str="{\n" +
                "    \"result\":0,\n" +
                "    \"rpts\":\"\"\n" +
                "}";
//        String str="{\n" +
//                "    \"result\": 0,\n" +
//                "    \"rpts\": [\n" +
//                "        {\n" +
//                "            \"msgid\": 5125463339614339528,\n" +
//                "            \"custid\": \"5125463339614339528\",\n" +
//                "            \"pknum\": 1,\n" +
//                "            \"pktotal\": 3,\n" +
//                "            \"mobile\": \"13612345678\",\n" +
//                "            \"spno\": \"106579999\",\n" +
//                "            \"exno\": \"\",\n" +
//                "            \"stime\": \"2021-04-14 08:20:55\",\n" +
//                "            \"rtime\": \"2021-04-14 08:20:56\",\n" +
//                "            \"status\": 0,\n" +
//                "            \"errcode\": \"DELIVRD\",\n" +
//                "            \"errdesc\": \"success\",\n" +
//                "            \"exdata\": \"\"\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"msgid\": 5125463356794208712,\n" +
//                "            \"custid\": \"5125463339614339528\",\n" +
//                "            \"pknum\": 2,\n" +
//                "            \"pktotal\": 3,\n" +
//                "            \"mobile\": \"13612345678\",\n" +
//                "            \"spno\": \"106579999\",\n" +
//                "            \"exno\": \"\",\n" +
//                "            \"stime\": \"2021-04-14 08:20:55\",\n" +
//                "            \"rtime\": \"2021-04-14 08:20:56\",\n" +
//                "            \"status\": 0,\n" +
//                "            \"errcode\": \"DELIVRD\",\n" +
//                "            \"errdesc\": \"success\",\n" +
//                "            \"exdata\": \"\"\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"msgid\": 5125463373974077896,\n" +
//                "            \"custid\": \"5125463339614339528\",\n" +
//                "            \"pknum\": 3,\n" +
//                "            \"pktotal\": 3,\n" +
//                "            \"mobile\": \"13612345678\",\n" +
//                "            \"spno\": \"106579999\",\n" +
//                "            \"exno\": \"\",\n" +
//                "            \"stime\": \"2021-04-14 08:20:55\",\n" +
//                "            \"rtime\": \"2021-04-14 08:20:56\",\n" +
//                "            \"status\": 0,\n" +
//                "            \"errcode\": \"DELIVRD\",\n" +
//                "            \"errdesc\": \"success\",\n" +
//                "            \"exdata\": \"\"\n" +
//                "        }\n" +
//                "    ]\n" +
//                "}";
        JSONObject returnJSON = new JSONObject();
        List<SendRpts> sendRptsList = new ArrayList<>();

        JSONObject jsonObject = JSONObject.parseObject(str);
        System.out.println(jsonObject);

        if(jsonObject.getJSONArray("rpts")!=null){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            JSONArray rptsJSONArr = jsonObject.getJSONArray("rpts");
            System.out.println(rptsJSONArr);
            List<GetRpts> getRptsList = rptsJSONArr.toJavaList(GetRpts.class);
            for (GetRpts getRpts : getRptsList) {
                SendRpts sendRpts = new SendRpts();
                sendRpts.setReportStatus("0");
                sendRpts.setMobile(getRpts.getMobile());
                //字符串"yyyy-MM-dd HH:mm:ss" 转换为字符串"yyyyMMddHHmmss"
                sendRpts.setSubmitDate(DateFormatUtils.format(DateUtils.parseDate(getRpts.getStime(), new String[]{"yyyy-MM-dd HH:mm:ss"}), "yyyyMMddHHmmss"));
                sendRpts.setReceiveDate(DateFormatUtils.format(DateUtils.parseDate(getRpts.getRtime(), new String[]{"yyyy-MM-dd HH:mm:ss"}), "yyyyMMddHHmmss"));
                sendRpts.setMsgGroup(getRpts.getMsgid().toString());
                sendRptsList.add(sendRpts);
            }
            returnJSON.put("sendRpts",sendRptsList);
        }else {
            JSONObject json = JSONObject.parseObject(str);
            System.out.println(json.toJSONString());
        }
        System.out.println(returnJSON);


    }

}
