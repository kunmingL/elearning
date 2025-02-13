package com.changjiang.elearn.domain.repository;

import com.changjiang.elearn.domain.enums.DocumentStatus;
import com.changjiang.elearn.domain.model.Document;

import java.util.List;

public interface DocumentRepository {
    void save(Document document);
    Document findById(String id);
    void update(Document document);
    List<Document> findByStatus(DocumentStatus status);
} 