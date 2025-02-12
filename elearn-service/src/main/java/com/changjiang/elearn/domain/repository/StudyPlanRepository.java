package com.changjiang.elearn.domain.repository;

import com.changjiang.elearn.domain.model.StudyPlan;

public interface StudyPlanRepository {
    void save(StudyPlan studyPlan);
    StudyPlan findById(String id);
    void update(StudyPlan studyPlan);
} 