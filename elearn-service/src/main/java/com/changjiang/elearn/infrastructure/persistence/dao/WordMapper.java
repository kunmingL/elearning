package com.changjiang.elearn.infrastructure.persistence.dao;

import com.changjiang.elearn.domain.model.Word;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WordMapper {
    void insert(Word word);
    Word selectById(@Param("id") String id);
    List<Word> selectDailyWords(@Param("documentId") String documentId, 
                               @Param("offset") int offset,
                               @Param("limit") int limit);
    void update(Word word);
} 