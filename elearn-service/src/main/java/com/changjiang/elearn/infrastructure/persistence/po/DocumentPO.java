package com.changjiang.elearn.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.math.BigDecimal;

/**
 * 文档表
 */
@Data
@TableName("t_document")
public class DocumentPO {
    /**
     * 文档ID
     */
    @TableId("doc_id")
    private String docId;
    /**
     * 学习计划ID
     */
    @TableField("plan_id")
    private String planId;
    /**
     * 用户ID
     */
    @TableField("user_id")
    private String userId;
    /**
     * 文件名
     */
    @TableField("file_name")
    private String fileName;
    /**
     * 文件路径
     */
    @TableField("file_path")
    private String filePath;
    /**
     * 文件大小(字节)
     */
    @TableField("file_size")
    private Long fileSize;
    /**
     * 单词数量
     */
    @TableField("word_count")
    private Integer wordCount;
    /**
     * 状态:0-待处理,1-处理中,2-处理完成
     */
    @TableField("status")
    private Integer status;
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