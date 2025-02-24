package com.changjiang.elearn.domain.repository;

import com.changjiang.elearn.domain.model.Conversation;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;

/**
 * 对话表 仓储接口
 */
public interface ConversationRepository {
    /**
     * 根据ID查询
     */
    Conversation findById(String conversationId);
    
    /**
     * 查询所有
     */
    List<Conversation> findAll();
    
    /**
     * 条件查询
     * @param condition 查询条件
     * @return 符合条件的对象列表
     */
    List<Conversation> findByCondition(Conversation condition);
    
    /**
     * 分页条件查询
     * @param page 页码
     * @param size 每页大小
     * @param condition 查询条件
     * @return 分页结果
     */
    IPage<Conversation> findByPage(int page, int size, Conversation condition);
    
    /**
     * 保存
     */
    void save(Conversation entity);
    
    /**
     * 更新
     */
    void update(Conversation entity);
    
    /**
     * 根据ID删除
     */
    void deleteById(String conversationId);
} 