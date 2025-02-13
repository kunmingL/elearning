package com.changjiang.elearn.infrastructure.repository;

import com.changjiang.elearn.domain.enums.DocumentStatus;
import com.changjiang.elearn.domain.model.Document;
import com.changjiang.elearn.domain.repository.DocumentRepository;
import com.changjiang.elearn.infrastructure.mapper.DocumentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DocumentRepositoryImpl implements DocumentRepository {

    @Autowired
    private DocumentMapper documentMapper;

    @Override
    public void save(Document document) {
        documentMapper.insert(document);
    }

    @Override
    public Document findById(String id) {
        return documentMapper.selectById(id);
    }

    @Override
    public void update(Document document) {
        documentMapper.update(document);
    }

    @Override
    public List<Document> findByStatus(DocumentStatus status) {
        return List.of();
    }
}