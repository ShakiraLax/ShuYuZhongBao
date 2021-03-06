package com.sypm.shuyuzhongbao.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/11/27.
 */

public class Order implements Serializable {

    /**
     * status : 1
     * msg : 获取成功
     * data : {"orderSn":"P708368376372284","mobile":"15169077170","address":"山东省济南市槐荫区济宁路3-商铺104号济宁路3-商铺104号","lat":36.66255951,"lng":116.90277863,"storeSn":"","storeLat":null,"storeLng":null,"storeName":null,"status":0,"endTime":"","countType":0,"payed":0,"amount":0,"unpayed":0,"payCode":"cash","note":"我叫备注","orderStatus":"订单状态","goodsList":[{"goodsTitle":"商品名称","goodsSn":"商品编码","originalPrice":"商品原价格","goodsNumber":"商品数量","preferentialPrice":"商品优惠价格","isGift":1}]}
     */

    private int status;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        private String orderSn;
        private String mobile;
        private String address;
        private String name;
        private double lat;
        private double lng;
        private String storeSn;
        private Object storeLat;
        private Object storeLng;
        private Object storeName;
        private int status;
        private String endTime;
        private int countType;
        private int payed;
        private int amount;
        private int unpayed;
        private String payCode;
        private String note;
        private String orderStatus;
        private List<GoodsListBean> goodsList;

        public String getOrderSn() {
            return orderSn;
        }

        public void setOrderSn(String orderSn) {
            this.orderSn = orderSn;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public String getStoreSn() {
            return storeSn;
        }

        public void setStoreSn(String storeSn) {
            this.storeSn = storeSn;
        }

        public Object getStoreLat() {
            return storeLat;
        }

        public void setStoreLat(Object storeLat) {
            this.storeLat = storeLat;
        }

        public Object getStoreLng() {
            return storeLng;
        }

        public void setStoreLng(Object storeLng) {
            this.storeLng = storeLng;
        }

        public Object getStoreName() {
            return storeName;
        }

        public void setStoreName(Object storeName) {
            this.storeName = storeName;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public int getCountType() {
            return countType;
        }

        public void setCountType(int countType) {
            this.countType = countType;
        }

        public int getPayed() {
            return payed;
        }

        public void setPayed(int payed) {
            this.payed = payed;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public int getUnpayed() {
            return unpayed;
        }

        public void setUnpayed(int unpayed) {
            this.unpayed = unpayed;
        }

        public String getPayCode() {
            return payCode;
        }

        public void setPayCode(String payCode) {
            this.payCode = payCode;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        public List<GoodsListBean> getGoodsList() {
            return goodsList;
        }

        public void setGoodsList(List<GoodsListBean> goodsList) {
            this.goodsList = goodsList;
        }

        public static class GoodsListBean {
            /**
             * goodsTitle : 商品名称
             * goodsSn : 商品编码
             * originalPrice : 商品原价格
             * goodsNumber : 商品数量
             * preferentialPrice : 商品优惠价格
             * isGift : 1
             */

            private String goodsTitle;
            private String goodsSn;
            private String originalPrice;
            private String goodsNumber;
            private String preferentialPrice;
            private int isGift;

            public String getGoodsTitle() {
                return goodsTitle;
            }

            public void setGoodsTitle(String goodsTitle) {
                this.goodsTitle = goodsTitle;
            }

            public String getGoodsSn() {
                return goodsSn;
            }

            public void setGoodsSn(String goodsSn) {
                this.goodsSn = goodsSn;
            }

            public String getOriginalPrice() {
                return originalPrice;
            }

            public void setOriginalPrice(String originalPrice) {
                this.originalPrice = originalPrice;
            }

            public String getGoodsNumber() {
                return goodsNumber;
            }

            public void setGoodsNumber(String goodsNumber) {
                this.goodsNumber = goodsNumber;
            }

            public String getPreferentialPrice() {
                return preferentialPrice;
            }

            public void setPreferentialPrice(String preferentialPrice) {
                this.preferentialPrice = preferentialPrice;
            }

            public int getIsGift() {
                return isGift;
            }

            public void setIsGift(int isGift) {
                this.isGift = isGift;
            }
        }
    }
}
