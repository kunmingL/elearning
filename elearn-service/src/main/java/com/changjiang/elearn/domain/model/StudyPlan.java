package com.changjiang.elearn.domain.model;

import com.changjiang.elearn.domain.enums.StudyPlanStatus;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.math.BigDecimal;

/**
 * 学习计划表
 */
@Getter
@NoArgsConstructor
public class StudyPlan {
    /**
     * 学习计划ID
     */
    private String planId;
    /**
     * 用户ID
     */
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
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    @Builder
    public StudyPlan(String planId, String userId, Integer dailyWords, Integer totalDays, Integer totalWords, Integer currentDay, Integer status, LocalDateTime createTime, LocalDateTime updateTime) {
        this.planId = planId;
        this.userId = userId;
        this.dailyWords = dailyWords;
        this.totalDays = totalDays;
        this.totalWords = totalWords;
        this.currentDay = currentDay;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public void changeTotalwords(int totalWords){
        this.totalWords = totalWords;
    }

    public void moveToNextDay(){
        this.currentDay++;
    }
    public boolean isCompleted(){
        if (this.status == StudyPlanStatus.COMPLETED.getCode()){
            return true;
        }else {
            return false;
        }
    }
}