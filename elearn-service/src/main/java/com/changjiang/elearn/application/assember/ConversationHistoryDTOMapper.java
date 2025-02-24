package com.changjiang.elearn.application.assember;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.changjiang.elearn.api.dto.ConversationHistoryDTO;
import com.changjiang.elearn.domain.model.ConversationHistory;
import java.util.List;

/**
 * 对话历史记录表 DTO-Domain转换器
 */
@Mapper
public interface ConversationHistoryDTOMapper {
    ConversationHistoryDTOMapper INSTANCE = Mappers.getMapper(ConversationHistoryDTOMapper.class);
    
    /**
     * DTO转换为Domain
     */
    ConversationHistory toDomain(ConversationHistoryDTO dto);
    
    /**
     * Domain转换为DTO
     */
    ConversationHistoryDTO toDTO(ConversationHistory domain);
    
    /**
     * DTO列表转换为Domain列表
     */
    List<ConversationHistory> toDomainList(List<ConversationHistoryDTO> dtoList);
    
    /**
     * Domain列表转换为DTO列表
     */
    List<ConversationHistoryDTO> toDTOList(List<ConversationHistory> domainList);
} 