package com.changjiang.elearn.domain.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.changjiang.elearn.domain.model.DailyStudy;
import com.changjiang.elearn.domain.repository.DailyStudyRepository;
import com.changjiang.elearn.infrastructure.converter.DailyStudyConverter;
import com.changjiang.elearn.infrastructure.persistence.dao.DailyStudyMapper;
import com.changjiang.elearn.infrastructure.persistence.po.DailyStudyPO;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.util.StringUtils;

/**
 * 每日学习记录表 仓储实现类
 */
@Repository
@RequiredArgsConstructor
public class DailyStudyRepositoryImpl implements DailyStudyRepository {
    private final DailyStudyMapper dailyStudyMapper;
    private final DailyStudyConverter converter = DailyStudyConverter.INSTANCE;
    
    @Override
    public DailyStudy findById(String dailyId) {
        DailyStudyPO po = dailyStudyMapper.selectById(dailyId);
        return converter.toDomain(po);
    }
    
    @Override
    public List<DailyStudy> findAll() {
        List<DailyStudyPO> poList = dailyStudyMapper.selectList(null);
        return converter.toDomainList(poList);
    }
    
    @Override
    public List<DailyStudy> findByCondition(DailyStudy condition) {
        DailyStudyPO po = converter.toPO(condition);
        List<DailyStudyPO> poList = dailyStudyMapper.selectByCondition(po);
        return converter.toDomainList(poList);
    }
    
    @Override
    public IPage<DailyStudy> findByPage(int page, int size, DailyStudy condition) {
        Page<DailyStudyPO> pageParam = new Page<>(page, size);
        DailyStudyPO po = converter.toPO(condition);
        
        IPage<DailyStudyPO> poPage = dailyStudyMapper.selectPage(pageParam, po);
        
        // 转换分页结果
        IPage<DailyStudy> domainPage = new Page<>(poPage.getCurrent(), poPage.getSize(), poPage.getTotal());
        domainPage.setRecords(converter.toDomainList(poPage.getRecords()));
        
        return domainPage;
    }
    
    @Override
    public void save(DailyStudy entity) {
        DailyStudyPO po = converter.toPO(entity);
        dailyStudyMapper.insert(po);
    }
    
    @Override
    public void update(DailyStudy entity) {
        DailyStudyPO po = converter.toPO(entity);
        dailyStudyMapper.updateById(po);
    }
    
    @Override
    public void deleteById(String dailyId) {
        dailyStudyMapper.deleteById(dailyId);
    }
} 