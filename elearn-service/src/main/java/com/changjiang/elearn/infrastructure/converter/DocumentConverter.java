package com.changjiang.elearn.infrastructure.converter;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.changjiang.elearn.domain.model.Document;
import com.changjiang.elearn.infrastructure.persistence.po.DocumentPO;
import java.util.List;

@Mapper
public interface DocumentConverter {
    DocumentConverter INSTANCE = Mappers.getMapper(DocumentConverter.class);
    
    Document toDomain(DocumentPO po);
    
    DocumentPO toPO(Document domain);
    
    List<Document> toDomainList(List<DocumentPO> poList);
    
    List<DocumentPO> toPOList(List<Document> domainList);
} 