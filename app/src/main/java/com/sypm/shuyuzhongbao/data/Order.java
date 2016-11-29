package com.sypm.shuyuzhongbao.data;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/27.
 */

public class Order implements Serializable {

    /**
     * status : 1
     * msg : 获取成功
     * data : {"orderSn":"Y929869746465783","mobile":"13561255885","address":"山东省聊城市临清市","lat":36.7939682,"lng":115.9197464,"storeSn":"","storeLat":null,"storeLng":null,"storeName":null,"status":0,"endTime":"","countType":0,"payed":null,"amount":3.6,"unpayed":3.6,"payCode":"cash"}
     */

    public int status;
    public String msg;
    public DataBean list;

    public static class DataBean implements Serializable {
        /**
         * orderSn : Y929869746465783
         * mobile : 13561255885
         * address : 山东省聊城市临清市
         * lat : 36.7939682
         * lng : 115.9197464
         * storeSn :
         * storeLat : null
         * storeLng : null
         * storeName : null
         * status : 0
         * endTime :
         * countType : 0
         * payed : null
         * amount : 3.6
         * unpayed : 3.6
         * payCode : cash
         */

        public String orderSn;
        public String mobile;
        public String name;
        public String address;
        public String lat;
        public String lng;
        public String storeSn;
        public String storeLat;
        public String storeLng;
        public String storeName;
        public int status;
        public String endTime;
        public int countType;
        public String payed;
        public String amount;
        public String unpayed;
        public String payCode;

    }
}
