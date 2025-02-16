package com.changjiang.elearn.domain.repository;

import com.changjiang.elearn.domain.model.Conversation;
import java.util.List;

public interface ConversationRepository {
    void save(Conversation conversation);
    Conversation findById(String id);
    List<Conversation> findByUserId(String userId);
} 