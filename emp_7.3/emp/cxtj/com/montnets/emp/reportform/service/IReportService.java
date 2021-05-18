package com.montnets.emp.reportform.service;


import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.reportform.bean.ReportVo;
import com.montnets.emp.reportform.bean.Request;
import com.montnets.emp.reportform.dto.AprovinceCityDto;
import com.montnets.emp.util.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 查询统计报表service
 *
 * @author lianghuageng
 * @date te 2018/12/10 17:41
 */
public interface IReportService {
    /**
     * 获取省市信息
     *
     * @return 返回包含省市信息的list集合
     */
    List<AprovinceCityDto> getProvinceAndCity();


    /**
     * 获取从缓存中初始化数据
     *
     * @param user   操作用户
     * @param module 模块名称
     * @return 初始化数据
     */
    String getInitDataByCache(LfSysuser user, String module);

    /**
     * 获取初始化数据
     *
     * @param langName 语言
     * @param btnMap   按钮权限
     * @param titleMap 菜单权限
     * @param corpCode 企业编码
     * @param user     操作员
     * @param module   模块名称
     * @return 包含初始化后数据的Json
     */
    String getInitData(String langName, Map<String, String> btnMap, Map<String, String> titleMap, String corpCode, LfSysuser user, String module);

    /**
     * 获取跳转路径
     *
     * @param pathUrl 请求路径
     * @return 跳转路径
     */
    String getJumpModulePath(String pathUrl);

    /**
     * 查询报表数据入口方法
     *
     * @param sysUser     操作员对象
     * @param queryEntity 查询实体类
     * @param page        分页对象
     * @param module      模块名字
     * @return 返回包含报表信息的list
     */
    List<ReportVo> findMtDataRptByModuleName(LfSysuser sysUser, ReportVo queryEntity, String module, PageInfo page);

    /**
     * 点击详情按钮时
     *
     * @param user          操作员对象
     * @param queryEntity   查询实体类
     * @param requestEntity 自定义Request对象
     * @return 返回包含报表信息的list
     */
    List<ReportVo> findMtDataRptDetail(LfSysuser user, ReportVo queryEntity, Request requestEntity);

    /**
     * 将service返回的list集合加上总条数转换为json传给前端
     *
     * @param reportVos list结果集合
     * @param totalRec  总条数
     * @return json串
     */
    String handleReportVoList2Json(List<ReportVo> reportVos, int totalRec);

    /**
     * 根据机构Id获取机构树信息
     * @param depId 机构Id
     * @param flag 是否获取操作员树 默认获取机构树
     * @return 结果Json串
     */
    String getDeptOrUserTree(String depId, boolean flag);

    /**
     * 懒加载方式获取机构树信息
     * @param depId 机构Id
     * @param flag 否获取操作员树 默认获取机构树
     * @return 结果Json串
     */
    String getDeptOrUserTreeByLazy(String depId, boolean flag);
}
