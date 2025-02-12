package com.changjiang.elearn.domain.repository;

import com.changjiang.elearn.domain.model.Word;
import java.util.List;

public interface WordRepository {
    void save(Word word);
    Word findById(String id);
    List<Word> findDailyWords(String planId, int day, int limit);
    void update(Word word);
} 