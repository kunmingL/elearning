package com.changjiang.elearn.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.math.BigDecimal;

/**
 * 对话表
 */
@Getter
@NoArgsConstructor
public class Conversation {
    /**
     * 对话ID
     */
    private String conversationId;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 对话标题
     */
    private String title;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    @Builder
    public Conversation(String conversationId, String userId, String title, LocalDateTime createTime, LocalDateTime updateTime) {
        this.conversationId = conversationId;
        this.userId = userId;
        this.title = title;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
}