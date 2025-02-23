package com.changjiang.elearn.application.assember;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.changjiang.elearn.api.dto.WordDTO;
import com.changjiang.elearn.domain.model.Word;
import java.util.List;

/**
 * 单词表 DTO-Domain转换器
 */
@Mapper
public interface WordDTOMapper {
    WordDTOMapper INSTANCE = Mappers.getMapper(WordDTOMapper.class);
    
    /**
     * DTO转换为Domain
     */
    Word toDomain(WordDTO dto);
    
    /**
     * Domain转换为DTO
     */
    WordDTO toDTO(Word domain);
    
    /**
     * DTO列表转换为Domain列表
     */
    List<Word> toDomainList(List<WordDTO> dtoList);
    
    /**
     * Domain列表转换为DTO列表
     */
    List<WordDTO> toDTOList(List<Word> domainList);
} 