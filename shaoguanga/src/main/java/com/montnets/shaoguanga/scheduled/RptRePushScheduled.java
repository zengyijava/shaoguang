package com.montnets.shaoguanga.scheduled;


import com.alibaba.fastjson.JSONObject;
import com.montnets.shaoguanga.constant.SgConstant;
import com.montnets.shaoguanga.properties.MwConfig;
import com.montnets.shaoguanga.service.HttpService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @param
 * @ClassName RptRePushScheduled
 * @Author zengyi
 * @Description
 * @Date 2021/4/14 9:31
 **/
@Slf4j
@Component
@PropertySource("classpath:application.properties")
public class RptRePushScheduled {
    @Autowired
    private HttpService httpService;
    @Autowired
    private MwConfig mwConfig;

    @Scheduled(cron = "${scheduled.rptRePushTime}")
    public void timeGetRpt() throws Exception {
        String getRpt = null;
        String sendUrl=null;
        Boolean flag;
        try {
            SimpleDateFormat d = new SimpleDateFormat("yyyyMMdd");
            String day = d.format(new Date());
            Map<String, String> spMap = mwConfig.getSpMap();
            File rptsPath = new File(System.getProperty("user.dir") + File.separator + "rptsfile");
            String realPath = rptsPath + File.separator + "real";
            String bakPath = rptsPath + File.separator + "bak";
            //判断是否需要配送状态报告
            if (mwConfig.isNeedRpt() && null!=spMap && spMap.size()>0) {
                for (Map.Entry<String, String> entry : spMap.entrySet()) {
                    File rfile = new File(realPath + File.separator + entry.getKey());
                    File bfile = new File(bakPath+File.separator+day+File.separator+entry.getKey());
                    File[] files = rfile.listFiles();
                    if(files!=null && files.length>0){
                        for (File file : files) {
                            getRpt = readTxt(file);
                            if (StringUtils.isNotEmpty(getRpt)) {
                                JSONObject rptsJSON = JSONObject.parseObject(getRpt);
                                String[] spArr = entry.getValue().split(SgConstant.REGEX);
                                if (spArr.length < 2) {
                                    continue;
                                }
                                sendUrl = spArr[1];
                                try {
                                    flag = httpService.send2SpUrl(sendUrl,rptsJSON.getJSONArray("sendRpts").toString());
                                    if (flag) {
                                        boolean delete = file.delete();
                                        if (delete) {
                                            log.info("删除文件成功：{}",file.getName());
                                        }else {
                                            log.error("删除文件失败：{}",file.getName());
                                            FileUtils.moveToDirectory(file,bfile,true);
                                        }
                                    }else {
                                        FileUtils.moveToDirectory(file,bfile,true);
                                    }
                                }catch (Exception e){
                                    FileUtils.moveToDirectory(file,bfile,true);
                                }
                            }else {
                                // txt内容为空则删除
                                file.delete();
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            log.error("重退定时任务异常：",e);
        }

    }


    /**
     * 读取存储在txt文件中的内容
     * @param
     * @return
     * @throws IOException
     */
    public String readTxt(File fileName) {
        BufferedReader reader = null;
        String line = null;
        FileInputStream in = null;
        try {
            in = new FileInputStream(fileName);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (Exception e) {
            log.error("读取状态报告异常：", e);
        }finally {
            try {
                if(in!=null){
                    in.close();
                }
                if(reader!=null){
                    reader.close();
                }
            }catch (Exception e){
                log.error("流关闭异常：",e);
            }
        }
        return null;

    }






}
