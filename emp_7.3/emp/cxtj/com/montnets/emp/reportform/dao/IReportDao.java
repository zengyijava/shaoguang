package com.montnets.emp.reportform.dao;

import com.montnets.emp.entity.report.AprovinceCity;
import com.montnets.emp.entity.selfparam.LfWgParmDefinition;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.reportform.bean.ReportVo;
import com.montnets.emp.util.PageInfo;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 查询统计报表 Dao 层
 * @author lianghuageng
 * @date 2018/12/10 17:48
 */
public interface IReportDao {

    /**
     * 获取省市信息
     * @return 包含省市信息的list 集合
     */
    List<AprovinceCity> getProvinceAndCity();

    /**
     * 根据管辖范围获取机构id集合
     * @param conditionMap 条件集合
     * @return 管辖范围内的机构id集合
     */
    List<LfDep> getDepByDomination(LinkedHashMap<String, String> conditionMap);

    /**
     * 通过机构Id集合找到对应的操作员
     * @param toString 机构Id集合
     * @return 对应的操作员集合
     */
    List<LfSysuser> findSysUserByDepIds(String toString);

    /**
     * 根据模块名字获取对应的报表信息
     * @param sysUser 操作员对象
     * @param queryEntity 查询实体类对象
     * @param module 模块名字
     * @param page 分页对象 可以为null
     * @return key为总条数与value为list集合的map
     */
    List<ReportVo> findMtDataRptByModuleName(LfSysuser sysUser, ReportVo queryEntity, String module, PageInfo page);

    /**
     * 获取自定义参数列表
     * @param conditionMap 条件集合
     * @param module 模块名字
     * @return 参数列表集合
     */
    List<LfWgParmDefinition> getLfWgParmConfList(LinkedHashMap<String, String> conditionMap, String module);

    /**
     * 根据操作员Id获取其管辖机构
     * @param userId 操作员Id
     * @return 机构list集合
     */
    List<LfDep> getDominationByUserId(Long userId);

    /**
     * 根据机构Id找到下级子机构
     * @param depId 机构Id
     * @return 集合
     */
    List<LfDep> getChildrenDepById(String depId);

    /**
     * 获取指定机构下的操作员
     * @param depId 机构Id
     * @return 集合
     */
    List<LfSysuser> getSysUserById(Long depId);
}
