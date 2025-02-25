package com.changjiang.elearn.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 对话历史记录表
 */
@Data
@TableName("t_conversation_history")
public class ConversationHistoryPO {
    /**
     * 历史记录ID
     */
    @TableId("history_id")
    private String historyId;
    /**
     * 对话ID
     */
    @TableField("conversation_id")
    private String conversationId;
    /**
     * 角色:user-用户,assistant-AI助手
     */
    @TableField("role")
    private String role;
    /**
     * 内容
     */
    @TableField("content")
    private String content;
    /**
     * 顺序号
     */
    @TableField("sequence")
    private Integer sequence;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
} 