package com.montnets.emp.common.tools;

import com.montnets.emp.common.constant.StaticValue;

public class MessageUtils {
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public enum RmsMessage {

        SUCCESS("成功","Success","成功"),
        FAIL("失败","Fail","失敗"),

        PERMISSION_DENIED("没有权限","Permission denied","沒有權限"),

        SAVE_SUCCESS("保存成功", "Save success!", "保存成功"),
        UPDATE_SUCCESS("编辑成功", "Update success!", "編輯成功"),
        DELETE_SUCCESS("删除成功", "Delete success!", "删除成功"),
        SELECT_SUCCESS("查询成功", "Select success!", "查詢成功"),
        SELECT_FAIL("查询异常", "Select success!", "查詢異常"),

        PARAM_RECEIVE_EXCEPTION("参数接收异常","Parameter reception anomaly","參數接收異常"),

        SYSTEM_EXCEPTION("系统异常","System error","系統異常");






        private String zh_CN;
        private String zh_HK;
        private String zh_TW;

        RmsMessage(String zh_CN, String zh_HK, String zh_TW) {
            this.zh_CN = zh_CN;
            this.zh_HK = zh_HK;
            this.zh_TW = zh_TW;
        }

        public String getZh_TW() {
            return zh_TW;
        }

        private void setZh_TW(String zh_TW) {
            this.zh_TW = zh_TW;
        }

        public String getZh_CN() {
            return zh_CN;
        }

        private void setZh_CN(String zh_CN) {
            this.zh_CN = zh_CN;
        }

        public String getZh_HK() {
            return zh_HK;
        }

        private void setZh_HK(String zh_HK) {
            this.zh_HK = zh_HK;
        }

    }

    // 普通方法
    public static String getMsg(String type, MessageUtils.RmsMessage msg) {

        if (type.equals(StaticValue.ZH_CN)) {
            return msg.getZh_CN();
        } else if (type.equals(StaticValue.ZH_HK)) {
            return msg.getZh_HK();
        } else if (type.equals(StaticValue.ZH_TW)) {
            return msg.getZh_TW();
        } else {
            return msg.getZh_CN();
        }

    }


   /* public static void main(String[] args) {
        System.out.println(MessageUtils.getMsg(StaticValue.ZH_HK, RmsMessage.TMPLATE_DELETE));

    }*/
}
