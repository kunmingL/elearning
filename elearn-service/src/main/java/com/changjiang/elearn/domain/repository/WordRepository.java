package com.changjiang.elearn.domain.repository;

import com.changjiang.elearn.domain.model.Word;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 单词表 仓储接口
 */
public interface WordRepository {
    /**
     * 根据ID查询
     */
    Word findById(String wordId);
    
    /**
     * 查询所有
     */
    List<Word> findAll();
    
    /**
     * 条件查询
     * @param condition 查询条件
     * @return 符合条件的对象列表
     */
    List<Word> findByCondition(Word condition);
    
    /**
     * 分页条件查询
     * @param page 页码
     * @param size 每页大小
     * @param condition 查询条件
     * @return 分页结果
     */
    IPage<Word> findByPage(int page, int size, Word condition);
    
    /**
     * 保存
     */
    void save(Word entity);
    
    /**
     * 更新
     */
    void update(Word entity);
    
    /**
     * 根据ID删除
     */
    void deleteById(String wordId);
} 