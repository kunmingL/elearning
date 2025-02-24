package com.changjiang.elearn.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.math.BigDecimal;

/**
 * 对话表
 */
@Data
@TableName("t_conversation")
public class ConversationPO {
    /**
     * 对话ID
     */
    @TableId("conversation_id")
    private String conversationId;
    /**
     * 用户ID
     */
    @TableField("user_id")
    private String userId;
    /**
     * 对话标题
     */
    @TableField("title")
    private String title;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
} 