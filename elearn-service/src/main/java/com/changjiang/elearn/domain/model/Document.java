package com.changjiang.elearn.domain.model;
import com.changjiang.elearn.domain.enums.DocumentStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Document {
    private String id;
    private String userId;
    private String fileName;
    private String filePath;
    private Long fileSize;
    private Integer wordCount;
    private DocumentStatus status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    public boolean isOversize(long maxSize) {
        return this.fileSize > maxSize;
    }
    
    public void markAsProcessing() {
        this.status = DocumentStatus.PROCESSING;
    }
    
    public void completeProcessing(int wordCount) {
        this.wordCount = wordCount;
        this.status = DocumentStatus.COMPLETED;
    }
} 