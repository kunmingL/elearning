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
 * 对话历史记录表
 */
@Getter
@NoArgsConstructor
public class ConversationHistory {
    /**
     * 历史记录ID
     */
    private String historyId;
    /**
     * 对话ID
     */
    private String conversationId;
    /**
     * 角色:user-用户,assistant-AI助手
     */
    private String role;
    /**
     * 内容
     */
    private String content;
    /**
     * 顺序号
     */
    private Integer sequence;
    /**
     * 创建时间
     */
    private Date createTime;

    @Builder
    public ConversationHistory(String historyId, String conversationId, String role, String content, Integer sequence, Date createTime) {
        this.historyId = historyId;
        this.conversationId = conversationId;
        this.role = role;
        this.content = content;
        this.sequence = sequence;
        this.createTime = createTime;
    }
}