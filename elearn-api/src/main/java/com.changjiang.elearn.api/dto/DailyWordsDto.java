package com.changjiang.elearn.api.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class DailyWordsDto implements Serializable {
    /**
     * 用户id
     */
    private String userId;

    /**
     * 学习计划id
     */
    private String scheduleId;

    /**
     * 第几天
     */
    private String day;

    /**
     * 单词索引
     */
    private String wordIndex;
}
