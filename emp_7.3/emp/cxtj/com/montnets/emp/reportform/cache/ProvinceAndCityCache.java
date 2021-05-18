package com.montnets.emp.reportform.cache;

import com.montnets.emp.reportform.dto.AprovinceCityDto;

import java.util.List;

/**
 * 省份和地区缓存类
 * @author lianghuageng
 * @date 2018/12/11 16:43
 */
public enum ProvinceAndCityCache {
    /**
     * 枚举的单例
     */
    INSTANCE;

    private static List<AprovinceCityDto> provinceAndCity;


    public List<AprovinceCityDto> getProvinceAndCity() {
        return provinceAndCity;
    }

    public void setProvinceAndCity(List<AprovinceCityDto> provinceAndCity) {
        ProvinceAndCityCache.provinceAndCity = provinceAndCity;
    }
}
