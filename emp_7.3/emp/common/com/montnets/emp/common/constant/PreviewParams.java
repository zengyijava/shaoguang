package com.montnets.emp.common.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

public class PreviewParams {
    // 有效号码数
    private int effCount = 0;
    // 提交总数
    private int subCount = 0;
    // 不符合条件数
    private int badCount = 0;
    // 格式错误数
    private int badModeCount = 0;
    // 重复数
    private int repeatCount = 0;
    // 黑名单数据
    private int blackCount = 0;
    // 包含关键字的提交号码数
    private int kwCount = 0;
    // 预览内容
    private String previewPhone = "";
    // 号码是否可见，0：不可见，1：可见
    private int ishidephone = 0;
    // 有效号码集合
    private HashSet<Long> validPhone = new HashSet<Long>();
    // 有效号码集合(支持国际号码,使用String类型)
    private HashSet<String> validPhoneStr = new HashSet<String>();
    // 号码文件url
    private String[] phoneFilePath = null;
    // 待删除的临时文件路径集合
    private List<String> delFilePath = new ArrayList<String>();
    // 动态模板参数个数
    private int tempParamCount = 0;
    // 是否验证重复
    private boolean isCheckRepeat = false;
    // 发送类型。1-相同内容，2动态模板，3不同内容
    private int sendType = 1;
    // 运营商有效号。0:移动号码 ;1:联通号码;2:电信号码;3:国际号码
    private int[] oprValidPhone = new int[]{0, 0, 0, 0};
    // 上传文件名
    private String fileName;
    // 无效文件名与原因
    HashMap<String, String> invalidFileName = new HashMap<String, String>();

    public String getJsonStr(Map<String, Object> objMap) {
        JSONObject resultJson = new JSONObject();
        resultJson.put("previewPhone", this.previewPhone);
        resultJson.put("subCount", this.subCount);
        resultJson.put("effCount", this.effCount);
        resultJson.put("badModeCount", this.badModeCount);
        resultJson.put("repeatCount", this.repeatCount);
        resultJson.put("blackCount", this.blackCount);
        // 1：有效号码文件相对路径
        resultJson.put("validFilePath", this.phoneFilePath[1]);
        // 5：无效号码相对路径
        resultJson.put("badFilePath", phoneFilePath[5]);
        // 4：预览文件相对路径
        resultJson.put("viewFilePath", phoneFilePath[4]);
        resultJson.put("kwCount", this.kwCount);
        resultJson.put("invalidFileName", this.invalidFileName);

        // 将传入的map合入到resultJson
        if (objMap != null && objMap.size() > 0) {
            resultJson.putAll(objMap);
        }
        return resultJson.toString();
    }

    public String getJsonStrBigfile(Map<String, Object> objMap, boolean isbigfile) {
        JSONObject resultJson = new JSONObject();
        resultJson.put("previewPhone", this.previewPhone);
        resultJson.put("subCount", this.subCount);
        resultJson.put("effCount", this.effCount);
        resultJson.put("badModeCount", this.badModeCount);
        resultJson.put("repeatCount", this.repeatCount);
        resultJson.put("blackCount", this.blackCount);
        // 1：有效号码文件相对路径
        resultJson.put("validFilePath", this.phoneFilePath[1]);
        // 5：无效号码相对路径
        resultJson.put("badFilePath", phoneFilePath[5]);
        // 大文件发送不能覆盖该值
        if (!isbigfile) {
            // 4：预览文件相对路径
            resultJson.put("viewFilePath", phoneFilePath[4]);
        }
        resultJson.put("kwCount", this.kwCount);
        resultJson.put("invalidFileName", this.invalidFileName);

        // 将传入的map合入到resultJson
        if (objMap != null && objMap.size() > 0) {
            resultJson.putAll(objMap);
        }
        return resultJson.toString();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getEffCount() {
        return effCount;
    }

    public void setEffCount(int effCount) {
        this.effCount = effCount;
    }

    public int getSubCount() {
        return subCount;
    }

    public void setSubCount(int subCount) {
        this.subCount = subCount;
    }

    public int getBadCount() {
        return badCount;
    }

    public void setBadCount(int badCount) {
        this.badCount = badCount;
    }

    public int getBadModeCount() {
        return badModeCount;
    }

    public void setBadModeCount(int badModeCount) {
        this.badModeCount = badModeCount;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }

    public int getBlackCount() {
        return blackCount;
    }

    public void setBlackCount(int blackCount) {
        this.blackCount = blackCount;
    }

    public String getPreviewPhone() {
        return previewPhone;
    }

    public void setPreviewPhone(String previewPhone) {
        this.previewPhone = previewPhone;
    }

    public int getIshidephone() {
        return ishidephone;
    }

    public void setIshidephone(int ishidephone) {
        this.ishidephone = ishidephone;
    }

    public HashSet<Long> getValidPhone() {
        return validPhone;
    }

    public void setValidPhone(HashSet<Long> validPhone) {
        this.validPhone = validPhone;
    }

    public String[] getPhoneFilePath() {
        return phoneFilePath;
    }

    public void setPhoneFilePath(String[] phoneFilePath) {
        this.phoneFilePath = phoneFilePath;
    }

    public List<String> getDelFilePath() {
        return delFilePath;
    }

    public void setDelFilePath(List<String> delFilePath) {
        this.delFilePath = delFilePath;
    }

    public int getTempParamCount() {
        return tempParamCount;
    }

    public void setTempParamCount(int tempParamCount) {
        this.tempParamCount = tempParamCount;
    }

    public boolean isCheckRepeat() {
        return isCheckRepeat;
    }

    public void setCheckRepeat(boolean isCheckRepeat) {
        this.isCheckRepeat = isCheckRepeat;
    }

    public int getSendType() {
        return sendType;
    }

    public void setSendType(int sendType) {
        this.sendType = sendType;
    }

    public int getKwCount() {
        return kwCount;
    }

    public void setKwCount(int kwCount) {
        this.kwCount = kwCount;
    }

    public int[] getOprValidPhone() {
        return oprValidPhone;
    }

    public void setOprValidPhone(int[] oprValidPhone) {
        this.oprValidPhone = oprValidPhone;
    }

    public HashSet<String> getValidPhoneStr() {
        return validPhoneStr;
    }

    public void setValidPhoneStr(HashSet<String> validPhoneStr) {
        this.validPhoneStr = validPhoneStr;
    }

    public HashMap<String, String> getInvalidFileName() {
        return invalidFileName;
    }

    public void setInvalidFileName(HashMap<String, String> invalidFileName) {
        this.invalidFileName = invalidFileName;
    }

}
