package com.changjiang.elearn.infrastructure.mapper;

import com.changjiang.elearn.domain.model.ConversationHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ConversationHistoryMapper {
    
    // ... 其他已有的方法 ...
    
    /**
     * 获取对话的最大序列号
     * @param conversationId 对话ID
     * @return 最大序列号
     */
    Integer getMaxSequence(@Param("conversationId") String conversationId);
} 