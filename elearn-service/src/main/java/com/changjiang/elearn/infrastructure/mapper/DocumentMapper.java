package com.changjiang.elearn.infrastructure.mapper;

import com.changjiang.elearn.domain.model.Document;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DocumentMapper {
    void insert(Document document);
    Document selectById(@Param("id") String id);
    void update(Document document);
} 