package com.changjiang.elearn.infrastructure.mapper;

import com.changjiang.elearn.domain.model.Conversation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 对话数据访问接口
 */
@Mapper
public interface ConversationMapper {
    /**
     * 插入新对话
     * @param conversation 对话实体
     */
    void insert(Conversation conversation);

    /**
     * 根据ID查询对话
     * @param id 对话ID
     * @return 对话实体
     */
    Conversation selectById(@Param("id") String id);

    /**
     * 查询用户的所有对话
     * @param userId 用户ID
     * @return 对话列表
     */
    List<Conversation> selectByUserId(@Param("userId") String userId);
} 