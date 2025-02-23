package com.changjiang.elearn.domain.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.changjiang.elearn.domain.model.Word;
import com.changjiang.elearn.domain.repository.WordRepository;
import com.changjiang.elearn.infrastructure.converter.WordConverter;
import com.changjiang.elearn.infrastructure.persistence.dao.WordMapper;
import com.changjiang.elearn.infrastructure.persistence.po.WordPO;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import java.util.List;


/**
 * 单词表 仓储实现类
 */
@Repository
@RequiredArgsConstructor
public class WordRepositoryImpl implements WordRepository {
    private final WordMapper wordMapper;
    private final WordConverter converter = WordConverter.INSTANCE;
    
    @Override
    public Word findById(String wordId) {
        WordPO po = wordMapper.selectById(wordId);
        return converter.toDomain(po);
    }
    
    @Override
    public List<Word> findAll() {
        List<WordPO> poList = wordMapper.selectList(null);
        return converter.toDomainList(poList);
    }
    
    @Override
    public List<Word> findByCondition(Word condition) {
        WordPO po = converter.toPO(condition);
        List<WordPO> poList = wordMapper.selectByCondition(po);
        return converter.toDomainList(poList);
    }
    
    @Override
    public IPage<Word> findByPage(int page, int size, Word condition) {
        Page<WordPO> pageParam = new Page<>(page, size);
        WordPO po = converter.toPO(condition);
        
        IPage<WordPO> poPage = wordMapper.selectPage(pageParam, po);
        
        // 转换分页结果
        IPage<Word> domainPage = new Page<>(poPage.getCurrent(), poPage.getSize(), poPage.getTotal());
        domainPage.setRecords(converter.toDomainList(poPage.getRecords()));
        
        return domainPage;
    }
    
    @Override
    public void save(Word entity) {
        WordPO po = converter.toPO(entity);
        wordMapper.insert(po);
    }
    
    @Override
    public void update(Word entity) {
        WordPO po = converter.toPO(entity);
        wordMapper.updateById(po);
    }
    
    @Override
    public void deleteById(String wordId) {
        wordMapper.deleteById(wordId);
    }
} 