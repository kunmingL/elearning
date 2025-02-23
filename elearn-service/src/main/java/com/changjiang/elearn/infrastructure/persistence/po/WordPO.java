package com.changjiang.elearn.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.math.BigDecimal;

/**
 * 单词表
 */
@Data
@TableName("t_word")
public class WordPO {
    /**
     * 单词ID
     */
    @TableId("word_id")
    private String wordId;
    /**
     * 学习计划ID
     */
    @TableField("plan_id")
    private String planId;
    /**
     * dailyID
     */
    @TableField("daily_id")
    private String dailyId;
    /**
     * 单词索引
     */
    @TableField("word_idx")
    private Integer wordIdx;
    /**
     * 单词
     */
    @TableField("word")
    private String word;
    /**
     * 音标
     */
    @TableField("pronunciation")
    private String pronunciation;
    /**
     * 翻译
     */
    @TableField("word_translation")
    private String wordTranslation;
    /**
     * 例句
     */
    @TableField("sentence")
    private String sentence;
    /**
     * 例句翻译
     */
    @TableField("sentence_translation")
    private String sentenceTranslation;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
} 