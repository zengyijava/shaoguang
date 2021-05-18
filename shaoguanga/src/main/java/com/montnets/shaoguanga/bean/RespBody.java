package com.montnets.shaoguanga.bean;

/**
 * @Author WJH
 * @Description
 * @date 2021/4/7 14:56
 * @Email ibytecode2020@gmail.com
 */
public class RespBody {
    private String rspcod;
    private String mgsGroup;
    private boolean success;
    private Integer count;
    private Object content;

    public String getRspcod() {
        return rspcod;
    }

    public void setRspcod(String rspcod) {
        this.rspcod = rspcod;
    }

    public String getMgsGroup() {
        return mgsGroup;
    }

    public void setMgsGroup(String mgsGroup) {
        this.mgsGroup = mgsGroup;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "RespBody{" +
                "rspcod='" + rspcod + '\'' +
                ", mgsGroup='" + mgsGroup + '\'' +
                ", success=" + success +
                ", count=" + count +
                ", content=" + content +
                '}';
    }
}
