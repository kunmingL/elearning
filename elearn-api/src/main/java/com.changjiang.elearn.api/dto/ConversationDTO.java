package com.changjiang.elearn.api.dto;

import lombok.Data;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 对话表
 */
@Data
public class ConversationDTO {
    /**
     * 对话ID
     */
    @Size(max = 32)
    private String conversationId;
    /**
     * 用户ID
     */
    @Size(max = 32)
    private String userId;
    /**
     * 对话标题
     */
    @Size(max = 100)
    private String title;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
} 