package com.changjiang.elearn.api.dto;

import lombok.Data;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 单词表
 */
@Data
public class WordDTO {
    /**
     * 单词ID
     */
    @Size(max = 32)
    private String wordId;
    /**
     * 学习计划ID
     */
    @Size(max = 32)
    private String planId;
    /**
     * dailyID
     */
    @Size(max = 32)
    private String dailyId;
    /**
     * 单词索引
     */
    private Integer wordIdx;
    /**
     * 单词
     */
    @Size(max = 100)
    private String word;
    /**
     * 音标
     */
    @Size(max = 100)
    private String pronunciation;
    /**
     * 翻译
     */
    @Size(max = 500)
    private String wordTranslation;
    /**
     * 例句
     */
    @Size(max = 500)
    private String sentence;
    /**
     * 例句翻译
     */
    @Size(max = 500)
    private String sentenceTranslation;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
} 