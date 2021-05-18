package com.montnets.emp.rms.tools;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * 将模板转化为Json的DTO对象
 * @date 2018-8-8 10:51:38
 * @author Cheng
 */
public class Excel2JsonDto implements Serializable{
    private static final long serialVersionUID = -179220516214767642L;
    /**
     * 帧序号  从0开始
     */
    private String fmno;
    /**
     * 该帧是否含有参数  0没有，1有  默认为1
     */
    private String ispara = "1";
    /**
     * 参数类型 0图文  1报表
     */
    private String type;
    /**
     * 是否有文本
     */
    private transient Boolean hasTxt = false;
    /**
     * 是否有图片
     */
    private transient Boolean hasImg = false;
    /**
     * 是否有报表
     */
    private transient Boolean hasReport = false;
    /**
     * 文本参数个数
     */
    private transient Integer txtParamCount;
    /**
     * 文本参数  p1=XXX p2=XXX
     */
    private HashMap<String,String> txt;
    /**
     * 报表标题参数  p1=XXX p2=XXX
     */
    private HashMap<String,String> title;
    /**
     * 报表行标题个数
     */
    private transient Integer rscnt;
    /**
     * 报表列标题个数
     */
    private transient Integer cscnt;
    /**
     * 组装json格式(img)
     */
    private LinkedHashMap<String,Object> img;
    /**
     * 图片参数个数
     */
    private transient Integer pcts;
    /**
     * 报表参数类型  2-数值动态，3-全值动态(带有行标题，列标题)
     */
    private transient Integer dynamicType;
    /**
     * 报表类型  3：饼图，4：柱状图，5：折线图，6：工资条，7：表格
     */
    private transient Integer chartType;
    /**
     * 是否有报表标题
     */
    private transient boolean hasReportTitle;
    /**
     * 需要在不同内容发送预览时替换参数的路径
     */
    private transient String paramFileName;
    /**
     * 图表+文参时 图表的顺序 在前为true
     */
    private transient boolean chartBefore;
    /**
     * 图参+文参时 图参的顺序 在前为true
     */
    private transient boolean imgBefore;

    public Boolean getHasTxt() {
        return hasTxt;
    }

    public void setHasTxt(Boolean hasTxt) {
        this.hasTxt = hasTxt;
    }

    public Boolean getHasImg() {
        return hasImg;
    }

    public void setHasImg(Boolean hasImg) {
        this.hasImg = hasImg;
    }

    public Boolean getHasReport() {
        return hasReport;
    }

    public void setHasReport(Boolean hasReport) {
        this.hasReport = hasReport;
    }

    public Integer getTxtParamCount() {
        return txtParamCount;
    }

    public void setTxtParamCount(Integer txtParamCount) {
        this.txtParamCount = txtParamCount;
    }

    public HashMap<String, String> getTxt() {
        return txt;
    }

    public void setTxt(HashMap<String, String> txt) {
        this.txt = txt;
    }

    public HashMap<String, String> getTitle() {
        return title;
    }

    public void setTitle(HashMap<String, String> title) {
        this.title = title;
    }

    public Integer getRscnt() {
        return rscnt;
    }

    public void setRscnt(Integer rscnt) {
        this.rscnt = rscnt;
    }

    public Integer getCscnt() {
        return cscnt;
    }

    public void setCscnt(Integer cscnt) {
        this.cscnt = cscnt;
    }

    public HashMap<String, Object> getImg() {
        return img;
    }

    public void setImg(LinkedHashMap<String, Object> img) {
        this.img = img;
    }

    public Integer getPcts() {
        return pcts;
    }

    public void setPcts(Integer pcts) {
        this.pcts = pcts;
    }

    public Integer getDynamicType() {
        return dynamicType;
    }

    public void setDynamicType(Integer dynamicType) {
        this.dynamicType = dynamicType;
    }

    public Integer getChartType() {
        return chartType;
    }

    public void setChartType(Integer chartType) {
        this.chartType = chartType;
    }

    public boolean getHasReportTitle() {
        return hasReportTitle;
    }

    public void setHasReportTitle(boolean hasReportTitle) {
        this.hasReportTitle = hasReportTitle;
    }

    public String getFmno() {
        return fmno;
    }

    public void setFmno(String fmno) {
        this.fmno = fmno;
    }

    public String getIspara() {
        return ispara;
    }

    public void setIspara(String ispara) {
        this.ispara = ispara;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isHasReportTitle() {
        return hasReportTitle;
    }

    public String getParamFileName() {
        return paramFileName;
    }

    public void setParamFileName(String paramFileName) {
        this.paramFileName = paramFileName;
    }

    public boolean getChartBefore() {
        return chartBefore;
    }

    public void setChartBefore(boolean chartBefore) {
        this.chartBefore = chartBefore;
    }

    public boolean getImgBefore() {
        return imgBefore;
    }

    public void setImgBefore(boolean imgBefore) {
        this.imgBefore = imgBefore;
    }
}
