package com.montnets.emp.rms.meditor.biz;


import com.alibaba.fastjson.JSONObject;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.rms.commontempl.entity.LfIndustryUse;
import com.montnets.emp.rms.meditor.entity.TempData;
import com.montnets.emp.rms.meditor.vo.TempHFiveVo;
import com.montnets.emp.rms.meditor.vo.TempsVo;
import com.montnets.emp.rms.vo.LfTemplateChartVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface MeditorBiz {

    /**
     * 获取模板列表
     * @return
     */
    void getTemps(TempsVo tempsVo, HttpServletRequest request,HttpServletResponse response);

    /**
     * 删除模板
     * @param tempsVo
     * @param request
     * @param response
     * @throws Exception
     */
    void deleteTemp(TempsVo tempsVo, HttpServletRequest request,HttpServletResponse response);
    
    /**
     * 模板启禁用
     * @param tempsVo
     * @param request
     * @param response
     * @throws Exception
     */
    void changeTmState(TempsVo tempsVo,HttpServletRequest request,HttpServletResponse response);
    
    /**
     * 图表生成
     * @param request
     * @param response
     * @throws Exception
     */
    void createPicture(LfTemplateChartVo templateChartVo,HttpServletRequest request, HttpServletResponse response);
    
    
    /**
     * 查询模板详情
     * @param tempsVo
     * @param request
     * @param response
     * @throws Exception
     */
    void getTempDetail(TempsVo tempsVo,HttpServletRequest request,HttpServletResponse response);
    
    /**
     * 新增行业用途
     * @param request
     * @param response
     * @throws Exception
     */
    void addIndustryUse(String name,String type,String tmpType,HttpServletRequest request, HttpServletResponse response);

    /**
     * 修改行业用途
     * @param request
     * @param response
     * @throws Exception
     */
    void updateIndustryUse(LfIndustryUse lfIndustryUse,HttpServletRequest request,HttpServletResponse response);
    
    /**
     * 删除行业用途
     * @param id
     * @param request
     * @param response
     * @throws Exception
     */
    void deleteIndustryUse(String id,HttpServletRequest request, HttpServletResponse response);
    
    /**
     * 获取行业用途
     * @param name
     * @param type
     * @param request
     * @param response
     * @throws Exception
     */
    void getIndustryUses(String name,String type,String tmpType,HttpServletRequest request, HttpServletResponse response);
    
    /**
     * 添加模板
     * @param tmpData
     * @param request
     * @param response
     * @throws Exception
     */
    void addTemplate(TempData tmpData,HttpServletRequest request,HttpServletResponse response);


    /**
     * 快捷场景设置
     * @param tempsVo
     * @param request
     * @param response
     */
    void setShotCutTem(TempsVo tempsVo,HttpServletRequest request,HttpServletResponse response);

    /**
     * 获取用户信息
     * @param tmpType
     * @param request
     * @param response
     * @throws Exception
     */
    /**
     * 根据模板类型查询行业用途
     * @param tmpType
     * @param request
     * @param response
     * @throws Exception
     */
    void getUse(String tmpType,HttpServletRequest request,HttpServletResponse response)throws Exception;
    
    /**
     * H5-添加
     * @param tempHFiveVo
     * @param request
     * @param response
     */
    void addH5(TempHFiveVo tempHFiveVo,HttpServletRequest request,HttpServletResponse response)throws Exception;
    
    /**
     * H5-查询列表
     * @param tempHFiveVo
     * @param request
     * @param response
     * @throws Exception
     */
    void getH5s(TempHFiveVo tempHFiveVo,HttpServletRequest request,HttpServletResponse response)throws Exception;
    
    /**
     * H5-删除
     * @param hId
     * @param request
     * @param response
     * @throws Exception
     */
    void deleteH5(String hId,HttpServletRequest request,HttpServletResponse response)throws Exception;
    
    /**
     * H5-编辑
     * @param tempHFiveVo
     * @param request
     * @param response
     * @throws Exception
     */
    void updateH5(TempHFiveVo tempHFiveVo,HttpServletRequest request,HttpServletResponse response)throws Exception;
    
    /**
     * H5-详情查看
     * @param hId
     * @param request
     * @param response
     * @throws Exception
     */
    void getH5Detail(String hId,HttpServletRequest request,HttpServletResponse response)throws Exception;

    /**
     * 数据上传
     * @param request
     * @param response
     * @throws Exception
     */
    void uploadFile(HttpServletRequest request,HttpServletResponse response)throws Exception;

    /**
     * 保存html
     * @param request
     * @param response
     * @throws Exception
     */
    void saveFodder(HttpServletRequest request,HttpServletResponse response)throws Exception;

    /**
     * 获取素材列表
     * @param request
     * @param response
     * @throws Exception
     */
    void getFodder(HttpServletRequest request, HttpServletResponse response) throws Exception;

    /**
     * 删除素材
     * @param request
     * @param response
     * @throws Exception
     */
    void deleteFodder(List<Integer> fodderIds, HttpServletRequest request, HttpServletResponse response)throws Exception;

    /**
     * 根据自己获取模板id
     * @param tmId
     * @param request
     * @param response
     */
    void getTempById(Long tmId,HttpServletRequest request, HttpServletResponse response);

    /**
     * 模板共享
     * @param depidstr
     * @param useridstr
     * @param tempId
     * @param InfoType
     * @param lfsysuser
     * @return
     */
    String updateShareTemp(String depidstr, String useridstr, String tempId, String InfoType, LfSysuser lfsysuser);

    /**
     * 是否是共享模板
     * @param tmId
     * @param lfSysuser
     * @return
     */
    boolean isShare(Long tmId,LfSysuser lfSysuser);

    /**
     * 图文合成
     * @param jsonObject
     * @param request
     * @param response
     */
    void compoundImageText(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response);
}
