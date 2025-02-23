package com.changjiang.elearn.domain.repository.impl;

import com.changjiang.elearn.domain.model.Word;
import com.changjiang.elearn.domain.repository.WordRepository;
import com.changjiang.elearn.infrastructure.persistence.dao.WordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WordRepositoryImpl implements WordRepository {

    @Autowired
    private WordMapper wordMapper;

    @Override
    public void save(Word word) {
        wordMapper.insert(word);
    }

    @Override
    public Word findById(String id) {
        return wordMapper.selectById(id);
    }

    @Override
    public List<Word> findDailyWords(String documentId, int day, int limit) {
        int offset = (day - 1) * limit;
        return wordMapper.selectDailyWords(documentId, offset, limit);
    }

    @Override
    public void update(Word word) {
        wordMapper.update(word);
    }
} 