package com.montnets.emp.common.vo;

public class GrpupInfoDto {
    //共享类型
    private String shareType;
    //群组人数
    private String groupCount;
    //群组类型
    private Integer groupType;
    //群组名字
    private String groupName;
    //群组标记(用于前端判断)
    private Integer isDep;
    //群组Id
    private Long groupId;
    //每位成员的唯一标识
    private Long udgId;
    //手机号
    private String mobile;
    //用户名字
    private String userName;
    //签约用户属性名字
    private String clientFieldName;
    //签约用户属性Id
    private Long clientFieldId;
    //签约用户属性值
    private String clientFieldRef;
    //签约用户属性人数
    private Integer clientFieldCount;

    public String getClientFieldName() {
        return clientFieldName;
    }

    public void setClientFieldName(String clientFieldName) {
        this.clientFieldName = clientFieldName;
    }

    public Long getClientFieldId() {
        return clientFieldId;
    }

    public void setClientFieldId(Long clientFieldId) {
        this.clientFieldId = clientFieldId;
    }

    public String getClientFieldRef() {
        return clientFieldRef;
    }

    public void setClientFieldRef(String clientFieldRef) {
        this.clientFieldRef = clientFieldRef;
    }

    public Integer getClientFieldCount() {
        return clientFieldCount;
    }

    public void setClientFieldCount(Integer clientFieldCount) {
        this.clientFieldCount = clientFieldCount;
    }

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public String getGroupCount() {
        return groupCount;
    }

    public void setGroupCount(String groupCount) {
        this.groupCount = groupCount;
    }

    public Integer getGroupType() {
        return groupType;
    }

    public void setGroupType(Integer groupType) {
        this.groupType = groupType;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getIsDep() {
        return isDep;
    }

    public void setIsDep(Integer isDep) {
        this.isDep = isDep;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getUdgId() {
        return udgId;
    }

    public void setUdgId(Long udgId) {
        this.udgId = udgId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
