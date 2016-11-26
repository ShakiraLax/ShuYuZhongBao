package com.sypm.shuyuzhongbao.data;

import java.io.Serializable;

/**
 * Created by ygdlp on 2016/11/25 0025.
 */

public class OrderDetail implements Serializable {

    /**
     * status : 1
     * msg : 获取成功
     * data : {"orderSn":"P708368376372284","mobile":"15169077170","address":"山东省济南市槐荫区济宁路3-商铺104号济宁路3-商铺104号","lat":36.66255951,"lng":116.90277863,"storeSn":"","storeLat":null,"storeLng":null,"storeName":null,"status":0,"endTime":"","countType":0,"payed":0,"amount":0,"unpayed":0,"payCode":"cash"}
     */

    public int status;
    public String msg;
    public DataBean data;

    public static class DataBean implements Serializable{
        /**
         * orderSn : P708368376372284
         * mobile : 15169077170
         * address : 山东省济南市槐荫区济宁路3-商铺104号济宁路3-商铺104号
         * lat : 36.66255951
         * lng : 116.90277863
         * storeSn : 
         * storeLat : null
         * storeLng : null
         * storeName : null
         * status : 0
         * endTime : 
         * countType : 0
         * payed : 0
         * amount : 0
         * unpayed : 0
         * payCode : cash
         */

        public String orderSn;
        public String mobile;
        public String address;
        public double lat;
        public double lng;
        public String storeSn;
        public double storeLat;
        public double storeLng;
        public String storeName;
        public int status;
        public String endTime;
        public int countType;
        public int payed;
        public int amount;
        public int unpayed;
        public String payCode;

    }
}
