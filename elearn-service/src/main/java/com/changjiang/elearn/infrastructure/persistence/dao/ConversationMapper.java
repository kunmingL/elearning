package com.changjiang.elearn.infrastructure.persistence.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.changjiang.elearn.infrastructure.persistence.po.ConversationPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 对话表 数据访问接口
 */
@Mapper
public interface ConversationMapper extends BaseMapper<ConversationPO> {
    /**
     * 根据条件查询列表
     * @param condition 查询条件
     * @return 实体列表
     */
    List<ConversationPO> selectByCondition(@Param("condition") ConversationPO condition);
    
    /**
     * 根据条件分页查询
     * @param page 分页参数
     * @param condition 查询条件
     * @return 分页结果
     */
    IPage<ConversationPO> selectPage(IPage<ConversationPO> page, @Param("condition") ConversationPO condition);
} 