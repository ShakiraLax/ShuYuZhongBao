package com.sypm.shuyuzhongbao.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/11/27.
 */

public class OrderCurrent implements Serializable {

    /**
     * status : 1
     * msg : 获取成功
     * list : [{"orderSn":"Y929869746465783","mobile":"13561255885","address":"山东省聊城市临清市","lat":36.7939682,"lng":115.9197464,"storeSn":"","storeLat":null,"storeLng":null,"storeName":null,"status":0,"endTime":"","countType":0,"payed":null,"amount":3.6,"unpayed":3.6,"payCode":"cash","note":"我叫备注","orderStatus":"订单状态","deliverId":100002,"goodsList":[{"goodsTitle":"商品名称","goodsSn":"商品编码","originalPrice":"商品原价格","goodsNumber":"商品数量","preferentialPrice":"商品优惠价格","isGift":1}]}]
     */

    public int status;
    public String msg;
    public List<ListBean> list;
    
    public static class ListBean {
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
         * note : 我叫备注
         * orderStatus : 订单状态
         * deliverId : 100002
         * goodsList : [{"goodsTitle":"商品名称","goodsSn":"商品编码","originalPrice":"商品原价格","goodsNumber":"商品数量","preferentialPrice":"商品优惠价格","isGift":1}]
         */

        public String orderSn;
        public String mobile;
        public String address;
        public Double lat;
        public Double lng;
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
        public String note;
        public String orderStatus;
        public int deliverId;
        public List<GoodsListBean> goodsList;
        
        public static class GoodsListBean {
            /**
             * goodsTitle : 商品名称
             * goodsSn : 商品编码
             * originalPrice : 商品原价格
             * goodsNumber : 商品数量
             * preferentialPrice : 商品优惠价格
             * isGift : 1
             */

            public String goodsTitle;
            public String goodsSn;
            public String originalPrice;
            public String goodsNumber;
            public String preferentialPrice;
            public int isGift;
        }
    }
}
