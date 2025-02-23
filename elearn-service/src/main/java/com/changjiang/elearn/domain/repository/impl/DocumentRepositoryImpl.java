package com.changjiang.elearn.domain.repository.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.changjiang.elearn.domain.model.Document;
import com.changjiang.elearn.domain.repository.DocumentRepository;
import com.changjiang.elearn.infrastructure.converter.DocumentConverter;
import com.changjiang.elearn.infrastructure.persistence.dao.DocumentMapper;
import com.changjiang.elearn.infrastructure.persistence.po.DocumentPO;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.util.StringUtils;

/**
 * 文档表 仓储实现类
 */
@Repository
@RequiredArgsConstructor
public class DocumentRepositoryImpl implements DocumentRepository {
    private final DocumentMapper documentMapper;
    private final DocumentConverter converter = DocumentConverter.INSTANCE;
    
    @Override
    public Document findById(String docId) {
        DocumentPO po = documentMapper.selectById(docId);
        return converter.toDomain(po);
    }
    
    @Override
    public List<Document> findAll() {
        List<DocumentPO> poList = documentMapper.selectList(null);
        return converter.toDomainList(poList);
    }
    
    @Override
    public List<Document> findByCondition(Document condition) {
        DocumentPO po = converter.toPO(condition);
        List<DocumentPO> poList = documentMapper.selectByCondition(po);
        return converter.toDomainList(poList);
    }
    
    @Override
    public IPage<Document> findByPage(int page, int size, Document condition) {
        Page<DocumentPO> pageParam = new Page<>(page, size);
        DocumentPO po = converter.toPO(condition);
        
        IPage<DocumentPO> poPage = documentMapper.selectPage(pageParam, po);
        
        // 转换分页结果
        IPage<Document> domainPage = new Page<>(poPage.getCurrent(), poPage.getSize(), poPage.getTotal());
        domainPage.setRecords(converter.toDomainList(poPage.getRecords()));
        
        return domainPage;
    }
    
    @Override
    public void save(Document entity) {
        DocumentPO po = converter.toPO(entity);
        documentMapper.insert(po);
    }
    
    @Override
    public void update(Document entity) {
        DocumentPO po = converter.toPO(entity);
        documentMapper.updateById(po);
    }
    
    @Override
    public void deleteById(String docId) {
        documentMapper.deleteById(docId);
    }
} 