package com.changjiang.elearn.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 单词表
 */
@Getter
@NoArgsConstructor
public class Word {
    /**
     * 单词ID
     */
    private String wordId;
    /**
     * 学习计划ID
     */
    private String planId;
    /**
     * dailyID
     */
    private String dailyId;
    /**
     * 单词索引
     */
    private Integer wordIdx;
    /**
     * 单词
     */
    private String word;
    /**
     * 音标
     */
    private String pronunciation;
    /**
     * 翻译
     */
    private String wordTranslation;
    /**
     * 例句
     */
    private String sentence;
    /**
     * 例句翻译
     */
    private String sentenceTranslation;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    @Builder
    public Word(String wordId, String planId, String dailyId, Integer wordIdx, String word, String pronunciation, String wordTranslation, String sentence, String sentenceTranslation, Date createTime, Date updateTime) {
        this.wordId = wordId;
        this.planId = planId;
        this.dailyId = dailyId;
        this.wordIdx = wordIdx;
        this.word = word;
        this.pronunciation = pronunciation;
        this.wordTranslation = wordTranslation;
        this.sentence = sentence;
        this.sentenceTranslation = sentenceTranslation;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
}