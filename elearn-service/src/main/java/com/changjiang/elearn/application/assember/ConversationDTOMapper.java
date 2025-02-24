package com.changjiang.elearn.application.assember;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.changjiang.elearn.api.dto.ConversationDTO;
import com.changjiang.elearn.domain.model.Conversation;
import java.util.List;

/**
 * 对话表 DTO-Domain转换器
 */
@Mapper
public interface ConversationDTOMapper {
    ConversationDTOMapper INSTANCE = Mappers.getMapper(ConversationDTOMapper.class);
    
    /**
     * DTO转换为Domain
     */
    Conversation toDomain(ConversationDTO dto);
    
    /**
     * Domain转换为DTO
     */
    ConversationDTO toDTO(Conversation domain);
    
    /**
     * DTO列表转换为Domain列表
     */
    List<Conversation> toDomainList(List<ConversationDTO> dtoList);
    
    /**
     * Domain列表转换为DTO列表
     */
    List<ConversationDTO> toDTOList(List<Conversation> domainList);
} 