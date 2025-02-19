package com.changjiang.elearn.infrastructure.persistence.dao;

import com.changjiang.elearn.domain.model.Document;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DocumentMapper {
    int insert(Document document);
    Document selectById(@Param("id") String id);
    int update(Document document);
} 