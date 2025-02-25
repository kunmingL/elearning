package com.changjiang.elearn.api.dto;

import lombok.Data;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 学习计划表
 */
@Data
public class StudyPlanDTO {
    /**
     * 学习计划ID
     */
    @Size(max = 32)
    private String planId;
    /**
     * 用户ID
     */
    @Size(max = 32)
    private String userId;
    /**
     * 每日单词数
     */
    private Integer dailyWords;
    /**
     * 总天数
     */
    private Integer totalDays;
    /**
     * 总单词数
     */
    private Integer totalWords;
    /**
     * 当前天数
     */
    private Integer currentDay;
    /**
     * 状态:0-进行中,1-已完成
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
} 