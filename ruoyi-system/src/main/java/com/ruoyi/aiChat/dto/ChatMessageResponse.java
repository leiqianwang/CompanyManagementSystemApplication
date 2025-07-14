package com.ruoyi.aiChat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

/**
 * 聊天消息响应 DTO
 * 
 * @author ruoyi
 * @date 2025-07-03
 */
public class ChatMessageResponse {
    
    /** 消息ID */
    private String messageId;
    
    /** 会话ID */
    private String sessionId;
    
    /** 消息内容 */
    private String content;
    
    /** 消息类型：user/assistant */
    private String messageType;
    
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    /** 是否完成 */
    private Boolean finished = true;
    
    /** 使用的模型 */
    private String model;
    
    /** 消耗的令牌数 */
    private Integer tokenUsage;
    
    /** 错误消息 */
    private String error;

    public ChatMessageResponse() {
        this.createTime = LocalDateTime.now();
    }

    public ChatMessageResponse(String content, String messageType) {
        this();
        this.content = content;
        this.messageType = messageType;
    }

    public ChatMessageResponse(String sessionId, String content, String messageType) {
        this(content, messageType);
        this.sessionId = sessionId;
    }

    // getters and setters
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getTokenUsage() {
        return tokenUsage;
    }

    public void setTokenUsage(Integer tokenUsage) {
        this.tokenUsage = tokenUsage;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "ChatMessageResponse{" +
                "messageId='" + messageId + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", content='" + content + '\'' +
                ", messageType='" + messageType + '\'' +
                ", createTime=" + createTime +
                ", finished=" + finished +
                ", model='" + model + '\'' +
                ", tokenUsage=" + tokenUsage +
                ", error='" + error + '\'' +
                '}';
    }
}
