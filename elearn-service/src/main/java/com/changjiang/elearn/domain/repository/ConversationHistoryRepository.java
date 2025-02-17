package com.changjiang.elearn.domain.repository;

import com.changjiang.elearn.domain.model.ConversationHistory;
import java.util.List;

public interface ConversationHistoryRepository {
    void save(ConversationHistory history);
    List<ConversationHistory> findByConversationId(String conversationId);
    Integer getMaxSequence(String conversationId);
} 