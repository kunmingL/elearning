package com.changjiang.elearn.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.math.BigDecimal;

/**
 * 学习计划表
 */
@Data
@TableName("t_study_plan")
public class StudyPlanPO {
    /**
     * 学习计划ID
     */
    @TableId("plan_id")
    private String planId;
    /**
     * 用户ID
     */
    @TableField("user_id")
    private String userId;
    /**
     * 每日单词数
     */
    @TableField("daily_words")
    private Integer dailyWords;
    /**
     * 总天数
     */
    @TableField("total_days")
    private Integer totalDays;
    /**
     * 总单词数
     */
    @TableField("total_words")
    private Integer totalWords;
    /**
     * 当前天数
     */
    @TableField("current_day")
    private Integer currentDay;
    /**
     * 状态:0-进行中,1-已完成
     */
    @TableField("status")
    private Integer status;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
} 