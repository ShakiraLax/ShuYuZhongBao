package com.sypm.shuyuzhongbao.data;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/21.
 */

public class UserInfo implements Serializable {

    /**
     * status : 1
     * data : {"sn":100002,"name":"李文杰","money":null,"status":0,"mobile":"","idNumber":null,"idFront":"/var/www/youhuo/common/config/../../attachments/","idBack":"/var/www/youhuo/common/config/../../attachments/"}
     */

    public int status;
    public DataBean list;


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

    }

}
