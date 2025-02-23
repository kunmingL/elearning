package com.changjiang.elearn.domain.enums;

public enum DailyStudyStatus {
    UN_STARTED(0, "未开始"),
    STARTED(1, "进行中"),
    COMPLETED(2, "已完成");

    private int code;
    private String desc;
    public int getCode() {
        return code;
    }
    public String getDesc() {
        return desc;
    }
    DailyStudyStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
