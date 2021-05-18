package com.montnets.emp.rms.tools;

import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.util.GetSxCount;
import com.montnets.emp.util.TxtFileUtil;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RmsTxtFileUtil extends TxtFileUtil {
    @Override
    public String readFileByLines(String fileName) {
        String phoneStr = null;
        BufferedReader br = null;

        try {
            File file = new File(getWebRoot() + fileName);
            StringBuffer sb = new StringBuffer();
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf-8"));
            String tempString = null;

            while((tempString = br.readLine()) != null) {
                sb.append(tempString.trim()).append(",");
            }

            if (sb.lastIndexOf(",") != -1) {
                sb.deleteCharAt(sb.lastIndexOf(","));
            }

            phoneStr = sb.toString();
            sb.setLength(0);
        } catch (Exception var15) {
            EmpExecutionContext.error(var15, "以行为单位读取文件异常！");
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException var14) {
                EmpExecutionContext.error(var14, "关闭文本流异常！ ");
            }

        }

        return phoneStr;
    }

    @Override
    public boolean writeToTxtFile(String fileName, String content) throws EMPException {
        FileOutputStream foss = null;
        ByteArrayInputStream baInput = null;

        try {
            File filee = new File(fileName);
            if (!filee.exists()) {
                boolean flag = filee.createNewFile();
                if (!flag) {
                    EmpExecutionContext.error("创建文件失败！");
                }
            }

            foss = new FileOutputStream(fileName, true);
            baInput = new ByteArrayInputStream(content.getBytes("utf-8"));
            byte[] buffer = new byte[8192];
            boolean var7 = false;

            int ch;
            while((ch = baInput.read(buffer)) != -1) {
                foss.write(buffer, 0, ch);
            }
        } catch (Exception var18) {
            EmpExecutionContext.error(var18, "将字符串内容写入文件异常！fileName:" + fileName);
            throw new EMPException("B20002", var18);
        } finally {
            if (baInput != null) {
                try {
                    baInput.close();
                } catch (IOException var17) {
                    EmpExecutionContext.error(var17, "将字符串内容写入文件关闭输入输流异常!fileName:" + fileName);
//                    throw new EMPException("B20003", var17);
                }
            }

            if (foss != null) {
                try {
                    foss.close();
                } catch (IOException var16) {
                    EmpExecutionContext.error(var16, "关闭流异常！fileName:" + fileName);
//                    throw new EMPException("B20003", var16);
                }
            }

        }
        return true;
    }
    public String[] getSaveRmsMobileFileUrl(Long lgUserId, String[] param) throws EMPException {
        try {
            Calendar c = Calendar.getInstance();
            String uploadPath = "file/rmstxt/manualrmstxt/";
            String[] url = new String[6];
            String saveName = "";
            GetSxCount sx = GetSxCount.getInstance();
            String sxcount = sx.getCount();
            if (param[0] != null && !"".equals(param[0].trim())) {
                saveName = "1_" + lgUserId.toString() + "_" + (new SimpleDateFormat("yyyyMMddHHmmssS")).format(c.getTime()) + "_" + param[0] + "_" + sxcount + ".txt";
            } else {
                saveName = "1_" + lgUserId.toString() + "_" + (new SimpleDateFormat("yyyyMMddHHmmssS")).format(c.getTime()) + "_" + sxcount + ".txt";
            }

            String physicsUrl = this.getWebRoot();
            String dirPath = this.createDir(physicsUrl + uploadPath, c);
            physicsUrl = physicsUrl + uploadPath + dirPath + saveName;
            String logicUrl = uploadPath + dirPath + saveName;
            url[0] = physicsUrl;
            url[1] = logicUrl;
            url[2] = url[0].replace(".txt", "_bad.txt");
            url[3] = url[0].replace(".txt", "_view.txt");
            url[4] = url[1].replace(".txt", "_view.txt");
            // 新增无效文件相对路径
            url[5] = url[1].replace(".txt", "_bad.txt");
            return url;
        } catch (Exception var12) {
            EmpExecutionContext.error(var12, "获取文件路径失败。错误码：B20001");
            throw new EMPException("B20001", var12);
        }
    }
}
