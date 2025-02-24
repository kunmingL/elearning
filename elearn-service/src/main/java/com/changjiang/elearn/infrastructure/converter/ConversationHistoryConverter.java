package com.changjiang.elearn.infrastructure.converter;

import com.changjiang.elearn.domain.model.ConversationHistory;
import com.changjiang.elearn.infrastructure.persistence.po.ConversationHistoryPO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ConversationHistoryConverter {
    ConversationHistoryConverter INSTANCE = Mappers.getMapper(ConversationHistoryConverter.class);
    
    ConversationHistory toDomain(ConversationHistoryPO po);
    ConversationHistoryPO toPO(ConversationHistory domain);

    List<ConversationHistory> toDomainList(List<ConversationHistoryPO> poList);

    List<ConversationHistoryPO> toPOList(List<ConversationHistory> domainList);
} 