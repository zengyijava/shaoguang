package com.montnets.emp.entity.wxsysuser;

import java.sql.Timestamp;
/**
 * @author linzhihan
 */
public class LfSysUser implements java.io.Serializable
{

    private static final long serialVersionUID = -4923255341568942848L;

    // 操作员userid
    private Long              userId;

    // 机构id
    private Long              depId;

    // 操作员id
    private String            userName;

    // 操作员名称
    private String            name;

    // 性别
    private Integer           sex;

    // 手机号码
    private String            mobile;

    // 密码
    private String            password;

    // 操作员状态（0为禁用，1为启用，2为注销）
    private Integer           userState;

    // 注册时间
    private Timestamp         regTime;

    // 开户人
    private String            holder;

    // 描述
    private String            comments;

    // 企业编码
    private String            corpCode;

    // 权限类型：1系统管理员， 2：客服管理员，3：客服 人员 
    private Integer           permissionType;

    // 用户编码
    private String            userCode;
    // 公众号ID
    private Long AId ;
    
    
    public Long getAId()
    {
        return AId;
    }

    public void setAId(Long AId)
    {
        this.AId = AId;
    }

    public LfSysUser()
    {
        regTime = new Timestamp(System.currentTimeMillis());
        permissionType = new Integer(1);
    }

    // 操作员userid
    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    // 操作员id
    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    // 操作员名称
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    // 性别
    public Integer getSex()
    {
        return sex;
    }

    public void setSex(Integer sex)
    {
        this.sex = sex;
    }

    // 手机号码
    public String getMobile()
    {
        if(mobile == null)
        {
            return "";
        }
        return mobile;
    }

    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }

    // 密码
    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    // 操作员状态（0为禁用，1为启用，2为注销）
    public Integer getUserState()
    {
        return userState;
    }

    public void setUserState(Integer userState)
    {
        this.userState = userState;
    }

    // 注册时间
    public Timestamp getRegTime()
    {
        return regTime;
    }

    public void setRegTime(Timestamp regTime)
    {
        this.regTime = regTime;
    }

    // 开户人
    public String getHolder()
    {
        return holder;
    }

    public void setHolder(String holder)
    {
        this.holder = holder;
    }

    // 描述
    public String getComments()
    {
        return comments;
    }

    public void setComments(String comments)
    {
        this.comments = comments;
    }

    // 机构id
    public Long getDepId()
    {
        return depId;
    }

    public void setDepId(Long depId)
    {
        this.depId = depId;
    }

    // 企业编码
    public String getCorpCode()
    {
        return corpCode;
    }

    public void setCorpCode(String corpCode)
    {
        this.corpCode = corpCode;
    }

    // 权限类型 1：个人权限 2：机构权限
    public Integer getPermissionType()
    {
        return permissionType;
    }

    public void setPermissionType(Integer permissionType)
    {
        this.permissionType = permissionType;
    }

    // 用户编码
    public String getUserCode()
    {
        return userCode;
    }

    public void setUserCode(String userCode)
    {
        this.userCode = userCode;
    }

}
