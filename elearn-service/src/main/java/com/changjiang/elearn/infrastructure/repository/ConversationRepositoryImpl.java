package com.changjiang.elearn.infrastructure.repository;

import com.changjiang.elearn.domain.model.Conversation;
import com.changjiang.elearn.domain.repository.ConversationRepository;
import com.changjiang.elearn.infrastructure.mapper.ConversationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 对话仓储实现类
 */
@Repository
public class ConversationRepositoryImpl implements ConversationRepository {

    @Autowired
    private ConversationMapper conversationMapper;

    @Override
    public void save(Conversation conversation) {
        conversationMapper.insert(conversation);
    }

    @Override
    public Conversation findById(String id) {
        return conversationMapper.selectById(id);
    }

    @Override
    public List<Conversation> findByUserId(String userId) {
        return conversationMapper.selectByUserId(userId);
    }
} 