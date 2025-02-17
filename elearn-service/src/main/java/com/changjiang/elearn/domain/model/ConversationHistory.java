package com.changjiang.elearn.domain.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ConversationHistory {
    private String id;
    private String conversationId;
    private String role;
    private String content;
    private Integer sequence;
    private LocalDateTime createTime;
} 