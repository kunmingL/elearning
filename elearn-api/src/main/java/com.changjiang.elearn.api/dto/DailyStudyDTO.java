package com.changjiang.elearn.api.dto;

import lombok.Data;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 每日学习记录表
 */
@Data
public class DailyStudyDTO {
    /**
     * dailyID
     */
    @Size(max = 32)
    private String dailyId;
    /**
     * 计划ID
     */
    @Size(max = 32)
    private String planId;
    /**
     * 用户ID
     */
    @Size(max = 32)
    private String userId;
    /**
     * 学习天数
     */
    private Integer studyDay;
    /**
     * 当前单词索引
     */
    private Integer currentWordIdx;
    /**
     * 单词数量
     */
    private Integer wordCount;
    /**
     * 状态:0-未完成,1-已完成
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