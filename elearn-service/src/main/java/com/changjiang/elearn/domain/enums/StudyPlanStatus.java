package com.changjiang.elearn.domain.enums;

public enum StudyPlanStatus {
    EFFECTIVE(0, "已生效"),
    UN_EFFECTIVE(1, "未生效"),
    COMPLETED(2, "已完成");


    private int code;
    private String desc;

    StudyPlanStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
} 