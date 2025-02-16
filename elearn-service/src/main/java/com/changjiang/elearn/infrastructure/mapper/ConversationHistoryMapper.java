package com.changjiang.elearn.infrastructure.mapper;

import com.changjiang.elearn.domain.model.ConversationHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 对话历史数据访问接口
 */
@Mapper
public interface ConversationHistoryMapper {
    /**
     * 插入对话历史记录
     * @param history 历史记录实体
     */
    void insert(ConversationHistory history);

    /**
     * 查询对话的所有历史记录
     * @param conversationId 对话ID
     * @return 历史记录列表
     */
    List<ConversationHistory> selectByConversationId(@Param("conversationId") String conversationId);

    /**
     * 获取对话的最大序号
     * @param conversationId 对话ID
     * @return 最大序号
     */
    Integer selectMaxSequence(@Param("conversationId") String conversationId);
} 