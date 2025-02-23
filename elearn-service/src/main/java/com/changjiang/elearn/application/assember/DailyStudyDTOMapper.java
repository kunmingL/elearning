package com.changjiang.elearn.application.assember;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.changjiang.elearn.api.dto.DailyStudyDTO;
import com.changjiang.elearn.domain.model.DailyStudy;
import java.util.List;

/**
 * 每日学习记录表 DTO-Domain转换器
 */
@Mapper
public interface DailyStudyDTOMapper {
    DailyStudyDTOMapper INSTANCE = Mappers.getMapper(DailyStudyDTOMapper.class);
    
    /**
     * DTO转换为Domain
     */
    DailyStudy toDomain(DailyStudyDTO dto);
    
    /**
     * Domain转换为DTO
     */
    DailyStudyDTO toDTO(DailyStudy domain);
    
    /**
     * DTO列表转换为Domain列表
     */
    List<DailyStudy> toDomainList(List<DailyStudyDTO> dtoList);
    
    /**
     * Domain列表转换为DTO列表
     */
    List<DailyStudyDTO> toDTOList(List<DailyStudy> domainList);
} 