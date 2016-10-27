package com.sypm.shuyuzhongbao.data;

import java.util.List;

/**
 * Created by Jack Wang on 2016/7/13.
 */
public class DataResult<T> {

    /**
     * status : 1
     * message : 登陆成功
     * data{ }
     */

    public String status;
    public String message;

    public List<T> list;
    public String[] array;

}
