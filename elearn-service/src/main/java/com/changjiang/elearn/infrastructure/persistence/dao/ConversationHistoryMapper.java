package com.changjiang.elearn.infrastructure.persistence.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.changjiang.elearn.infrastructure.persistence.po.ConversationHistoryPO;
import com.changjiang.elearn.domain.model.ConversationHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 对话历史记录表 数据访问接口
 */
@Mapper
public interface ConversationHistoryMapper extends BaseMapper<ConversationHistoryPO> {
    /**
     * 根据条件查询列表
     * @param condition 查询条件
     * @return 实体列表
     */
    List<ConversationHistoryPO> selectByCondition(@Param("condition") ConversationHistoryPO condition);
    
    /**
     * 根据条件分页查询
     * @param page 分页参数
     * @param condition 查询条件
     * @return 分页结果
     */
    IPage<ConversationHistoryPO> selectPage(IPage<ConversationHistoryPO> page, @Param("condition") ConversationHistoryPO condition);

    /**
     * 获取对话的最大序列号
     * @param conversationId 对话ID
     * @return 最大序列号
     */
    Integer getMaxSequence(@Param("conversationId") String conversationId);

    /**
     * 保存历史记录
     */
    void insert(ConversationHistory history);
} 