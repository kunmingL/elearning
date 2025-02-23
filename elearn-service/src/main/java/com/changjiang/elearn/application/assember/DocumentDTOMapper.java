package com.changjiang.elearn.application.assember;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.changjiang.elearn.api.dto.DocumentDTO;
import com.changjiang.elearn.domain.model.Document;
import java.util.List;

/**
 * 文档表 DTO-Domain转换器
 */
@Mapper
public interface DocumentDTOMapper {
    DocumentDTOMapper INSTANCE = Mappers.getMapper(DocumentDTOMapper.class);
    
    /**
     * DTO转换为Domain
     */
    Document toDomain(DocumentDTO dto);
    
    /**
     * Domain转换为DTO
     */
    DocumentDTO toDTO(Document domain);
    
    /**
     * DTO列表转换为Domain列表
     */
    List<Document> toDomainList(List<DocumentDTO> dtoList);
    
    /**
     * Domain列表转换为DTO列表
     */
    List<DocumentDTO> toDTOList(List<Document> domainList);
} 