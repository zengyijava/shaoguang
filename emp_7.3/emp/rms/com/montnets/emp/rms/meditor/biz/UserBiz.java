package com.montnets.emp.rms.meditor.biz;

import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.rms.entity.LfDfadvanced;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;

public interface UserBiz {

    boolean hasCreatePublicTemp(Long userId, HttpServletRequest request, HttpServletResponse response);
    String getAuthority(Long userId, HttpServletRequest request, HttpServletResponse response);
    String getModulePer(Long userId, HttpServletRequest request, String corpCode, String configName);
    LfSysuser getUserByCondition(LinkedHashMap<String,String> conditionMap);
    /**
     * 处理员工机构
     *
     * @param phoneStr 手机号
     * @param empDepIds 员工机构Id集合
     * @param lgcorpcode 企业编码
     * @return 手机号字符串
     */
    String getEmployeePhoneSrrByDepId(String phoneStr, String empDepIds, String lgcorpcode);
    /**
     * 处理客户机构
     *
     * @param phoneStr 手机号
     * @param cliDepIds 客户机构Id集合
     * @param lgcorpcode 企业编码
     * @return 手机号字符串
     */
    String getClientPhoneStrByDepId(String phoneStr, String cliDepIds, String lgcorpcode);
    /**
     * 处理群组
     *
     * @param phoneStr 手机号
     * @param groupIds 群组
     * @return 手机号字符串
     */
    String getGroupPhoneStrById(String phoneStr, String groupIds);
    /**
     * 高级设置存为默认
     * @param conditionMap 删除原来设置条件
     * @param lfDfadvanced 更新默认高级设置对象
     * @return 成功为"success"，失败为"fail"
     */
    String setDefault(LinkedHashMap<String, String> conditionMap, LfDfadvanced lfDfadvanced);
}
