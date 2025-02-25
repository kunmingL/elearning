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
 * 文档表
 */
@Getter
@NoArgsConstructor
public class Document {
    /**
     * 文档ID
     */
    private String docId;
    /**
     * 学习计划ID
     */
    private String planId;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件路径
     */
    private String filePath;
    /**
     * 文件大小(字节)
     */
    private Long fileSize;
    /**
     * 单词数量
     */
    private Integer wordCount;
    /**
     * 状态:0-待处理,1-处理中,2-处理完成
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    @Builder
    public Document(String docId, String planId, String userId, String fileName, String filePath, Long fileSize, Integer wordCount, Integer status, Date createTime, Date updateTime) {
        this.docId = docId;
        this.planId = planId;
        this.userId = userId;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.wordCount = wordCount;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public void changeStatus(Integer status) {
        this.status = status;
    }
}