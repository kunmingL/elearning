package com.changjiang.elearn.domain.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.changjiang.elearn.domain.model.Conversation;
import com.changjiang.elearn.domain.repository.ConversationRepository;
import com.changjiang.elearn.infrastructure.converter.ConversationConverter;
import com.changjiang.elearn.infrastructure.persistence.dao.ConversationMapper;
import com.changjiang.elearn.infrastructure.persistence.po.ConversationPO;

import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.util.StringUtils;

/**
 * 对话表 仓储实现类
 */
@Repository
@RequiredArgsConstructor
public class ConversationRepositoryImpl implements ConversationRepository {
    private final ConversationMapper conversationMapper;
    private final ConversationConverter converter = ConversationConverter.INSTANCE;
    
    @Override
    public Conversation findById(String conversationId) {
        ConversationPO po = conversationMapper.selectById(conversationId);
        return converter.toDomain(po);
    }
    
    @Override
    public List<Conversation> findAll() {
        List<ConversationPO> poList = conversationMapper.selectList(null);
        return converter.toDomainList(poList);
    }
    
    @Override
    public List<Conversation> findByCondition(Conversation condition) {
        ConversationPO po = converter.toPO(condition);
        List<ConversationPO> poList = conversationMapper.selectByCondition(po);
        return converter.toDomainList(poList);
    }
    
    @Override
    public IPage<Conversation> findByPage(int page, int size, Conversation condition) {
        Page<ConversationPO> pageParam = new Page<>(page, size);
        ConversationPO po = converter.toPO(condition);
        
        IPage<ConversationPO> poPage = conversationMapper.selectPage(pageParam, po);
        
        // 转换分页结果
        IPage<Conversation> domainPage = new Page<>(poPage.getCurrent(), poPage.getSize(), poPage.getTotal());
        domainPage.setRecords(converter.toDomainList(poPage.getRecords()));
        
        return domainPage;
    }
    
    @Override
    public void save(Conversation entity) {
        ConversationPO po = converter.toPO(entity);
        conversationMapper.insert(po);
    }
    
    @Override
    public void update(Conversation entity) {
        ConversationPO po = converter.toPO(entity);
        conversationMapper.updateById(po);
    }
    
    @Override
    public void deleteById(String conversationId) {
        conversationMapper.deleteById(conversationId);
    }
} 