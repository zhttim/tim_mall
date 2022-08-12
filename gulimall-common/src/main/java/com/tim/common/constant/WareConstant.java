package com.tim.common.constant;

/**
 * @author tim
 * @date 2022/8/12 14:41
 **/
public class WareConstant {
    public enum PurchaseStatusEnum {
        /**
         * CREATED(0, "新建")
         * ASSIGNED(1, "已分配"),
         * RECEIVE(2, "已领取")
         * FINISH(3, "已完成"),
         * HASERROR(4, "有异常");
         */
        CREATED(0, "新建"), ASSIGNED(1, "已分配"),
        RECEIVE(2, "已领取"), FINISH(3, "已完成"),
        HASERROR(4, "有异常");
        private int code;
        private String msg;

        PurchaseStatusEnum(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    public enum PurchaseDetailStatusEnum {
        /**
         * CREATED(0, "新建")
         * ASSIGNED(1, "已分配"),
         * BUYING(2, "正在采购")
         * FINISH(3, "已完成"),
         * HASERROR(4, "采购失败");
         */
        CREATED(0, "新建"), ASSIGNED(1, "已分配"),
        BUYING(2, "正在采购"), FINISH(3, "已完成"),
        HASERROR(4, "采购失败");
        private int code;
        private String msg;

        PurchaseDetailStatusEnum(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
