package com.changjiang.elearn.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 每日学习记录表
 */
@Getter
@NoArgsConstructor
public class DailyStudy {
    /**
     * dailyID
     */
    private String dailyId;
    /**
     * 计划ID
     */
    private String planId;
    /**
     * 用户ID
     */
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

    @Builder
    public DailyStudy(String dailyId, String planId, String userId, Integer studyDay, Integer currentWordIdx, Integer wordCount, Integer status, Date createTime, Date updateTime) {
        this.dailyId = dailyId;
        this.planId = planId;
        this.userId = userId;
        this.studyDay = studyDay;
        this.currentWordIdx = currentWordIdx;
        this.wordCount = wordCount;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public void changeCurrentWordIdx(Integer currentWordIdx){
        this.currentWordIdx = currentWordIdx;
    }

    public void changeStatus(Integer status){
        this.status = status;
    }
}