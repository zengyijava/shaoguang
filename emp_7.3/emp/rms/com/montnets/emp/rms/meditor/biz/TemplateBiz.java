package com.montnets.emp.rms.meditor.biz;

import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.rms.templmanage.biz.RmsShortTemplateBiz;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface TemplateBiz {
    /**
     * 设为快捷场景
     */
    void addShotCutTem(String tmId, String tmName, HttpServletRequest request, HttpServletResponse response);

    /**
     * 取消快捷场景
     * @param tmId
     * @param request
     * @param response
     */
    void delShotCutTem(String tmId,HttpServletRequest request, HttpServletResponse response);

    /**
     * 模板同步
     */
    void TempSynch();

    /**
     * rms目录生成
     * @param basePath  生成在哪个目录下
     * @param contentIn  字节数组
     * @return
     */
    boolean rmsFileCreate(String basePath,byte[] contentIn);

    /**
     * rms目录生成
     * @param basePath  生成在哪个目录下
     * @param contentIn  字节数组
     * @param lfTemplate  模板对象，用于存储解析文件中的模板相关数据
     * @return
     */
    boolean rmsFileCreate(String basePath, byte[] contentIn, LfTemplate lfTemplate);

    /**
     * ott目录生成
     * @param basePath 生成在哪个目录下
     * @param contentIn 字节数组
     * @return
     */
    boolean ottFileCreate(String basePath,byte[] contentIn);

    /**
     * ott目录生成
     * @param basePath  生成在哪个目录下
     * @param contentIn  字节数组
     * @param lfTemplate  模板对象，用于存储解析文件中的模板相关数据
     * @return
     */
    boolean ottFileCreate(String basePath, byte[] contentIn, LfTemplate lfTemplate);
}
