package com.changjiang.elearn.infrastructure.persistence.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.changjiang.elearn.infrastructure.persistence.po.WordPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 单词表 数据访问接口
 */
@Mapper
public interface WordMapper extends BaseMapper<WordPO> {
    /**
     * 根据条件查询列表
     * @param condition 查询条件
     * @return 实体列表
     */
    List<WordPO> selectByCondition(@Param("condition") WordPO condition);
    
    /**
     * 根据条件分页查询
     * @param page 分页参数
     * @param condition 查询条件
     * @return 分页结果
     */
    IPage<WordPO> selectPage(IPage<WordPO> page, @Param("condition") WordPO condition);
} 