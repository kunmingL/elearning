package com.changjiang.elearn.domain.enums;

public enum StudyPlanStatus {
    IN_PROGRESS(0, "进行中"),
    COMPLETED(1, "已完成");

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