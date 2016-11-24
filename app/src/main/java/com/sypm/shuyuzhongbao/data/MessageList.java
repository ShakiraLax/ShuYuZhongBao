package com.sypm.shuyuzhongbao.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/11/24.
 */

public class MessageList implements Serializable {

    /**
     * status : 1
     * msg : 获取成功
     * list : [{"fromId":0,"content":"你好，这是系统发送给你的消息","createTime":"2016-11-18 23:17:21","shipSn":"Y607462020033843"}]
     */

    public int status;
    public String msg;
    public List<ListBean> list;

    public static class ListBean {
        /**
         * fromId : 0
         * content : 你好，这是系统发送给你的消息
         * createTime : 2016-11-18 23:17:21
         * shipSn : Y607462020033843
         */

        public int fromId;
        public String content;
        public String createTime;
        public String shipSn;

    }
}
