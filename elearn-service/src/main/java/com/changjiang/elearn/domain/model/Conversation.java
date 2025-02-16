package com.changjiang.elearn.domain.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Conversation {
    private String id;
    private String userId;
    private String title;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 