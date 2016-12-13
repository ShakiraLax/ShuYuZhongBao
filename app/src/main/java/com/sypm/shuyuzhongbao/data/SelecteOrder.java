package com.sypm.shuyuzhongbao.data;

import java.util.List;

/**
 * Created by Administrator on 2016/12/10.
 */

public class SelecteOrder {

    /**
     * status : 1
     * msg : 获取成功
     * list : [{"name":"收货人","mobile":"联系方式","address":"订单地址","shipSn":"Y890798740644481"}]
     */

    private int status;
    private String msg;
    private List<ListBean> list;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * name : 收货人
         * mobile : 联系方式
         * address : 订单地址
         * shipSn : Y890798740644481
         */

        private String name;
        private String mobile;
        private String address;
        private String shipSn;
        private String paycode;
        private String amount;
        private String status;
        private String shipStatus;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPaycode() {
            return paycode;
        }

        public void setPaycode(String paycode) {
            this.paycode = paycode;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getShipSn() {
            return shipSn;
        }

        public void setShipSn(String shipSn) {
            this.shipSn = shipSn;
        }

        public String getShipStatus() {
            return shipStatus;
        }

        public void setShipStatus(String shipStatus) {
            this.shipStatus = shipStatus;
        }
    }
}
