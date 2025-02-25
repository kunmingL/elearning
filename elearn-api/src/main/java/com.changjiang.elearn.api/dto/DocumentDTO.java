package com.changjiang.elearn.api.dto;

import lombok.Data;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 文档表
 */
@Data
public class DocumentDTO {
    /**
     * 文档ID
     */
    @Size(max = 32)
    private String docId;
    /**
     * 学习计划ID
     */
    @Size(max = 32)
    private String planId;
    /**
     * 用户ID
     */
    @Size(max = 32)
    private String userId;
    /**
     * 文件名
     */
    @Size(max = 200)
    private String fileName;
    /**
     * 文件路径
     */
    @Size(max = 500)
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
} 