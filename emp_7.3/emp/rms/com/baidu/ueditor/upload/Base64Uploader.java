package com.baidu.ueditor.upload;

import com.baidu.ueditor.PathFormat;
import com.baidu.ueditor.define.AppInfo;
import com.baidu.ueditor.define.BaseState;
import com.baidu.ueditor.define.FileType;
import com.baidu.ueditor.define.State;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.rms.meditor.tools.String2FileUtil;
import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public final class Base64Uploader {

    public static State save(String content, Map<String, Object> conf) {

        byte[] data = decode(content);

        long maxSize = ((Long) conf.get("maxSize")).longValue();

        if (!validSize(data, maxSize)) {
            return new BaseState(false, AppInfo.MAX_SIZE);
        }

        String suffix = FileType.getSuffix("JPG");

        String savePath = PathFormat.parse((String) conf.get("savePath"),
                (String) conf.get("filename"));

        savePath = savePath + suffix;
        String physicalPath = (String) conf.get("rootPath") + savePath;

        State storageState = StorageManager.saveBinaryFile(data, physicalPath);
        try {
            if (storageState.isSuccess()) {

                storageState.putInfo("url", PathFormat.format(savePath));
                storageState.putInfo("type", suffix);
                storageState.putInfo("original", "");
            }

            File file = new File(physicalPath);
            //读取图片对象
            BufferedImage img = ImageIO.read(file);
            //获得图片的宽
            int width = img.getWidth();
            //获得图片的高
            int height = img.getHeight();
            String ratio = String.valueOf(new BigDecimal(width).divide(new BigDecimal(height),2,RoundingMode.HALF_UP));
            storageState.putInfo("width", width);
            storageState.putInfo("height", height);
            storageState.putInfo("ratio",ratio);
            String type = "2";

            String h5url = String2FileUtil.uploadFile(file, savePath, "2", type);
            storageState.putInfo("url", h5url);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            EmpExecutionContext.error(e, "发现异常！");
        }
        return storageState;
    }

    private static byte[] decode(String content) {
        return Base64.decodeBase64(content.getBytes());
    }

    private static boolean validSize(byte[] data, long length) {
        return data.length <= length;
    }

}