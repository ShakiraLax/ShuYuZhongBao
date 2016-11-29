package com.sypm.shuyuzhongbao.data;

/**
 * Created by Administrator on 2016/11/24.
 */

public class UserInfo {


    public int status;
    public DataBean list;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DataBean getList() {
        return list;
    }

    public void setList(DataBean list) {
        this.list = list;
    }

    public static class DataBean {
        /**
         * sn : 100002
         * name : 李文杰
         * money : null
         * status : 0
         * mobile :
         * idNumber : null
         * idFront : /var/www/youhuo/common/config/../../attachments/
         * idBack : /var/www/youhuo/common/config/../../attachments/
         */

        public int sn;
        public String name;
        public String money;
        public int status;
        public String mobile;
        public String idNumber;
        public String idFront;
        public String idBack;

        public int getSn() {
            return sn;
        }

        public void setSn(int sn) {
            this.sn = sn;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getIdNumber() {
            return idNumber;
        }

        public void setIdNumber(String idNumber) {
            this.idNumber = idNumber;
        }

        public String getIdFront() {
            return idFront;
        }

        public void setIdFront(String idFront) {
            this.idFront = idFront;
        }

        public String getIdBack() {
            return idBack;
        }

        public void setIdBack(String idBack) {
            this.idBack = idBack;
        }
    }
}
