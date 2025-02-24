package com.changjiang.elearn.infrastructure.converter;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.changjiang.elearn.domain.model.Conversation;
import com.changjiang.elearn.infrastructure.persistence.po.ConversationPO;
import java.util.List;

@Mapper
public interface ConversationConverter {
    ConversationConverter INSTANCE = Mappers.getMapper(ConversationConverter.class);
    
    Conversation toDomain(ConversationPO po);
    
    ConversationPO toPO(Conversation domain);
    
    List<Conversation> toDomainList(List<ConversationPO> poList);
    
    List<ConversationPO> toPOList(List<Conversation> domainList);
} 