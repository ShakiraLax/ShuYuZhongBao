package com.sypm.shuyuzhongbao.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/11/27.
 */

public class OrderBySn implements Serializable {
    /**
     * status : 1
     * msg : 获取成功
     * data : {"orderSn":"Y900725157390240","mobile":"18615678345","address":"山东省济南市山大北路56号电商部","name":"刘伟","lat":36.67805481,"lng":117.06121063,"store":"359","shippingStoreSn":"","storeSn":"359","storeLat":"","storeLng":"","storeName":"","status":0,"endTime":"","countType":0,"payed":null,"amount":11.9,"unpayed":11.9,"payCode":"现金","note":"测试","orderStatus":"已确认","goodsList":[{"goodsSn":"110060","goodsTitle":"对乙酰氨基酚混悬滴剂","goodsNumber":1,"originalPrice":0,"preferentialPrice":11.9,"isGift":0}],"deliverId":113802}
     */

    public int status;
    public String msg;
    public DataBean list;

    public static class DataBean implements Serializable {
        /**
         * orderSn : Y900725157390240
         * mobile : 18615678345
         * address : 山东省济南市山大北路56号电商部
         * name : 刘伟
         * lat : 36.67805481
         * lng : 117.06121063
         * store : 359
         * shippingStoreSn :
         * storeSn : 359
         * storeLat :
         * storeLng :
         * storeName :
         * status : 0
         * endTime :
         * countType : 0
         * payed : null
         * amount : 11.9
         * unpayed : 11.9
         * payCode : 现金
         * note : 测试
         * orderStatus : 已确认
         * goodsList : [{"goodsSn":"110060","goodsTitle":"对乙酰氨基酚混悬滴剂","goodsNumber":1,"originalPrice":0,"preferentialPrice":11.9,"isGift":0}]
         * deliverId : 113802
         */

        public String orderSn;
        public String mobile;
        public String address;
        public String name;
        public String lat;
        public String lng;
        public String store;
        public String shippingStoreSn;
        public String storeSn;
        public String storeLat;
        public String storeLng;
        public String storeName;
        public int status;
        public String endTime;
        public int countType;
        public String payed;
        public double amount;
        public double unpayed;
        public String payCode;
        public String note;
        public String orderStatus;
        public int deliverId;
        public int isGet;
        public int distanceA;
        public int distanceB;
        public String createTime;
        public String storeAddress;
        public String storeTel;

        public List<GoodsListBean> goodsList;

        public static class GoodsListBean implements Serializable {
            /**
             * goodsSn : 110060
             * goodsTitle : 对乙酰氨基酚混悬滴剂
             * goodsNumber : 1
             * originalPrice : 0
             * preferentialPrice : 11.9
             * isGift : 0
             */

            public String goodsSn;
            public String goodsTitle;
            public int goodsNumber;
            public String originalPrice;
            public String preferentialPrice;
            public int isGift;

        }
    }
}
