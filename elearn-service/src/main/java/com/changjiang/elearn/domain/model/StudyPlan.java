package com.changjiang.elearn.domain.model;
import com.changjiang.elearn.domain.enums.StudyPlanStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StudyPlan {
    private String id;
    private String userId;
    private String documentId;
    private Integer dailyWords;
    private Integer totalDays;
    private Integer currentDay;
    private StudyPlanStatus status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    public boolean isCompleted() {
        return currentDay > totalDays;
    }
    
    public void moveToNextDay() {
        if(!isCompleted()) {
            this.currentDay++;
            if(isCompleted()) {
                this.status = StudyPlanStatus.COMPLETED;
            }
        }
    }
} 