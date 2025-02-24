package com.changjiang.elearn.domain.repository;

import com.changjiang.elearn.domain.model.ConversationHistory;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;

/**
 * 对话历史记录表 仓储接口
 */
public interface ConversationHistoryRepository {
    /**
     * 根据ID查询
     */
    ConversationHistory findById(String historyId);
    
    /**
     * 查询所有
     */
    List<ConversationHistory> findAll();
    
    /**
     * 条件查询
     * @param condition 查询条件
     * @return 符合条件的对象列表
     */
    List<ConversationHistory> findByCondition(ConversationHistory condition);
    
    /**
     * 分页条件查询
     * @param page 页码
     * @param size 每页大小
     * @param condition 查询条件
     * @return 分页结果
     */
    IPage<ConversationHistory> findByPage(int page, int size, ConversationHistory condition);
    
    /**
     * 保存
     */
    void save(ConversationHistory entity);
    
    /**
     * 更新
     */
    void update(ConversationHistory entity);
    
    /**
     * 根据ID删除
     */
    void deleteById(String historyId);

    Integer getMaxSequence(String conversationId);
} 