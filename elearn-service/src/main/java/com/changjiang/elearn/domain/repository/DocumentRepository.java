package com.changjiang.elearn.domain.repository;

import com.changjiang.elearn.domain.model.Document;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 文档表 仓储接口
 */
public interface DocumentRepository {
    /**
     * 根据ID查询
     */
    Document findById(String docId);
    
    /**
     * 查询所有
     */
    List<Document> findAll();
    
    /**
     * 条件查询
     * @param condition 查询条件
     * @return 符合条件的对象列表
     */
    List<Document> findByCondition(Document condition);
    
    /**
     * 分页条件查询
     * @param page 页码
     * @param size 每页大小
     * @param condition 查询条件
     * @return 分页结果
     */
    IPage<Document> findByPage(int page, int size, Document condition);
    
    /**
     * 保存
     */
    void save(Document entity);
    
    /**
     * 更新
     */
    void update(Document entity);
    
    /**
     * 根据ID删除
     */
    void deleteById(String docId);
} 