package com.montnets.emp.rms.meditor.tools;

import com.montnets.emp.common.context.EmpExecutionContext;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;

/**
 * 功能描述: 模板字节获取工具类
 *
 * @auther: xuty
 * @date: 2018/7/31 16:36
 */
public class RmsFileBytesTool {
    /**
     * filePath: 资源文件存储位置
     * title ： 模板标题
     * list ： 资源文件集合
     * 获取RMS相关文件字节流
     *
     * @return
     */
    public byte[] getRmsFileBytes(String filePath, String title, List<Map<String, String>> list) {
        byte[] results = null;
        try {
            //用于记录RMS总长度的位置
            int rmsBgIndex = 0;
            int index = 0;
            // 设置一个，每次 装载信息的容器
            byte[] bytes = new byte[1024 * 1024 * 8];
            bytes[index] = (byte) 162;// 文件标识:A2
            index++;

            bytes[index] = (byte) 02;// 版本:01 表示V1.0
            index++;

            bytes[index] = (byte) 00;// 文件编码格式:0:明文
            index++;

            // 直接将整型数4字节以2进制形式存储(下同)
            // 计算所有文件总长度:4byte
            //int contLegth = getContFileLegth(filePath);
            int rmsTotalLen = 0;
            rmsBgIndex = index;
            index += 4;

            // 计算包含文件个数：2byte
            int fileCount = list.size() + 1;
            System.arraycopy(int2bytes(fileCount), 2, bytes, index, 2);
            index += 2;
            //累加总长度
            rmsTotalLen = rmsTotalLen + 2;
            // 计算标题内容长度:1
            int tileLegth = title.getBytes("UTF-8").length;
            System.arraycopy(int2bytes(tileLegth), 3, bytes, index, 1);
            index++;
            //累加总长度
            rmsTotalLen = rmsTotalLen + 1;

            // 标题内容
            System.arraycopy(title.getBytes("UTF-8"), 0, bytes, index, tileLegth);
            index += tileLegth;
            //累加总长度
            rmsTotalLen = rmsTotalLen + tileLegth;

            // ----遍历文件Map
            Map<String, byte[]> rmsMap = getRmsMap(filePath);
            Iterator<Map.Entry<String, byte[]>> it = rmsMap.entrySet().iterator();
            Map.Entry<String, byte[]> entry = it.next();
            for (int i = -1; i < list.size(); i++) {
                //文件类型 ：1byte
                //int rfileType = FileNameBytesUtil.getFileType(rfileName);
                int rfileType = 1;
                if (i != -1) {
                    rfileType = Integer.parseInt(list.get(i).get("fileType"));
                }
                System.arraycopy(int2bytes(rfileType), 3, bytes, index, 1);
                index++;
                //累加总长度
                rmsTotalLen = rmsTotalLen + 1;

                if (rfileType == 6) {
                    int messageLength = list.get(i).get("message").getBytes("UTF-8").length;
                    System.arraycopy(int2bytes(messageLength), 0, bytes, index, 4);
                    index += 4;
                    //累加总长度
                    rmsTotalLen = rmsTotalLen + 4;

                    byte[] message = list.get(i).get("message").getBytes("UTF-8");
                    System.arraycopy(message, 0, bytes, index, messageLength);
                    index += messageLength;
                    //累加总长度
                    rmsTotalLen = rmsTotalLen + messageLength;
                } else if (rfileType == 7) {
                    int messageLength = list.get(i).get("message").getBytes("UTF-8").length;
                    System.arraycopy(int2bytes(messageLength), 0, bytes, index, 4);
                    index += 4;
                    //累加总长度
                    rmsTotalLen = rmsTotalLen + 4;

                    byte[] message = list.get(i).get("message").getBytes("UTF-8");
                    System.arraycopy(message, 0, bytes, index, messageLength);
                    index += messageLength;
                    //累加总长度
                    rmsTotalLen = rmsTotalLen + messageLength;
                    continue;
                }

                String rfileName = entry.getKey();

                // 文件文件名的字节长度 :1byte
                int rfileNameLegth = entry.getKey().getBytes("UTF-8").length;
                System.arraycopy(int2bytes(rfileNameLegth), 3, bytes, index, 1);
                index++;
                //累加总长度
                rmsTotalLen = rmsTotalLen + 1;
                // 文件文件名 :实际计算长度
                System.arraycopy(rfileName.getBytes("UTF-8"), 0, bytes, index, rfileNameLegth);
                index += rfileNameLegth;
                //累加总长度
                rmsTotalLen = rmsTotalLen + rfileNameLegth;

                // 文件内容长度: 4byte
                byte[] content = entry.getValue();
                int contLenth = content.length;
                System.arraycopy(int2bytes(contLenth), 0, bytes, index, 4);
                index += 4;
                //累加总长度
                rmsTotalLen = rmsTotalLen + 4;
                // 文件内容：实际计算长度
                System.arraycopy(content, 0, bytes, index, contLenth);
                index += contLenth;
                //累加总长度
                rmsTotalLen = rmsTotalLen + contLenth;

                if (it.hasNext()) {
                    entry = it.next();
                }
            }

            //最后写入RMS总长度
            System.arraycopy(int2bytes(rmsTotalLen), 0, bytes, rmsBgIndex, 4);
            results = new byte[index];
            System.arraycopy(bytes, 0, results, 0, index);
        } catch (Exception e) {
            EmpExecutionContext.error("封装rms文件字节数组出现异常：" + e.toString());
        }
        return results;
    }

    /**
     * 整型转字节
     *
     * @param num ：待转整型数据
     * @return
     */
    private byte[] int2bytes(int num) {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (num >>> (24 - i * 8));
        }
        return b;
    }

    /**
     * 读取资源文件夹下的所有资源文件
     *
     * @param filePath
     * @return
     */
    // Map<序号 ,Map<文件名, byte[]>>
    private Map<String, byte[]> getRmsMap(String filePath) {
        Map<String, byte[]> map = new LinkedHashMap<String, byte[]>();
        File file = new File(filePath);
        if (!file.isDirectory()) {
            EmpExecutionContext.info(filePath + "路径不是一个文件夹");
            return null;
        }
        File[] tempList1 = file.listFiles();
        List<File> tempList = Arrays.asList(tempList1);
        Collections.sort(tempList, new Comparator<File>() {
            public int compare(File o1, File o2) {
                if (o1.isDirectory() && o2.isFile())
                    return -1;
                if (o1.isFile() && o2.isDirectory())
                    return 1;
                return o1.getName().compareTo(o2.getName());
            }
        });


        if (tempList == null || tempList.size() <= 0) {
            EmpExecutionContext.info(filePath + "src文件夹 目录为空");
            return null;
        }
        for (int i = 0; i < tempList.size(); i++) {
            File tempFile = tempList.get(i);
            if (tempFile.getName().endsWith(".smil")) {
                String finlName = tempFile.getName();
                byte[] tempBytes = getFileBytes(tempFile);
                map.put(finlName, tempBytes);
            }
        }
        for (int i = 0; i < tempList.size(); i++) {
            File tempFile = tempList.get(i);
            if (tempFile.getName().endsWith(".smil")) {
                continue;
            }
            String finlName = tempFile.getName();
            byte[] tempBytes = getFileBytes(tempFile);
            map.put(finlName, tempBytes);
        }
        return map;
    }

    /**
     * 读取 src 目录下的资源文件 字节
     *
     * @param tempFile
     * @return
     */
    private byte[] getFileBytes(File tempFile) {
        FileInputStream fis = null;
        ByteArrayOutputStream outStream = null;
        try {
            fis = new FileInputStream(tempFile);
            outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = fis.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }

        } catch (Exception e) {
            EmpExecutionContext.error(e, "读取文件字节流出现异常！");
        } finally {
            try {
                if (null != outStream) {
                    outStream.close();
                }
                if (null != fis) {
                    fis.close();
                }
            } catch (Exception e) {
                EmpExecutionContext.error(e, "读取src目录文件关闭流出现异常");
            }

        }
        return outStream==null?null:outStream.toByteArray();
    }


}
