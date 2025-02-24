package com.changjiang.elearn.api.dto;

import lombok.Data;

@Data
public class MessageDto {
    /**
     * 角色: user-用户, assistant-AI助手
     */
    private String role;
    
    /**
     * 对话内容
     */
    private String content;
} 