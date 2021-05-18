package com.montnets.emp.entity.sms;

import java.sql.Timestamp;

/**
 * @功能概要：草稿箱管理表实体类
 * @项目名称： emp_std_192.169.1.81
 * @初创作者： Administrator
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2016/1/19 13:39
 * <p>修改记录1：</p>
 * <pre>
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */
public class LfDrafts
{
    //短信内容
    private String mobileurl;

    //草稿类型，0－相同内容；1－不同内容动态模板；2－不同内容文件发送；3－客户群组群发；4-静态网讯发送；5－动态网讯发送
    private Integer draftstype;

    //短信内容
    private String msg;

    //用户ID
    private Long userid;

    //创建时间
    private Timestamp createtime;

    //自增ID
    private Long id;

    //修改时间
    private Timestamp updatetime;

    //企业编码
    private String corpcode;

    //标题
    private String title;

    public LfDrafts(){
    }

    public String getMobileurl(){

        return mobileurl;
    }

    public void setMobileurl(String mobileurl){

        this.mobileurl= mobileurl;

    }

    public Integer getDraftstype(){

        return draftstype;
    }

    public void setDraftstype(Integer draftstype){

        this.draftstype= draftstype;

    }

    public String getMsg(){

        return msg;
    }

    public void setMsg(String msg){

        this.msg= msg;

    }

    public Long getUserid(){

        return userid;
    }

    public void setUserid(Long userid){

        this.userid= userid;

    }

    public Timestamp getCreatetime(){

        return createtime;
    }

    public void setCreatetime(Timestamp createtime){

        this.createtime= createtime;

    }

    public Long getId(){

        return id;
    }

    public void setId(Long id){

        this.id= id;

    }

    public Timestamp getUpdatetime(){

        return updatetime;
    }

    public void setUpdatetime(Timestamp updatetime){

        this.updatetime= updatetime;

    }

    public String getCorpcode(){

        return corpcode;
    }

    public void setCorpcode(String corpcode){

        this.corpcode= corpcode;

    }

    public String getTitle(){

        return title;
    }

    public void setTitle(String title){

        this.title= title;

    }

}
