package com.changjiang.elearn.domain.repository;

import com.changjiang.elearn.domain.model.DailyStudy;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 每日学习记录表 仓储接口
 */
public interface DailyStudyRepository {
    /**
     * 根据ID查询
     */
    DailyStudy findById(String dailyId);
    
    /**
     * 查询所有
     */
    List<DailyStudy> findAll();
    
    /**
     * 条件查询
     * @param condition 查询条件
     * @return 符合条件的对象列表
     */
    List<DailyStudy> findByCondition(DailyStudy condition);
    
    /**
     * 分页条件查询
     * @param page 页码
     * @param size 每页大小
     * @param condition 查询条件
     * @return 分页结果
     */
    IPage<DailyStudy> findByPage(int page, int size, DailyStudy condition);
    
    /**
     * 保存
     */
    void save(DailyStudy entity);
    
    /**
     * 更新
     */
    void update(DailyStudy entity);
    
    /**
     * 根据ID删除
     */
    void deleteById(String dailyId);
} 