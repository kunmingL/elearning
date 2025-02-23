package com.changjiang.elearn.domain.repository;

import com.changjiang.elearn.domain.model.StudyPlan;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 学习计划表 仓储接口
 */
public interface StudyPlanRepository {
    /**
     * 根据ID查询
     */
    StudyPlan findById(String planId);
    
    /**
     * 查询所有
     */
    List<StudyPlan> findAll();
    
    /**
     * 条件查询
     * @param condition 查询条件
     * @return 符合条件的对象列表
     */
    List<StudyPlan> findByCondition(StudyPlan condition);
    
    /**
     * 分页条件查询
     * @param page 页码
     * @param size 每页大小
     * @param condition 查询条件
     * @return 分页结果
     */
    IPage<StudyPlan> findByPage(int page, int size, StudyPlan condition);
    
    /**
     * 保存
     */
    void save(StudyPlan entity);
    
    /**
     * 更新
     */
    void update(StudyPlan entity);
    
    /**
     * 根据ID删除
     */
    void deleteById(String planId);
} 