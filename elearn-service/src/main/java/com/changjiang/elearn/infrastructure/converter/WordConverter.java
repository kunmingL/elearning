package com.changjiang.elearn.infrastructure.converter;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.changjiang.elearn.domain.model.Word;
import com.changjiang.elearn.infrastructure.persistence.po.WordPO;
import java.util.List;

@Mapper
public interface WordConverter {
    WordConverter INSTANCE = Mappers.getMapper(WordConverter.class);
    
    Word toDomain(WordPO po);
    
    WordPO toPO(Word domain);
    
    List<Word> toDomainList(List<WordPO> poList);
    
    List<WordPO> toPOList(List<Word> domainList);
} 