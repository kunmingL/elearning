package com.changjiang.elearn.domain.repository.impl;

import com.changjiang.elearn.domain.model.StudyPlan;
import com.changjiang.elearn.domain.repository.StudyPlanRepository;
import com.changjiang.elearn.infrastructure.persistence.dao.StudyPlanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class StudyPlanRepositoryImpl implements StudyPlanRepository {

    @Autowired
    private StudyPlanMapper studyPlanMapper;

    @Override
    public void save(StudyPlan studyPlan) {
        studyPlanMapper.insert(studyPlan);
    }

    @Override
    public StudyPlan findById(String id) {
        return studyPlanMapper.selectById(id);
    }

    @Override
    public void update(StudyPlan studyPlan) {
        studyPlanMapper.update(studyPlan);
    }
} 