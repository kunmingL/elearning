package com.changjiang.elearn.domain.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.changjiang.elearn.domain.model.ConversationHistory;
import com.changjiang.elearn.domain.repository.ConversationHistoryRepository;
import com.changjiang.elearn.infrastructure.converter.ConversationHistoryConverter;
import com.changjiang.elearn.infrastructure.persistence.dao.ConversationHistoryMapper;
import com.changjiang.elearn.infrastructure.persistence.po.ConversationHistoryPO;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.util.StringUtils;

/**
 * 对话历史记录表 仓储实现类
 */
@Repository
@RequiredArgsConstructor
public class ConversationHistoryRepositoryImpl implements ConversationHistoryRepository {
    private final ConversationHistoryMapper conversationHistoryMapper;
    private final ConversationHistoryConverter converter = ConversationHistoryConverter.INSTANCE;
    
    @Override
    public ConversationHistory findById(String historyId) {
        ConversationHistoryPO po = conversationHistoryMapper.selectById(historyId);
        return converter.toDomain(po);
    }
    
    @Override
    public List<ConversationHistory> findAll() {
        List<ConversationHistoryPO> poList = conversationHistoryMapper.selectList(null);
        return converter.toDomainList(poList);
    }
    
    @Override
    public List<ConversationHistory> findByCondition(ConversationHistory condition) {
        ConversationHistoryPO po = converter.toPO(condition);
        List<ConversationHistoryPO> poList = conversationHistoryMapper.selectByCondition(po);
        return converter.toDomainList(poList);
    }
    
    @Override
    public IPage<ConversationHistory> findByPage(int page, int size, ConversationHistory condition) {
        Page<ConversationHistoryPO> pageParam = new Page<>(page, size);
        ConversationHistoryPO po = converter.toPO(condition);
        
        IPage<ConversationHistoryPO> poPage = conversationHistoryMapper.selectPage(pageParam, po);
        
        // 转换分页结果
        IPage<ConversationHistory> domainPage = new Page<>(poPage.getCurrent(), poPage.getSize(), poPage.getTotal());
        domainPage.setRecords(converter.toDomainList(poPage.getRecords()));
        
        return domainPage;
    }
    
    @Override
    public void save(ConversationHistory entity) {
        ConversationHistoryPO po = converter.toPO(entity);
        conversationHistoryMapper.insert(po);
    }
    
    @Override
    public void update(ConversationHistory entity) {
        ConversationHistoryPO po = converter.toPO(entity);
        conversationHistoryMapper.updateById(po);
    }
    
    @Override
    public void deleteById(String historyId) {
        conversationHistoryMapper.deleteById(historyId);
    }

    @Override
    public Integer getMaxSequence(String conversationId) {
        return conversationHistoryMapper.getMaxSequence(conversationId);
    }
}