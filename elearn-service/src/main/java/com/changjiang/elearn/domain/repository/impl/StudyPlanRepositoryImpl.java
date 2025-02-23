package com.changjiang.elearn.domain.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.changjiang.elearn.domain.model.StudyPlan;
import com.changjiang.elearn.domain.repository.StudyPlanRepository;
import com.changjiang.elearn.infrastructure.converter.StudyPlanConverter;
import com.changjiang.elearn.infrastructure.persistence.dao.StudyPlanMapper;
import com.changjiang.elearn.infrastructure.persistence.po.StudyPlanPO;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.util.StringUtils;

/**
 * 学习计划表 仓储实现类
 */
@Repository
@RequiredArgsConstructor
public class StudyPlanRepositoryImpl implements StudyPlanRepository {
    private final StudyPlanMapper studyPlanMapper;
    private final StudyPlanConverter converter = StudyPlanConverter.INSTANCE;
    
    @Override
    public StudyPlan findById(String planId) {
        StudyPlanPO po = studyPlanMapper.selectById(planId);
        return converter.toDomain(po);
    }
    
    @Override
    public List<StudyPlan> findAll() {
        List<StudyPlanPO> poList = studyPlanMapper.selectList(null);
        return converter.toDomainList(poList);
    }
    
    @Override
    public List<StudyPlan> findByCondition(StudyPlan condition) {
        StudyPlanPO po = converter.toPO(condition);
        List<StudyPlanPO> poList = studyPlanMapper.selectByCondition(po);
        return converter.toDomainList(poList);
    }
    
    @Override
    public IPage<StudyPlan> findByPage(int page, int size, StudyPlan condition) {
        Page<StudyPlanPO> pageParam = new Page<>(page, size);
        StudyPlanPO po = converter.toPO(condition);
        
        IPage<StudyPlanPO> poPage = studyPlanMapper.selectPage(pageParam, po);
        
        // 转换分页结果
        IPage<StudyPlan> domainPage = new Page<>(poPage.getCurrent(), poPage.getSize(), poPage.getTotal());
        domainPage.setRecords(converter.toDomainList(poPage.getRecords()));
        
        return domainPage;
    }
    
    @Override
    public void save(StudyPlan entity) {
        StudyPlanPO po = converter.toPO(entity);
        studyPlanMapper.insert(po);
    }
    
    @Override
    public void update(StudyPlan entity) {
        StudyPlanPO po = converter.toPO(entity);
        studyPlanMapper.updateById(po);
    }
    
    @Override
    public void deleteById(String planId) {
        studyPlanMapper.deleteById(planId);
    }
} 