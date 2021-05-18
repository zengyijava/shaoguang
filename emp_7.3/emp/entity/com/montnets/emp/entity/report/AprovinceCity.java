/**
 *
 */
package com.montnets.emp.entity.report;

/**
 * @author
 * @project montnets_gateway
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-4-7 上午09：43
 * @description
 */

public class AprovinceCity implements java.io.Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -90942723904714533L;

    private Integer id;

    private String province;

    private String city;

    private Integer areaCode;

    private Integer provinceCode;

    public AprovinceCity() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(Integer areaCode) {
        this.areaCode = areaCode;
    }

    public Integer getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(Integer provinceCode) {
        this.provinceCode = provinceCode;
    }
}
