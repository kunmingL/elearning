package com.changjiang.elearn.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 每日学习记录表
 */
@Data
@TableName("t_daily_study")
public class DailyStudyPO {
    /**
     * dailyID
     */
    @TableId("daily_id")
    private String dailyId;
    /**
     * 计划ID
     */
    @TableField("plan_id")
    private String planId;
    /**
     * 用户ID
     */
    @TableField("user_id")
    private String userId;
    /**
     * 学习天数
     */
    @TableField("study_day")
    private Integer studyDay;
    /**
     * 当前单词索引
     */
    @TableField("current_word_idx")
    private Integer currentWordIdx;
    /**
     * 单词数量
     */
    @TableField("word_count")
    private Integer wordCount;
    /**
     * 状态:0-未完成,1-已完成
     */
    @TableField("status")
    private Integer status;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;
} 