package com.changjiang.elearn.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户学习计划
 */
@Data
public class UserScheduleDto implements Serializable {
    /**
     * 用户id
     */
    private String userId;

    /**
     * 学习计划id
     */
    private String scheduleId;

    /**
     * 学习算法
     */
    private String studyModel;

    /**
     * 每日单词数
     */
    private Integer dailyWords;
    /**
     * 总单词数
     */
    private Integer countWords;
}
