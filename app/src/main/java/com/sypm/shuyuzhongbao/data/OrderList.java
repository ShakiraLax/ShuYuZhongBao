package com.sypm.shuyuzhongbao.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/11/27.
 */

public class OrderList implements Serializable {


    /**
     * status : 1
     * msg : 获取成功
     * list : [{"distance":4262,"storeSn":"001","storeName":"西门店"}]
     */

    public int status;
    public String msg;
    public List<ListBean> list;

    public static class ListBean implements Serializable {
        /**
         * distance : 4262
         * storeSn : 001
         * storeName : 西门店
         */

        public int distance;
        public String storeSn;
        public String storeName;


    }
}
