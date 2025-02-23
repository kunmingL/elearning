package com.changjiang.elearn.domain.repository.impl;

import com.changjiang.elearn.domain.model.ConversationHistory;
import com.changjiang.elearn.domain.repository.ConversationHistoryRepository;
import com.changjiang.elearn.infrastructure.persistence.dao.ConversationHistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 对话历史仓储实现类
 */
@Repository
public class ConversationHistoryRepositoryImpl implements ConversationHistoryRepository {

    @Autowired
    private ConversationHistoryMapper conversationHistoryMapper;

    @Override
    public void save(ConversationHistory history) {
        conversationHistoryMapper.insert(history);
    }

    @Override
    public List<ConversationHistory> findByConversationId(String conversationId) {
        return conversationHistoryMapper.selectByConversationId(conversationId);
    }

    @Override
    public Integer getMaxSequence(String conversationId) {
        return conversationHistoryMapper.selectMaxSequence(conversationId);
    }
} 