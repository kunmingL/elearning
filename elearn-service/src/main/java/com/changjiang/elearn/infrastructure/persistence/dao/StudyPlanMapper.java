package com.changjiang.elearn.infrastructure.persistence.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.changjiang.elearn.infrastructure.persistence.po.StudyPlanPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 学习计划表 数据访问接口
 */
@Mapper
public interface StudyPlanMapper extends BaseMapper<StudyPlanPO> {
    /**
     * 根据条件查询列表
     * @param condition 查询条件
     * @return 实体列表
     */
    List<StudyPlanPO> selectByCondition(@Param("condition") StudyPlanPO condition);
    
    /**
     * 根据条件分页查询
     * @param page 分页参数
     * @param condition 查询条件
     * @return 分页结果
     */
    IPage<StudyPlanPO> selectPage(IPage<StudyPlanPO> page, @Param("condition") StudyPlanPO condition);
} 