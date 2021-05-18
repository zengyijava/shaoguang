package com.montnets.emp.rms.meditor.tools;

import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.rms.rmsapi.util.OOSUtil;
import com.montnets.emp.util.StringUtils;
import com.montnets.emp.util.TxtFileUtil;

import java.io.File;


public class ZipDownUtil {
    private OOSUtil oosUtil = new OOSUtil();
    private CommonBiz commBiz = new CommonBiz();
    //传入相对路径,需在配置文件服务器地址
    public boolean tempalteDown(String relativePath,Long tmState){
        TxtFileUtil txtfileutil = new TxtFileUtil();
        String dirUrl = txtfileutil.getWebRoot();
        if (StringUtils.isBlank(relativePath)){
            EmpExecutionContext.error("未获取到文件相对路径:"+relativePath);
            return false;
        }
        //确认本地文件夹是否存在
        File fFrame = new File(dirUrl+relativePath.substring(0,relativePath.indexOf(".zip")));
        //文件存在则不用同步
        if (fFrame.exists()){
            if (null == tmState){
               return true;
            }else {
                if (tmState != 2){//草稿需要重新下载，避免每个节点模板文件不一致的情况
                    return true;
                }
            }
        }

        //从文件服务器下载文件到本地
        long fileStartTm = System.currentTimeMillis();
        String downFileZip = null;

        //单独抓取下载异常，某个模板文件下载异常时继续后面的文件下载
        try{
            downFileZip = commBiz.downFileFromFileCenWhitZip(relativePath);
        }catch (Exception e){
            EmpExecutionContext.error(e,"下载文件异常:"+relativePath);
        }
        long fileEndTm = System.currentTimeMillis();
        EmpExecutionContext.info(relativePath+"文件服务器下载耗时：" +(fileEndTm - fileStartTm)+" ms");
        if(!"success".equals(downFileZip)){
            EmpExecutionContext.error(relativePath+"文件服务器下载失败!");
            //文件服务器没有下载成功,则从阿里云下载 zip
            //oosUtil.downLoadFile(sourcPath,destPath, rmsZipName)
            return false;
        }else {
            return true;
        }
    }
    public boolean tempalteDown(String relativePath){
        Long tmState = null;
        return tempalteDown(relativePath, tmState);
    }
}
