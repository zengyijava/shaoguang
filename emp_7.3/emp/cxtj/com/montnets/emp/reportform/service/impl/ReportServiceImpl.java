package com.montnets.emp.reportform.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.montnets.EMPException;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.ViewParams;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.biztype.LfBusManager;
import com.montnets.emp.entity.report.AprovinceCity;
import com.montnets.emp.entity.selfparam.LfWgParmDefinition;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.query.biz.QueryBiz;
import com.montnets.emp.reportform.bean.CacheBean;
import com.montnets.emp.reportform.bean.ReportVo;
import com.montnets.emp.reportform.bean.Request;
import com.montnets.emp.reportform.bean.SendType;
import com.montnets.emp.reportform.cache.DepAndUserTreeInfoCache;
import com.montnets.emp.reportform.cache.InitDataCache;
import com.montnets.emp.reportform.cache.ProvinceAndCityCache;
import com.montnets.emp.reportform.cxtjenum.JumpPathEnum;
import com.montnets.emp.reportform.cxtjenum.OperatorEnum;
import com.montnets.emp.reportform.cxtjenum.SendTypeEnum;
import com.montnets.emp.reportform.dao.IReportDao;
import com.montnets.emp.reportform.dao.impl.ReportDaoImpl;
import com.montnets.emp.reportform.dto.AprovinceCityDto;
import com.montnets.emp.reportform.dto.DepOrUserTreeDto;
import com.montnets.emp.reportform.service.IReportService;
import com.montnets.emp.reportform.sql.ReportMtDataRptSql;
import com.montnets.emp.reportform.util.FastJsonUtils;
import com.montnets.emp.util.PageInfo;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * 查询统计报表 service 实现类
 *
 * @author lianghuageng
 * @date 2018/12/10 17:41
 */
public class ReportServiceImpl implements IReportService {

    private final IReportDao reportDao = new ReportDaoImpl();
    private final QueryBiz queryBiz = new QueryBiz();
    private final BaseBiz baseBiz = new BaseBiz();
    private final Gson gson = new Gson();

    @Override
    public List<AprovinceCityDto> getProvinceAndCity() {
        List<AprovinceCityDto> aProvinceDtos = null;
        try {
            //先从缓存取，如果取不到再查询库
            aProvinceDtos = ProvinceAndCityCache.INSTANCE.getProvinceAndCity();
            if (null != aProvinceDtos && aProvinceDtos.size() > 0) {
                return aProvinceDtos;
            }
            HashMap<String, List<AprovinceCityDto>> provinceMap = new HashMap<String, List<AprovinceCityDto>>(16);
            List<AprovinceCity> aprovinceCities = reportDao.getProvinceAndCity();
            if (null != aprovinceCities && aprovinceCities.size() > 0) {
                for (AprovinceCity aprovinceCity : aprovinceCities) {
                    //判断是否已存在
                    if (provinceMap.containsKey(aprovinceCity.getProvince() + "&" + aprovinceCity.getProvinceCode())) {
                        List<AprovinceCityDto> aprovinceCityDtos = provinceMap.get(aprovinceCity.getProvince() + "&" + aprovinceCity.getProvinceCode());
                        AprovinceCityDto cityDto = new AprovinceCityDto();
                        cityDto.setValue(aprovinceCity.getAreaCode().toString());
                        cityDto.setLabel(aprovinceCity.getCity());
                        aprovinceCityDtos.add(cityDto);
                    } else {
                        List<AprovinceCityDto> cityList = new ArrayList<AprovinceCityDto>();
                        AprovinceCityDto cityDto = new AprovinceCityDto();
                        cityDto.setValue(aprovinceCity.getAreaCode().toString());
                        cityDto.setLabel(aprovinceCity.getCity());
                        cityList.add(cityDto);
                        provinceMap.put(aprovinceCity.getProvince() + "&" + aprovinceCity.getProvinceCode(), cityList);
                    }
                }
            }
            //遍历Map
            aProvinceDtos = new ArrayList<AprovinceCityDto>();
            for (Map.Entry<String, List<AprovinceCityDto>> entry : provinceMap.entrySet()) {
                AprovinceCityDto cityDto = new AprovinceCityDto();
                if (entry.getValue().size() > 1) {
                    cityDto.setValue(entry.getKey().split("&")[1]);
                    cityDto.setLabel(entry.getKey().split("&")[0]);
                    cityDto.setChildren(entry.getValue());
                    aProvinceDtos.add(cityDto);
                } else {
                    cityDto.setValue(entry.getKey().split("&")[1]);
                    cityDto.setLabel(entry.getKey().split("&")[0]);
                    aProvinceDtos.add(cityDto);
                }
            }
            ProvinceAndCityCache.INSTANCE.setProvinceAndCity(aProvinceDtos);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "--> ReportServiceImpl.getProvinceAndCity() 获取省市地区失败");
        }
        return aProvinceDtos;
    }

    @Override
    public String getInitDataByCache(LfSysuser user, String module) {
        try {
            if (null == user) {
                throw new EMPException("操作员信息异常!");
            }
            Map<String, CacheBean<String>> cacheMap = InitDataCache.INSTANCE.get(user.getUserId());
            if (null != cacheMap) {
                CacheBean<String> cache = cacheMap.get(module);
                if (null != cache) {
                    Long currentTime = System.currentTimeMillis();
                    Long cacheTime = cache.getCacheTime();
                    // 如果时间大于2分钟, 就从数据库中取
                    if (currentTime - cacheTime > (2 * 60 * 1000)) {
                        //初始化机构与操作员树
                        initUserAndDepTree(user.getCorpCode());
                        return cache.getCache();
                    }
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "--> ReportServiceImpl.getInitDataByCache() 从缓存中获取初始化数据出错!");
        }
        return null;
    }

    @Override
    public String getInitData(String langName, Map<String, String> btnMap, Map<String, String> titleMap, String corpCode, LfSysuser user, String module) {
        try {
            //机构树与操作员树结果集
            List<DepOrUserTreeDto> depTreeInfo = null;
            List<DepOrUserTreeDto> userTreeInfo = null;
            //机构数据权限
            if (2 == user.getPermissionType()) {
                // 初始化树(初始化整棵树)
                initUserAndDepTree(user.getCorpCode());
                //如果为10W号则直接返回
                if("100000".equals(corpCode)){
                    depTreeInfo = DepAndUserTreeInfoCache.DEP_TREE;
                    userTreeInfo = DepAndUserTreeInfoCache.USER_TREE;
                }else {
                    //获取当前操作员最高管辖机构Id
                    LfDep dep = getTopDepDominationId(user);
                    //机构信息 返回当前操作员的管辖机构的最高机构的名字以及id
                    List<DepOrUserTreeDto> depChildrenTree = findChildrenById("org_" + dep.getDepId(), DepAndUserTreeInfoCache.DEP_TREE);
                    depTreeInfo = handleTopUserOrDepTree(depChildrenTree, dep);
                    //操作员信息
                    List<DepOrUserTreeDto> userChildrenTree = findChildrenById("org_" + dep.getDepId(), DepAndUserTreeInfoCache.USER_TREE);
                    userTreeInfo = handleTopUserOrDepTree(userChildrenTree, dep);
                }
            }
            //短信SP账号
            List<String> smsSpUser = queryBiz.getSpUserList("0", corpCode, StaticValue.getCORPTYPE());
            //彩信SP账号
            List<String> mmsSpUser = queryBiz.getSpUserList("1", corpCode, StaticValue.getCORPTYPE());
            //省市信息
            List<AprovinceCityDto> provinceAndCity = getProvinceAndCity();
            //运营商账户ID 短信
            List<String> smsuserList = getListByMsType(0);
            //运营商账户ID 彩信
            List<String> mmsuserList = getListByMsType(1);
            //根据企业编码查询业务类型
            LinkedHashMap<String, String> conditionBusMap = new LinkedHashMap<String, String>();
            conditionBusMap.put("corpCode&in", "0," + corpCode);
            List<LfBusManager> busList = baseBiz.getByCondition(
                    LfBusManager.class, conditionBusMap, null);
            busList.addAll(0, getLfBusManager());
            //获取动态参数表信息
            List<LfWgParmDefinition> paramList = getLfWgParmConfList(user.getCorpCode(), module);
            // 这个参数需要一开始带过来，然后确定跳转的页面
            String menuCode = titleMap.get(module);
            menuCode = menuCode == null ? "0-0-0" : menuCode;
            //获取发送类型
            List<SendType> sendTypeList = getSendType(btnMap, menuCode, module);
            //获取账号类型
            List<SendType> accTypeList = getAccType(btnMap, menuCode);
            boolean access = btnMap.containsKey(menuCode + "-0");
            String position = ViewParams.getPosition(langName, menuCode);
            Integer permissionType = user.getPermissionType();
            Map<String, Object> map = new HashMap<String, Object>(15);
            map.put("permissionType", permissionType);
            map.put("menuCode", menuCode);
            map.put("position", position);
            map.put("access", access);
            map.put("module", module);
            map.put("smsSpUser", smsSpUser);
            map.put("mmsSpUser", mmsSpUser);
            map.put("paramList", paramList);
            map.put("provinceAndCity", provinceAndCity);
            map.put("depTreeInfo", depTreeInfo);
            map.put("userTreeInfo", userTreeInfo);
            map.put("busList", busList);
            map.put("sendTypeList", sendTypeList);
            map.put("accTypeList", accTypeList);
            map.put("smsuserList", smsuserList);
            map.put("mmsuserList", mmsuserList);
            map.put("corpCode", corpCode);

            // 转化结果集
            String result = gson.toJson(map);
            // 存缓存
            putCache(result, module, user);

            return result;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "-->ReportServiceImpl.getInitData(Object...) 获取初始化数据异常");
        }
        return null;
    }

    private List<DepOrUserTreeDto> handleTopUserOrDepTree(List<DepOrUserTreeDto> childrenTree, LfDep dep) {
        List<DepOrUserTreeDto> finalResult = new ArrayList<DepOrUserTreeDto>();
        DepOrUserTreeDto treeDto = new DepOrUserTreeDto();
        treeDto.setId("org_" + dep.getDepId());
        treeDto.setLabel(dep.getDepName());
        treeDto.setChildren(childrenTree);
        finalResult.add(treeDto);
        return finalResult;
    }

    private LfDep getTopDepDominationId(LfSysuser user) {
        //根据管辖权限查询所有的机构
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>(16);
        conditionMap.put("depState", "1");
        if (StaticValue.getCORPTYPE() == 1) {
            //企业编码
            conditionMap.put("corpCode", user.getCorpCode());
        }
        //操作员Id
        conditionMap.put("userId", user.getUserId().toString());
        List<LfDep> lfDeps = getDepByDomination(conditionMap);
        return lfDeps.get(0);
    }

    /**
     * 初始化整棵树，将树放到内存中
     */
    private void initUserAndDepTree(String corpCode) {
        //机构信息
        List<DepOrUserTreeDto> depTreeInfo;
        //操作员信息
        List<DepOrUserTreeDto> userTreeInfo;

        //如果是标准版则直接加载整棵树，但托管版考虑到内存先加载顶级机构
        if(StaticValue.getCORPTYPE() == 0){
            depTreeInfo = getAllDepOrUserTreeInfo(false);
            userTreeInfo = getAllDepOrUserTreeInfo(true);
        }else {
            depTreeInfo = getAllDepOrUserTreeInfo(corpCode);
            userTreeInfo = new ArrayList<DepOrUserTreeDto>(depTreeInfo);
        }
        DepAndUserTreeInfoCache.clear();
        DepAndUserTreeInfoCache.DEP_TREE.addAll(depTreeInfo);
        DepAndUserTreeInfoCache.USER_TREE.addAll(userTreeInfo);
    }

    private HashMap<String, List<LfDep>> getDepMapByCorpCode(LinkedHashMap<String, String> conditionMap) throws Exception{
        HashMap<String, List<LfDep>> map = new HashMap<String, List<LfDep>>(16);
        List<LfDep> lfDeps = baseBiz.getByCondition(LfDep.class, conditionMap, null);
        //按照企业编码分组
        for (LfDep dep : lfDeps) {
            if (map.containsKey(dep.getCorpCode())) {
                map.get(dep.getCorpCode()).add(dep);
            } else {
                List<LfDep> deps = new ArrayList<LfDep>();
                deps.add(dep);
                map.put(dep.getCorpCode(), deps);
            }
        }
        return map;
    }

    private List<DepOrUserTreeDto> getAllDepOrUserTreeInfo(boolean needSysUser) {

        List<DepOrUserTreeDto> depOrUserTreeDtos = new ArrayList<DepOrUserTreeDto>();
        try {
            //先查询机构
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("depState", "1");
            HashMap<String, List<LfDep>> map = getDepMapByCorpCode(conditionMap);
            for (Map.Entry<String, List<LfDep>> entry : map.entrySet()) {
                HashMap<Integer, List<LfSysuser>> userByDepIds = needSysUser ? findSysUserByDepIds(entry.getValue()) : null;
                List<DepOrUserTreeDto> dtos = handleDep2TreeInfoList(entry.getValue(), userByDepIds);
                depOrUserTreeDtos.addAll(dtos);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "--> ReportServiceImpl.getAllDepOrUserTreeInfo(boolean needSysUser) 初始化整棵树数据异常");
        }
        return depOrUserTreeDtos;
    }

    private List<DepOrUserTreeDto> getAllDepOrUserTreeInfo(String corpCode){
        List<DepOrUserTreeDto> depOrUserTreeDtos = new ArrayList<DepOrUserTreeDto>();
        try {
            //先查询机构
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("depState", "1");
            conditionMap.put("depLevel", "1");
            conditionMap.put("corpCode", "100000".equals(corpCode) ? "":corpCode);
            HashMap<String, List<LfDep>> map = getDepMapByCorpCode(conditionMap);
            for (Map.Entry<String, List<LfDep>> entry : map.entrySet()) {
                DepOrUserTreeDto treeDto = new DepOrUserTreeDto();
                treeDto.setId("org_" + entry.getValue().get(0).getDepId().toString());
                treeDto.setLabel(entry.getValue().get(0).getDepName());
                depOrUserTreeDtos.add(treeDto);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "--> ReportServiceImpl.getAllDepOrUserTreeInfo(boolean needSysUser, String corpCode) 初始化整棵树数据异常");
        }
        return depOrUserTreeDtos;
    }
    /**
     * 添加内容到缓存中
     *
     * @param result 需要存缓存的内容
     * @param module 模块名字
     * @param user   操作员对象
     */
    private void putCache(String result, String module, LfSysuser user) {
        // 加入缓存中
        // 获取时间戳
        long currentTime = System.currentTimeMillis();
        CacheBean<String> cache = new CacheBean<String>(currentTime, result);
        Map<String, CacheBean<String>> cacheMap = new HashMap<String, CacheBean<String>>(16);
        cacheMap.put(module, cache);
        InitDataCache.INSTANCE.put(user.getUserId(), cacheMap);
    }

    /**
     * 获取账号类型
     *
     * @param btnMap   按钮权限
     * @param menuCode 菜单编码
     * @return 集合
     */
    private List<SendType> getAccType(Map<String, String> btnMap, String menuCode) {
        List<SendType> accTypeList = new ArrayList<SendType>();

        getWhole(accTypeList, (null != btnMap.get(menuCode + "-1") && null != btnMap.get(menuCode + "-2")));

        if (null != btnMap.get(menuCode + "-1")) {
            setSendTypeList(accTypeList, 1, "EMP应用");
        }
        if (null != btnMap.get(menuCode + "-2")) {
            setSendTypeList(accTypeList, 2, "EMP接入");
        }
        return accTypeList;
    }

    private void getWhole(List<SendType> accTypeList, boolean whole) {
        if (whole) {
            setSendTypeList(accTypeList, SendTypeEnum.WHOLE_SEND.getCode(), SendTypeEnum.WHOLE_SEND.getName());
        }
    }

    private void setSendTypeList(List<SendType> accTypeList, int typeFlag, String typeName) {
        SendType accType = new SendType();
        accType.setSendType(typeFlag);
        accType.setSendName(typeName);
        accTypeList.add(accType);
    }

    /**
     * 获取业务类型的无业务类型
     *
     * @return list集合
     */
    private List<LfBusManager> getLfBusManager() {
        List<LfBusManager> busManagerList = new ArrayList<LfBusManager>();
        LfBusManager busManager = new LfBusManager();
        busManager.setBusCode("");
        busManager.setBusName("全部");
        busManagerList.add(busManager);
        busManager = new LfBusManager();
        busManager.setBusCode("-1");
        busManager.setBusName("无业务类型");
        busManagerList.add(busManager);
        return busManagerList;
    }

    /**
     * 获取发送类型
     *
     * @param btnMap   按钮权限
     * @param menuCode 菜单编码
     * @param module   模块
     * @return list集合
     */
    public List<SendType> getSendType(Map<String, String> btnMap, String menuCode, String module) {
        List<SendType> sendTypeList = new ArrayList<SendType>();

        getWhole(sendTypeList, (btnMap.get(menuCode + "-1") != null && btnMap.get(menuCode + "-2") != null));

        if (btnMap.get(menuCode + "-1") != null) {
            setSendTypeList(sendTypeList, SendTypeEnum.EMP_SEND.getCode(), SendTypeEnum.EMP_SEND.getName());
        }
        if (JumpPathEnum.busReport.getUrl().equals(module) || JumpPathEnum.sysDepReport.getUrl().equals(module)) {
            if (btnMap.get(menuCode + "-2") != null) {
                setSendTypeList(sendTypeList, SendTypeEnum.HTTP_SEND.getCode(), SendTypeEnum.HTTP_SEND.getName());
            }
            if (btnMap.get(menuCode + "-3") != null) {
                setSendTypeList(sendTypeList, SendTypeEnum.DB_SEND.getCode(), SendTypeEnum.DB_SEND.getName());
            }
            if (btnMap.get(menuCode + "-4") != null) {
                setSendTypeList(sendTypeList, SendTypeEnum.DIRECT_SEND.getCode(), SendTypeEnum.DIRECT_SEND.getName());
            }
        } else if (JumpPathEnum.spMtReport.getUrl().equals(module)) {
            if (btnMap.get(menuCode + "-2") != null) {
                setSendTypeList(sendTypeList, SendTypeEnum.HTTP_SEND.getCode(), SendTypeEnum.HTTP_SEND.getName());
                setSendTypeList(sendTypeList, SendTypeEnum.DB_SEND.getCode(), SendTypeEnum.DB_SEND.getName());
                setSendTypeList(sendTypeList, SendTypeEnum.DIRECT_SEND.getCode(), SendTypeEnum.DIRECT_SEND.getName());
            }
        }
        return sendTypeList;
    }

    /**
     * 获取动态参数表信息
     *
     * @param corpCode 企业编码
     * @param module   模块名字
     * @return 集合
     */
    private List<LfWgParmDefinition> getLfWgParmConfList(String corpCode, String module) {
        List<LfWgParmDefinition> paramList = new ArrayList<LfWgParmDefinition>();
        try {
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            String depcodethird2p2 = ResourceBundle.getBundle("SystemGlobals").getString("depcodethird2p2");
            if ("true".equals(depcodethird2p2)) {
                conditionMap.put("param&<>", "Param2");
            }
            if (StaticValue.getCORPTYPE() == 1 && ReportMtDataRptSql.CORPCODE_10W.equals(corpCode)) {
                conditionMap.put("corpCode", corpCode);
            }
            paramList = reportDao.getLfWgParmConfList(conditionMap, module);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "--> ReportServiceImpl.getLfWgParmConfList(String corpCode, String module) 获取动态参数表信息异常");
        }
        return paramList;
    }

    private HashMap<Integer, List<LfSysuser>> findSysUserByDepIds(List<LfDep> lfDeps) {
        HashMap<Integer, List<LfSysuser>> hashMap = new HashMap<Integer, List<LfSysuser>>(16);
        StringBuilder depIds = new StringBuilder();
        for (LfDep dep : lfDeps) {
            depIds.append(dep.getDepId()).append(",");
        }
        if (depIds.length() > 0) {
            depIds.deleteCharAt(depIds.lastIndexOf(","));
        }
        List<LfSysuser> lfSysuserList = reportDao.findSysUserByDepIds(depIds.toString());
        for (LfSysuser lfSysuser : lfSysuserList) {
            Integer depId = Integer.valueOf(lfSysuser.getDepId().toString());
            if (hashMap.containsKey(depId)) {
                hashMap.get(depId).add(lfSysuser);
            } else {
                List<LfSysuser> lfSysusers = new ArrayList<LfSysuser>();
                lfSysusers.add(lfSysuser);
                hashMap.put(Integer.valueOf(lfSysuser.getDepId().toString()), lfSysusers);
            }
        }
        return hashMap;
    }

    private List<DepOrUserTreeDto> handleDep2TreeInfoList(List<LfDep> lfDeps, HashMap<Integer, List<LfSysuser>> userByDepIds) {
        List<DepOrUserTreeDto> depOrUserTreeDtos = new ArrayList<DepOrUserTreeDto>();
        if (lfDeps != null && lfDeps.size() > 0) {
            //按照DEP_LEVEL排序
            Collections.sort(lfDeps, new Comparator<LfDep>() {
                @Override
                public int compare(LfDep o1, LfDep o2) {
                    return o1.getDepLevel() > o2.getDepLevel() ? 1 : 0;
                }
            });
            //得到该机构最大的深度 遍历每一级
            int startDepth = lfDeps.get(0).getDepLevel();
            int depDepth = lfDeps.get(lfDeps.size() - 1).getDepLevel();
            HashMap<Integer, List<LfDep>> map = new HashMap<Integer, List<LfDep>>(16);
            for (int i = startDepth; i <= depDepth; i++) {
                List<LfDep> deps = new ArrayList<LfDep>();
                for (LfDep lfDep : lfDeps) {
                    if (lfDep.getDepLevel() == i) {
                        deps.add(lfDep);
                    }
                }
                map.put(i, deps);
            }
            //组装第一级
            List<LfDep> topDeps = map.get(1);
            for(LfDep dep : topDeps){
                List<DepOrUserTreeDto> childrenList = new ArrayList<DepOrUserTreeDto>();
                DepOrUserTreeDto userTreeDto = new DepOrUserTreeDto();
                userTreeDto.setId("org_" + dep.getDepId().toString());
                userTreeDto.setLabel(dep.getDepName());
                if (userByDepIds != null) {
                    setSysUser(childrenList, 1L, userByDepIds);
                    userTreeDto.setChildren(childrenList);
                }
                depOrUserTreeDtos.add(userTreeDto);
            }

            //遍历map组装 按父子级组装list
            for (Map.Entry<Integer, List<LfDep>> entry : map.entrySet()) {
                //本级机构
                List<LfDep> currentDeps = entry.getValue();
                for (LfDep currentDep : currentDeps) {
                    //获取父机构Id
                    Long superiorId = currentDep.getSuperiorId();
                    if(superiorId == 0L){
                        continue;
                    }
                    List<DepOrUserTreeDto> children = new ArrayList<DepOrUserTreeDto>();
                    //根据父机构Id在树中找到children节点
                    DepOrUserTreeDto userTreeDto = new DepOrUserTreeDto();
                    userTreeDto.setId("org_" + currentDep.getDepId().toString());
                    userTreeDto.setLabel(currentDep.getDepName());
                    if (userByDepIds != null) {
                        List<DepOrUserTreeDto> newList = new ArrayList<DepOrUserTreeDto>();
                        setSysUser(newList, currentDep.getDepId(), userByDepIds);
                        userTreeDto.setChildren(newList);
                    }
                    children.add(userTreeDto);
                    findChildrenById("org_" + superiorId, depOrUserTreeDtos, children);
                }
            }
        }
        return depOrUserTreeDtos;
    }

    /**
     * 根据机构Id获取子机构(仅包含下级子机构)
     *
     * @param depId 机构Id
     * @param tree  操作员树或机构树
     * @return 结果集
     */
    private List<DepOrUserTreeDto> findChildrenById(String depId, List<DepOrUserTreeDto> tree) {
        List<DepOrUserTreeDto> result;
        if (null == tree) {
            return null;
        }
        for (DepOrUserTreeDto dto : tree) {
            if (!dto.getId().equals(depId)) {
                result = findChildrenById(depId, dto.getChildren());
                if (null != result && result.size() > 0) {
                    return result;
                }
            } else {
                if (null != dto.getChildren()) {
                    //遍历取出
                    List<DepOrUserTreeDto> resultList = new ArrayList<DepOrUserTreeDto>();
                    for (DepOrUserTreeDto treeDto : dto.getChildren()) {
                        DepOrUserTreeDto temp = new DepOrUserTreeDto();
                        temp.setId(treeDto.getId());
                        temp.setLabel(treeDto.getLabel());
                        resultList.add(temp);
                    }
                    return resultList;
                } else {
                    //说明没有自己机构直接跳出
                    break;
                }
            }
        }
        return null;
    }

    /**
     * //根据父机构Id在树中找到children节点
     *
     * @param depId             指定的id
     * @param depOrUserTreeDtos 已经生产的机构树
     * @param userTreeDto       需要在父节点增加的信息
     */
    private void findChildrenById(String depId, List<DepOrUserTreeDto> depOrUserTreeDtos, List<DepOrUserTreeDto> userTreeDto) {
        if (null == depOrUserTreeDtos) {
            return;
        }
        for (DepOrUserTreeDto dto : depOrUserTreeDtos) {
            if(dto.getId().startsWith("mem_")){
                continue;
            }
            if (!dto.getId().equals(depId)) {
                findChildrenById(depId, dto.getChildren(), userTreeDto);
            } else {
                if (null == dto.getChildren()) {
                    List<DepOrUserTreeDto> treeDtos = new ArrayList<DepOrUserTreeDto>(userTreeDto);
                    dto.setChildren(treeDtos);
                } else {
                    List<DepOrUserTreeDto> treeDtos = dto.getChildren();
                    treeDtos.addAll(userTreeDto);
                }
                userTreeDto.clear();
                return;
            }
        }
    }

    private List<LfDep> getDepByDomination(LinkedHashMap<String, String> conditionMap) {
        return reportDao.getDepByDomination(conditionMap);
    }

    /**
     * 获取跳转路径
     *
     * @param pathUrl 请求路径
     * @return 跳转路径
     */
    @Override
    public String getJumpModulePath(String pathUrl) {
        String module = "";
        //获取访问路径，判断跳转菜单
        String moduleName = pathUrl.substring(pathUrl.lastIndexOf("/") + 1, pathUrl.lastIndexOf(".htm"));
        //判断应该跳转哪一个页面
        for (JumpPathEnum pathEnum : JumpPathEnum.values()) {
            if (moduleName.contains(pathEnum.getUrl())) {
                module = pathEnum.getUrl();
                break;
            }
        }
        return module;
    }

    /**
     * 查询报表数据入口方法
     *
     * @param sysUser     操作员对象
     * @param queryEntity 查询实体类
     * @param module      模块名字
     * @return 返回包含报表信息的list集合的Json串
     */
    @Override
    public List<ReportVo> findMtDataRptByModuleName(LfSysuser sysUser, ReportVo queryEntity, String module, PageInfo page) {
        List<ReportVo> reportVos = new ArrayList<ReportVo>();
        try {
            //去掉操作员中的机构
            if(StringUtils.isNotEmpty(queryEntity.getUserIdStr())){
                queryEntity.setUserIdStr(queryEntity.getUserIdStr().replaceAll("\"org_\\d+\",?",""));
            }
            //兼容托管版加上企业编码
            if (StaticValue.getCORPTYPE() == 1 && !ReportMtDataRptSql.CORPCODE_10W.equals(sysUser.getCorpCode())) {
                queryEntity.setCorpCode(sysUser.getCorpCode());
            }
            //默认查询日报表
            if (null == queryEntity.getReportType()) {
                queryEntity.setReportType(2);
            }
            //默认查短信
            if (null == queryEntity.getMstype()) {
                queryEntity.setMstype(0);
            }
            //处理时间
            if (StringUtils.isEmpty(queryEntity.getQueryTime())) {
                setTimeCondition(queryEntity, queryEntity.getReportType());
            }
            //处理SP账号
            if (StringUtils.isEmpty(queryEntity.getSpUserId())) {
                getSpUsers(queryEntity, sysUser.getCorpCode());
            }
            // 处理机构统计报表默认orgId
            if (StringUtils.isEmpty(queryEntity.getOrgId()) && JumpPathEnum.sysDepReport.getUrl().equals(module)) {
                List<LfDep> deps = reportDao.getDominationByUserId(sysUser.getUserId());
                if (deps != null && deps.size() > 0) {
                    queryEntity.setOrgId(deps.get(0).getDepId().toString());
                }
            }
            //处理区域信息
            //doHandleAreaConditionOld(queryEntity);
            //处理区域查询条件,区域目前支持到省份,不支持到市级别
            doHandleAreaCondition(queryEntity);
            reportVos = reportDao.findMtDataRptByModuleName(sysUser, queryEntity, module, page);
            //处理结果集合
            handleMtDataRptList(reportVos, queryEntity, module);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "--> ReportServiceImpl.findMtDataRptByModuleName() 查询统计-->统计报表查询异常");
        }
        return reportVos;
    }

    /**
     * 处理区域查询条件信息,支持到省份级别
     * @param queryEntity    区域查询条件
     */
    private void doHandleAreaCondition(ReportVo queryEntity) {
        if (StringUtils.isNotEmpty(queryEntity.getProvinces())) {
            queryEntity.setProvince(queryEntity.getProvinces());
        }
    }

    /**
     * 处理区域查询条件信息,之前可支持处理到市级别
     * @param queryEntity    区域查询条件
     */
    private void doHandleAreaConditionOld(ReportVo queryEntity) {
        if (StringUtils.isNotEmpty(queryEntity.getProvinces())) {
            List<String> list = JSONObject.parseArray(queryEntity.getProvinces(), String.class);
            if (list.size() > 0) {
                queryEntity.setProvince(list.get(0));
                if (list.size() > 1) {
                    //选择了市
                    queryEntity.setCity(list.get(1));
                }
            }
        }
    }

    /**
     * 处理结果集
     *
     * @param reportVos   结果集
     * @param queryEntity 查询结果对象
     */
    private void handleMtDataRptList(List<ReportVo> reportVos, ReportVo queryEntity, String module) {
        if (null != reportVos && reportVos.size() > 0) {
            String showTime = handleShowTime(queryEntity);
            for (ReportVo reportVo : reportVos) {
                if (queryEntity.isDetail()) {
                    String iymd = reportVo.getIymd().toString();
                    if (queryEntity.getReportType() == ReportMtDataRptSql.YEAR_REPORT) {
                        showTime = iymd.substring(0, 4) + "年" + iymd.substring(4, 6) + "月";
                    } else {
                        showTime = iymd.substring(0, 4) + "年" + iymd.substring(4, 6) + "月" + iymd.substring(6) + "日";
                    }
                }
                //处理显示时间
                reportVo.setQueryTimeStr(showTime);
                //区域
                if (StringUtils.isBlank(queryEntity.getProvinces()) || StringUtils.isBlank(queryEntity.getCity()) || StringUtils.isBlank(reportVo.getCity())) {
                    reportVo.setCity("");
                }
                String city = StringUtils.isEmpty(reportVo.getCity()) || StringUtils.isBlank(queryEntity.getProvinces()) || "[]".equals(queryEntity.getProvinces()) ? "" : "/" + reportVo.getCity();
                reportVo.setProvinceAndCity(StringUtils.defaultIfEmpty(reportVo.getProvince(), "未知") + city);
                //选择省市
                reportVo.setProvinces(queryEntity.getProvinces());
                //发送成功数
                reportVo.setSendSucc(reportVo.getIcount() - reportVo.getRfail1());
                //短信类型
                reportVo.setMstype(queryEntity.getMstype());
                //运营商名字
                getOperatorName(reportVo, queryEntity.getSpisuncm());
                //发送类型
                getSendName(reportVo, queryEntity.getSendtype());
                //账号类型
                reportVo.setSpName(getSpName(reportVo.getSptype()));
                //更多维度机构ID
                reportVo.setOrgId(queryEntity.getOrgId());
                //更多维度操作员ID
                reportVo.setUserIdStr(queryEntity.getUserIdStr());
                //更多维度业务类型
                if (JumpPathEnum.busReport.getUrl().equals(module)) {
                    reportVo.setBusCode(reportVo.getSvrType());
                }else{
                    reportVo.setBusCode(queryEntity.getBusCode());
                }
                //更多维度sp账号
                if (JumpPathEnum.spMtReport.getUrl().equals(module)) {
                    //获取sp账号
                    reportVo.setSpUserId(reportVo.getUserId());
                }else{
                    reportVo.setSpUserId(queryEntity.getSpUserId());
                }
                //运营商账户Id
                reportVo.setSpIdName(getSpId(reportVo.getSpId()));
                //参数名字 参数值
                reportVo.setParamValue(StringUtils.defaultIfEmpty(reportVo.getParamValue(), "未知"));
                reportVo.setParamName(StringUtils.defaultIfEmpty(reportVo.getParamName(), "未知"));
                //报表类型
                reportVo.setReportType(queryEntity.getReportType());
                // 处理 机构/操作员组合名称
                getDepName(reportVo);
                //是否包含子机构
                reportVo.setContainSubDep(queryEntity.isContainSubDep());
            }
            //iymd排序
            Collections.sort(reportVos, new Comparator<ReportVo>() {
                @Override
                public int compare(ReportVo vo1, ReportVo vo2) {
                    if (vo1.getIymd() < vo2.getIymd()) {
                        return 1;
                    } else if (vo1.getIymd().equals(vo2.getIymd())) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            });
        }
    }

    private void getDepName(ReportVo reportVo) {
        if (null != reportVo && null != reportVo.getIdtype()) {
            switch (reportVo.getIdtype()) {
                case 1:
                    reportVo.setDepOrUserName("[机构]" + reportVo.getDepName());
                    break;
                case 2:
                    reportVo.setDepOrUserName("[操作员]" + reportVo.getDepName());
                    break;
                case 3:
                    reportVo.setDepOrUserName("[未知机构]" + reportVo.getDepName());
                    break;
                default:
                    reportVo.setDepOrUserName("[未知机构]" + reportVo.getDepName());
            }
        } else if (reportVo != null) {
            if (StringUtils.isEmpty(reportVo.getName())) {
                reportVo.setName("未知操作员");
            }
            if (StringUtils.isEmpty(reportVo.getDepName())) {
                reportVo.setDepName("未知机构");
            }
        }
    }

    private String handleShowTime(ReportVo queryEntity) {
        String showTime = "";
        //处理时间
        if (!queryEntity.isDetail()) {
            switch (queryEntity.getReportType()) {
                case ReportMtDataRptSql.DAY_REPORT:
                    List<String> list = JSONObject.parseArray(queryEntity.getQueryTime(), String.class);
                    String showStartTime = list.get(0).replaceFirst("-", "年").replaceFirst("-", "月") + "日";
                    String showEndTime = list.get(1).replaceFirst("-", "年").replaceFirst("-", "月") + "日";
                    showTime = showStartTime + "至" + showEndTime;
                    break;
                case ReportMtDataRptSql.MONTH_REPORT:
                    showTime = queryEntity.getQueryTime().replaceFirst("-", "年") + "月";
                    break;
                case ReportMtDataRptSql.YEAR_REPORT:
                    showTime = queryEntity.getQueryTime() + "年";
                    break;
                default:
                    return showTime;
            }
        }
        return showTime;
    }

    private void setTimeCondition(ReportVo queryEntity, Integer reportType) throws Exception {
        Integer month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        String monthStr = month.toString().length() == 2 ? month.toString() : "0" + month;
        switch (reportType) {
            case ReportMtDataRptSql.DAY_REPORT:
                Integer date = Calendar.getInstance().get(Calendar.DATE);
                String dateStr = date.toString().length() == 2 ? date.toString() : "0" + date;

                String startTime = Calendar.getInstance().get(Calendar.YEAR) + "-" + monthStr + "-01";
                String endTime = Calendar.getInstance().get(Calendar.YEAR) + "-" + monthStr + "-" + dateStr;
                queryEntity.setQueryTime("[\"" + startTime + "\",\"" + endTime + "\"]");
                break;
            case ReportMtDataRptSql.MONTH_REPORT:
                queryEntity.setQueryTime(Calendar.getInstance().get(Calendar.YEAR) + "-" + monthStr);
                break;
            case ReportMtDataRptSql.YEAR_REPORT:
                queryEntity.setQueryTime(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
                break;
            default:
                throw new EMPException("查询统计>统计报表处理查询时间异常，无法获取正确的报表查询类型");
        }
    }

    /**
     * 点击详情按钮时
     *
     * @param user          操作员对象
     * @param queryEntity   查询实体类
     * @param requestEntity 自定义Request对象
     * @return 返回包含报表信息的list Json串
     */
    @Override
    public List<ReportVo> findMtDataRptDetail(LfSysuser user, ReportVo queryEntity, Request requestEntity) {
        //处理时间
        String timeStr = queryEntity.getQueryTimeStr();
        switch (queryEntity.getReportType()) {
            case ReportMtDataRptSql.MONTH_REPORT:
                //月报表详情相当于查询指定年月的日报表
                timeStr = timeStr.replaceAll("年", "-").replace("月", "");
                break;
            case ReportMtDataRptSql.YEAR_REPORT:
                //年报表详情相当于查询指定年份的月报表
                timeStr = timeStr.replaceAll("年", "");
                break;
            case ReportMtDataRptSql.DAY_REPORT:
                //日报表详情只是group加入iymd分组条件即可
                timeStr = timeStr.replaceAll("[年月]", "-").replace("至", "\",\"").replaceAll("日", "");
                timeStr = "[\"" + timeStr + "\"]";
            default:
                // do nothing
        }
        queryEntity.setDetail(true);
        queryEntity.setQueryTime(timeStr);
        return findMtDataRptByModuleName(user, queryEntity, requestEntity.getModule(), requestEntity.getPage());
    }

    /**
     * 获取短彩信运营商账号
     *
     * @param type:0代表短信，1代表短信
     * @return list集合
     */
    private List<String> getListByMsType(Integer type) {
        List<String> spList = new ArrayList<String>();
        List<DynaBean> beans = new ReportDaoImpl().getListByMsType(type);
        for (DynaBean bean : beans) {
            spList.add(bean.get("spid").toString());
        }
        return spList;
    }

    private String getSpName(Integer spType) {
        if (spType != null) {
            if (spType == 1) {
                return "EMP应用";
            }
            return "EMP接入";
        }
        return "";
    }

    private String getSpId(String spId) {
        return StringUtils.isNotBlank(spId) ? spId.trim() : "未知";
    }

    private void getSpUsers(ReportVo queryEntity, String corpCode) throws Exception {
        //SP账号
        List<String> spUserList = queryBiz.getSpUserList(queryEntity.getMstype().toString(), corpCode, StaticValue.getCORPTYPE());
        StringBuilder spUsers = new StringBuilder();
        for (String s : spUserList) {
            spUsers.append("'").append(s).append("',");
        }

        if (spUsers.length() > 0) {
            queryEntity.setSpUsers(spUsers.substring(0, spUsers.length() - 1));
        }
    }

    private void getOperatorName(ReportVo reportVo, Integer spisuncm) {
        if (spisuncm == null && reportVo.getSpisuncm() == null) {
            reportVo.setSpisuncm(-1);
            reportVo.setOperatorName("全部");
        } else if (!isSpisuncm(reportVo.getSpisuncm(), spisuncm)) {
            reportVo.setOperatorName("未知");
        } else {
            Integer spisuncmVal = reportVo.getSpisuncm() != null ? reportVo.getSpisuncm() : spisuncm;
            //原来选择了运营商则展示选择的运营商名字
            for (OperatorEnum operatorEnum : OperatorEnum.values()) {
                if (null != spisuncmVal && spisuncmVal.equals(operatorEnum.getCode())) {
                    reportVo.setOperatorName(operatorEnum.getName());
                    reportVo.setSpisuncm(spisuncmVal);
                    return;
                }
            }
        }
    }

    private Boolean isSpisuncm(Integer spisuncm, Integer pageSpisuncm) {
        Integer spisuncmVal = spisuncm != null ? spisuncm : pageSpisuncm;
        return spisuncmVal == 0 || spisuncmVal == 1 || spisuncmVal == 21 || spisuncmVal == 5 || spisuncmVal == -1;
    }

    private void getSendName(ReportVo reportVo, Integer sendtype) {
        if (sendtype == 0 && reportVo.getSendtype() == null) {
            reportVo.setSendtype(0);
            reportVo.setSendName("全部");
        } else {
            Integer sendTypeVal = reportVo.getSendtype() != null ? reportVo.getSendtype() : sendtype;
            for (SendTypeEnum sendTypeEnum : SendTypeEnum.values()) {
                if (sendTypeVal.equals(sendTypeEnum.getCode())) {
                    reportVo.setSendName(sendTypeEnum.getName());
                    reportVo.setSendtype(sendTypeEnum.getCode());
                    return;
                }
            }
        }
    }

    /**
     * 将service返回的list集合加上总条数转换为json传给前端
     *
     * @param reportVos list结果集合
     * @param totalRec  总条数
     * @return json串
     */
    @Override
    public String handleReportVoList2Json(List<ReportVo> reportVos, int totalRec) {
        HashMap<String, Object> map = new HashMap<String, Object>(16);
        map.put("totalRec", totalRec);
        map.put("reportList", reportVos);
        return FastJsonUtils.collectToString(map);
    }

    /**
     * 根据机构Id获取机构树信息
     *
     * @param depId 机构Id
     * @param flag  是否获取操作员树 默认获取机构树
     * @return 结果Json串
     */
    @Override
    public String getDeptOrUserTree(String depId, boolean flag) {
        List<DepOrUserTreeDto> result;
        if (flag) {
            //操作员信息
            result = findChildrenById(depId, DepAndUserTreeInfoCache.USER_TREE);
        } else {
            //机构信息
            result = findChildrenById(depId, DepAndUserTreeInfoCache.DEP_TREE);
        }
        return FastJsonUtils.convertObjectToJSON(result);
    }

    /**
     * 懒加载方式获取机构树信息
     *
     * @param depId 机构Id
     * @param flag  否获取操作员树 默认获取机构树
     * @return 结果Json串
     */
    @Override
    public String getDeptOrUserTreeByLazy(String depId, boolean flag) {
        List<DepOrUserTreeDto> result = findChildrenByDepId(depId, flag);
        return FastJsonUtils.convertObjectToJSON(result);
    }

    private List<DepOrUserTreeDto> findChildrenByDepId(String depId, boolean flag) {
        List<DepOrUserTreeDto> depOrUserTreeDtos = new ArrayList<DepOrUserTreeDto>();
        List<LfDep> lfDeps = reportDao.getChildrenDepById(depId);
        for(LfDep dep : lfDeps){
            DepOrUserTreeDto treeDto = new DepOrUserTreeDto();
            treeDto.setId("org_" + dep.getDepId().toString());
            treeDto.setLabel(dep.getDepName());
            treeDto.setChildren(new ArrayList<DepOrUserTreeDto>());
            depOrUserTreeDtos.add(treeDto);
        }
        if(flag){
            setSysUser(depOrUserTreeDtos, Long.parseLong(depId));
        }
        return depOrUserTreeDtos;
    }

    private void handleSysUser(List<LfSysuser> lfSysusers, List<DepOrUserTreeDto> children){
        if (null != lfSysusers && lfSysusers.size() > 0) {
            for (LfSysuser sysuser : lfSysusers) {
                DepOrUserTreeDto dto = new DepOrUserTreeDto();
                dto.setId("mem_" + sysuser.getUserId());
                //判断是否为已注销
                String flag = sysuser.getUserState() == 2 ? "(已注销)" : "";
                dto.setLabel(sysuser.getName() + flag);
                children.add(dto);
            }
        }
    }

    private void setSysUser(List<DepOrUserTreeDto> children, Long depId) {
        List<LfSysuser> lfSysusers = reportDao.getSysUserById(depId);
        handleSysUser(lfSysusers, children);
    }

    private void setSysUser(List<DepOrUserTreeDto> children, Long superiorId, HashMap<Integer, List<LfSysuser>> userByDepIds) {
        //获取操作员
        List<LfSysuser> lfSysusers = userByDepIds.get(Integer.valueOf(superiorId.toString()));
        handleSysUser(lfSysusers, children);
    }
}
