package com.changjiang.elearn.infrastructure.converter;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.changjiang.elearn.domain.model.StudyPlan;
import com.changjiang.elearn.infrastructure.persistence.po.StudyPlanPO;
import java.util.List;

@Mapper
public interface StudyPlanConverter {
    StudyPlanConverter INSTANCE = Mappers.getMapper(StudyPlanConverter.class);
    
    StudyPlan toDomain(StudyPlanPO po);
    
    StudyPlanPO toPO(StudyPlan domain);
    
    List<StudyPlan> toDomainList(List<StudyPlanPO> poList);
    
    List<StudyPlanPO> toPOList(List<StudyPlan> domainList);
} 