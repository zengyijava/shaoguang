package com.montnets.emp.rms.meditor.tools;

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

        SYSTEM_EXCEPTION("系统异常","System error","系統異常"),

        TEMPLATE_SAVE_SUCCESS("保存成功", "Save success!", "保存成功"),
        TEMPLATE_DELETE_SUCCESS("删除成功", "Delete success!", "刪除成攻"),
        TEMPLATE_UPDATE_SUCCESS("编辑成功", "Update success!", "編輯成功"),
        TEMPLATE_SAVE_FAIL("保存失败", "Save fail!", "保存失敗"),
        TEMPLATE_DELETE_FAIL("删除失败", "Delete fail!", "刪除失敗"),
        TEMPLATE_UPDATE_FAIL("编辑失败", "Update fail!", "編輯失敗"),


        TEMPLATE_NOT_FIND("模板不存在","Template not find","模板不存在"),
        TEMPLATE_NAME_CANNOT_BE_BLANK("模板名称不能为空","Template cannot be blank","模板名稱不能爲空"),
        TEMPLATE_NOT_APPROVE("模板未审批","Template not approved","模板未審批"),
        TEMPLATE_START_USEING("已启用，发送时可选择该模板（需审核通过）","Enabled, this template can be selected when sent (approval is required)","已啓用，發送時可選擇該模板（需審核通過）"),
        TEMPLATE_FORBIDDEN("已禁用，发送时不可选择该模板","Disabled, this template cannot be selected when sent","已禁用，發送時不可選擇該模板"),
        SUBMIT_FAIL("提交审核平台失败！","Submit RSC Fail","提交審核平臺失敗！"),
        SUBMIT_SUCCESS("提交审核平台成功","Submit RSC Success","提交審核平臺成功");




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
