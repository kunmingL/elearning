package com.changjiang.elearn.api.dto;

import lombok.Data;
import java.util.List;

@Data
public class ConversationInputDto {
    /**
     * 对话ID,新对话为空
     */
    private String conversationId;
    
    /**
     * 用户ID
     */
    private String userId;
    
    /**
     * 当前输入的文本
     */
    private String currentText;
    
    /**
     * 历史对话记录
     */
    private List<MessageDto> history;
} 