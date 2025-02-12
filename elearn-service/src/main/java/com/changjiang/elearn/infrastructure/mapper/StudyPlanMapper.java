package com.changjiang.elearn.infrastructure.mapper;

import com.changjiang.elearn.domain.model.StudyPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface StudyPlanMapper {
    void insert(StudyPlan studyPlan);
    StudyPlan selectById(@Param("id") String id);
    void update(StudyPlan studyPlan);
} 