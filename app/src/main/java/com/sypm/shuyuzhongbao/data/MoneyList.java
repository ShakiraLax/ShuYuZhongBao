package com.sypm.shuyuzhongbao.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/11/24.
 */

public class MoneyList implements Serializable {

    /**
     * total : 25
     * todaytotal : 0
     * status : 1
     * msg : 获取成功
     * list : [{"fee":5,"status":"下单","createTime":"2016-11-10 17:15:57","shipSn":"Y162254103644237"}]
     */

    public String total;
    public String todaytotal;
    public String status;
    public String msg;
    public List<ListBean> list;


    public static class ListBean implements Serializable {
        /**
         * fee : 5
         * status : 下单
         * createTime : 2016-11-10 17:15:57
         * shipSn : Y162254103644237
         */

        public String fee;
        public String status;
        public String createTime;
        public String shipSn;

    }
}
