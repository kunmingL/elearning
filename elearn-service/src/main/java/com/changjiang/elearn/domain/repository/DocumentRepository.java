package com.changjiang.elearn.domain.repository;

import com.changjiang.elearn.domain.model.Document;

public interface DocumentRepository {
    void save(Document document);
    Document findById(String id);
    void update(Document document);
} 