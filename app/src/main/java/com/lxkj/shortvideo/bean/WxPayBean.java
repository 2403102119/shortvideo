package com.lxkj.shortvideo.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kxn on 2020/3/25 0025.
 */
public class WxPayBean extends BaseBean {

    /**
     * body : {"package":"Sign=WXPay","appid":"wx577de0927e5e1778","sign":"198A80551DD18C69DCBF74F7B501201C","pre_pay_order_status":"success","partnerid":"1581226741","prepayid":"wx251154096924225301a12c7f1079195500","noncestr":"5jngT6QHJlTAdQbh","timestamp":"1585108480"}
     */

    private BodyBean body;

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public static class BodyBean {
        /**
         * package : Sign=WXPay
         * appid : wx577de0927e5e1778
         * sign : 198A80551DD18C69DCBF74F7B501201C
         * pre_pay_order_status : success
         * partnerid : 1581226741
         * prepayid : wx251154096924225301a12c7f1079195500
         * noncestr : 5jngT6QHJlTAdQbh
         * timestamp : 1585108480
         */

        @SerializedName("package")
        private String packageX;
        private String appid;
        private String sign;
        private String pre_pay_order_status;
        private String partnerid;
        private String prepayid;
        private String noncestr;
        private String timestamp;

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getPre_pay_order_status() {
            return pre_pay_order_status;
        }

        public void setPre_pay_order_status(String pre_pay_order_status) {
            this.pre_pay_order_status = pre_pay_order_status;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }
}
