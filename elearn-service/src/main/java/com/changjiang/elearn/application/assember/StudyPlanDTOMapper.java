package com.changjiang.elearn.application.assember;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.changjiang.elearn.api.dto.StudyPlanDTO;
import com.changjiang.elearn.domain.model.StudyPlan;
import java.util.List;

/**
 * 学习计划表 DTO-Domain转换器
 */
@Mapper
public interface StudyPlanDTOMapper {
    StudyPlanDTOMapper INSTANCE = Mappers.getMapper(StudyPlanDTOMapper.class);
    
    /**
     * DTO转换为Domain
     */
    StudyPlan toDomain(StudyPlanDTO dto);
    
    /**
     * Domain转换为DTO
     */
    StudyPlanDTO toDTO(StudyPlan domain);
    
    /**
     * DTO列表转换为Domain列表
     */
    List<StudyPlan> toDomainList(List<StudyPlanDTO> dtoList);
    
    /**
     * Domain列表转换为DTO列表
     */
    List<StudyPlanDTO> toDTOList(List<StudyPlan> domainList);
} 