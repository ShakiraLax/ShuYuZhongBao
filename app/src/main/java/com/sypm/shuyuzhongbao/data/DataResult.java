package com.sypm.shuyuzhongbao.data;

import java.util.List;


public class DataResult<T> {

    /**
     * status : 1
     * message : 登陆成功
     * data{ }
     */

    public String status;
    public String message;
    public String msg;
    public List<T> list;
    public String[] array;

}
