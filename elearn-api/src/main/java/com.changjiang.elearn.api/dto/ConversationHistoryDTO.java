package com.changjiang.elearn.api.dto;

import lombok.Data;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 对话历史记录表
 */
@Data
public class ConversationHistoryDTO {
    /**
     * 历史记录ID
     */
    @Size(max = 32)
    private String historyId;
    /**
     * 对话ID
     */
    @Size(max = 32)
    private String conversationId;
    /**
     * 角色:user-用户,assistant-AI助手
     */
    @Size(max = 20)
    private String role;
    /**
     * 内容
     */
    @Size(max = 65535)
    private String content;
    /**
     * 顺序号
     */
    private Integer sequence;
    /**
     * 创建时间
     */
    private Date createTime;
} 