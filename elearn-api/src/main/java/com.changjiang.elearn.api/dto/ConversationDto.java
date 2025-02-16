package com.changjiang.elearn.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ConversationDto implements Serializable {
    private String conversationId;
    private String userId;
    private String currentText;
    private List<MessageDto> history;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCurrentText() {
        return currentText;
    }

    public void setCurrentText(String currentText) {
        this.currentText = currentText;
    }

    public List<MessageDto> getHistory() {
        return history;
    }

    public void setHistory(List<MessageDto> history) {
        this.history = history;
    }
}

