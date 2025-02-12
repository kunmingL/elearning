package com.changjiang.elearn.domain.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Word {
    private String id;
    private String documentId;
    private String word;
    private String pronunciation;
    private String translation;
    private String audioPath;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 