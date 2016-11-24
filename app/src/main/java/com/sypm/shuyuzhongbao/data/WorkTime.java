package com.sypm.shuyuzhongbao.data;

/**
 * Created by Administrator on 2016/11/22.
 */

public class WorkTime {

    /**
     * id : 1
     * userId : 100002
     * startTime : 7:00
     * endTime : 17:00
     * dayTime : 一二三
     * createTime : 2016-11-10 15:00:26
     * isOpen : 1
     */

    private int id;
    private int userId;
    private String startTime;
    private String endTime;
    private String dayTime;
    private String createTime;
    private int isOpen;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDayTime() {
        return dayTime;
    }

    public void setDayTime(String dayTime) {
        this.dayTime = dayTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(int isOpen) {
        this.isOpen = isOpen;
    }
}
